package br.com.devsrsouza.chucknorrisfacts.ui.home

import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class HomeViewModelTest {

    private lateinit var factsRepository: ChuckNorrisFactsRepository
    private lateinit var homeViewModel: HomeViewModel

    @Before
    fun setupViewModel() {
        factsRepository = mockk()

        homeViewModel = HomeViewModel(factsRepository)
    }

    @Test
    fun `onSearchQueryChange should publish the new state to the searchQueryFlow`() = runBlockingTest {
        val searchValue = "dota"

        homeViewModel.onSearchQueryChange(searchValue)

        assertEquals(searchValue, homeViewModel.searchQueryFlow.value)
    }
}