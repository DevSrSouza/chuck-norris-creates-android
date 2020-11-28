package br.com.devsrsouza.chucknorrisfacts.ui.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import br.com.devsrsouza.chucknorrisfacts.ChuckNorrisFactsApplication
import br.com.devsrsouza.chucknorrisfacts.R
import br.com.devsrsouza.chucknorrisfacts.databinding.FragmentHomeBinding
import br.com.devsrsouza.chucknorrisfacts.util.getQueryTextChangeStateFlow
import br.com.devsrsouza.chucknorrisfacts.util.requireChuckNorrisFactsApplication
import br.com.devsrsouza.chucknorrisfacts.util.viewBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val binding by viewBinding(FragmentHomeBinding::bind)
    private val viewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            requireChuckNorrisFactsApplication().factsRepository
        )
    }

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        val search = menu.findItem(R.id.search_item)
        setupSearchChangePublishToViewModel(search.actionView as SearchView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_fragment_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun setupSearchChangePublishToViewModel(searchView: SearchView) {
        lifecycleScope.launch {
            searchView.getQueryTextChangeStateFlow()
                    .collect {
                        viewModel.onSearchQueryChange(it)
                    }
        }
    }
}