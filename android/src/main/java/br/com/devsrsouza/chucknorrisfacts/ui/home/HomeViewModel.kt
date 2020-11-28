package br.com.devsrsouza.chucknorrisfacts.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _searchQueryFlow = MutableStateFlow<String>("")
    private val searchQueryFlow: StateFlow<String> = _searchQueryFlow

    fun onSearchQueryChange(value: String) {
        _searchQueryFlow.value = value
    }

    init {

    }
}