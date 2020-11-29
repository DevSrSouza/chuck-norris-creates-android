package br.com.devsrsouza.chucknorrisfacts.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(
    private val factsRepository: ChuckNorrisFactsRepository
) : ViewModel() {

    private val _searchQueryFlow = MutableStateFlow<String>("")
    val searchQueryFlow: StateFlow<String> = _searchQueryFlow

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