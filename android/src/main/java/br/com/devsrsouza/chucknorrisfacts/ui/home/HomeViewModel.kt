package br.com.devsrsouza.chucknorrisfacts.ui.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import br.com.devsrsouza.chucknorrisfacts.api.result.ApiResult
import br.com.devsrsouza.chucknorrisfacts.model.UIState
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.result.RepositoryResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// Dependency resolve bug in Kotlin with inner module, everything compiles,
// but the IDE does not resolve.
// https://youtrack.jetbrains.com/issue/KT-24309
class HomeViewModel(
        private val factsRepository: ChuckNorrisFactsRepository,
        private val networkStateFlow: StateFlow<Boolean>
) : ViewModel() {

    companion object {
        var SEARCH_DEBOUNCE_TIME_MS = 300L
            @VisibleForTesting set
    }

    private val _searchQueryFlow = MutableStateFlow<String>("")
    val searchQueryFlow: StateFlow<String> = _searchQueryFlow

    private val searchRequestFlow: Flow<RepositoryResult<List<Fact>>> = _searchQueryFlow
        .debounce(SEARCH_DEBOUNCE_TIME_MS)
        .filter(String::isNotBlank)
        .distinctUntilChanged()
        .flatMapLatest {
            flow {
                emit(factsRepository.searchFact(it))
            }
        }

    private val searchResultStateFlow = MutableStateFlow<UIState<List<Fact>>>(UIState.None())
    val searchResultLiveData: LiveData<UIState<List<Fact>>> = searchResultStateFlow.asLiveData()

    fun onSearchQueryChange(value: String) {
        _searchQueryFlow.value = value
    }

    init {
        setupSearchRequest()
        setupLoadingStateWatcher()
        setupLoadingStateWatcher()
        setupNoSearchStateWatcher()

        searchResultLiveData.observeForever {
            println(it::class.simpleName)
        }
    }

    private fun setupSearchRequest() {
        viewModelScope.launch {
            searchRequestFlow.collect {
                println("new request: $it")
                searchResultStateFlow.value = when(it) {
                    is RepositoryResult.Success -> UIState.Success(it.value)
                    is RepositoryResult.NetworkError -> {
                        val error = it.error
                        when(error) {
                            is ApiResult.ClientError -> UIState.NetworkClientError(error.responseError.message)
                            else -> UIState.Error(error.throwable)
                        }
                    }
                    is RepositoryResult.GenericError -> UIState.Error(it.error)
                }
            }
        }
    }

    private fun setupLoadingStateWatcher() {
        viewModelScope.launch {
            _searchQueryFlow
                .filter(String::isNotBlank)
                .collect {
                    searchResultStateFlow.value = UIState.Loading()
                }
        }
    }

    private fun setupNoSearchStateWatcher() {
        viewModelScope.launch {
            _searchQueryFlow
                .filter(String::isBlank)
                .collect {
                    searchResultStateFlow.value = UIState.None()
                }
        }
    }

    private fun updateSearchResultStateFlow(state: UIState<List<Fact>>) {

    }

}

class HomeViewModelFactory(
        private val factsRepository: ChuckNorrisFactsRepository,
        private val networkStateFlow: StateFlow<Boolean>,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            (HomeViewModel(factsRepository, networkStateFlow) as T)
}