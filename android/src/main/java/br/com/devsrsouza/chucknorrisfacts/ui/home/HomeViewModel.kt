package br.com.devsrsouza.chucknorrisfacts.ui.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import br.com.devsrsouza.chucknorrisfacts.model.UIState
import br.com.devsrsouza.chucknorrisfacts.model.repositoryResultAsUIState
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

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

    private val searchResultStateFlow: Flow<UIState<List<Fact>>> = _searchQueryFlow
            .debounce(SEARCH_DEBOUNCE_TIME_MS)
            .distinctUntilChanged()
            .flowOn(Dispatchers.Default)
            .flatMapLatest {
                flow<UIState<List<Fact>>> {
                    when {
                        it.isBlank() -> emit(UIState.None())
                        else -> {
                            emit(UIState.Loading())

                            emit(repositoryResultAsUIState(factsRepository.searchFact(it)))
                        }
                    }

                }
            }

    val searchResultLiveData: LiveData<UIState<List<Fact>>> = searchResultStateFlow.asLiveData()

    fun onSearchQueryChange(value: String) {
        _searchQueryFlow.value = value
    }

}

class HomeViewModelFactory(
        private val factsRepository: ChuckNorrisFactsRepository,
        private val networkStateFlow: StateFlow<Boolean>,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            (HomeViewModel(factsRepository, networkStateFlow) as T)
}