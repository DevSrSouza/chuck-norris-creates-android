package br.com.devsrsouza.chucknorrisfacts.ui.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.result.RepositoryResult
import kotlinx.coroutines.flow.*

class HomeViewModel(
        private val factsRepository: ChuckNorrisFactsRepository
) : ViewModel() {

    companion object {
        var SEARCH_DEBOUNCE_TIME_MS = 300L
            @VisibleForTesting set
    }

    private val _searchQueryFlow = MutableStateFlow<String>("")
    val searchQueryFlow: StateFlow<String> = _searchQueryFlow

    val searchResultFlow: Flow<RepositoryResult<List<Fact>>> = _searchQueryFlow
        .debounce(SEARCH_DEBOUNCE_TIME_MS)
        .filter(String::isNotBlank)
        .distinctUntilChanged()
        .flatMapLatest {
            flow {
                emit(factsRepository.searchFact(it))
            }
        }

    val searchResultLiveData: LiveData<RepositoryResult<List<Fact>>> = searchResultFlow.asLiveData()

    fun onSearchQueryChange(value: String) {
        _searchQueryFlow.value = value
    }

    init {

    }

}

class HomeViewModelFactory(
        private val factsRepository: ChuckNorrisFactsRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            (HomeViewModel(factsRepository) as T)
}