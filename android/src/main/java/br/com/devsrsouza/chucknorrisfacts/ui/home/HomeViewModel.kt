package br.com.devsrsouza.chucknorrisfacts.ui.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.*
import br.com.devsrsouza.chucknorrisfacts.model.UIState
import br.com.devsrsouza.chucknorrisfacts.model.repositoryResultAsUIState
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.util.share.ContentShare
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

// Dependency resolve bug in Kotlin with inner module, everything compiles,
// but the IDE does not resolve.
// https://youtrack.jetbrains.com/issue/KT-24309
class HomeViewModel(
        private val factsRepository: ChuckNorrisFactsRepository,
        private val networkStateFlow: StateFlow<Boolean>,
        private val contentShare: ContentShare
) : ViewModel() {

    companion object {
        var SEARCH_DEBOUNCE_TIME_MS = 300L
            @VisibleForTesting set
    }

    private val _searchQueryFlow = MutableStateFlow<String>("")
    val searchQueryFlow: StateFlow<String> = _searchQueryFlow

    val searchResultFlow: Flow<UIState<List<Fact>>> = _searchQueryFlow
            .debounce(SEARCH_DEBOUNCE_TIME_MS)
            .distinctUntilChanged()
            .flowOn(Dispatchers.Default)
            .flatMapLatest {
                flow<UIState<List<Fact>>> {
                    // check if there is Network unavailable
                    if(!networkStateFlow.value) {
                        // if not, emit NetworkNotAvailable State
                        emit(UIState.NetworkNotAvailable())

                        // Await until receive a update from the Network with State true.
                        networkStateFlow.first { it }
                    }

                    when {
                        it.isBlank() -> emit(UIState.None()) // if there is not text, emit state None
                        else -> {
                            // if there is search text, emit first Loading state
                            emit(UIState.Loading())

                            // and emit the result from the network when the API respond in the future.
                            emit(repositoryResultAsUIState(factsRepository.searchFact(it)))
                        }
                    }
                }
            }

    val searchResultLiveData: LiveData<UIState<List<Fact>>> = searchResultFlow.asLiveData()

    fun onSearchQueryChange(value: String) {
        _searchQueryFlow.value = value
    }

    fun shareFact(fact: Fact) {
        contentShare.shareText(fact.value)
    }

}

class HomeViewModelFactory(
        private val factsRepository: ChuckNorrisFactsRepository,
        private val networkStateFlow: StateFlow<Boolean>,
        private val contentShare: ContentShare,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>) =
            (HomeViewModel(factsRepository, networkStateFlow, contentShare) as T)
}