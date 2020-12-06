package br.com.devsrsouza.chucknorrisfacts.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import br.com.devsrsouza.chucknorrisfacts.api.model.ResponseError
import br.com.devsrsouza.chucknorrisfacts.api.result.ApiResult
import br.com.devsrsouza.chucknorrisfacts.model.UIState
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.result.RepositoryResult
import br.com.devsrsouza.chucknorrisfacts.util.share.ContentShare
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.Channel.Factory.UNLIMITED
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private lateinit var factsRepository: ChuckNorrisFactsRepository
    private lateinit var contentShare: ContentShare
    private lateinit var homeViewModel: HomeViewModel
    private val networkStateFlow = MutableStateFlow(false)
    private val testDispatcher = TestCoroutineDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        Dispatchers.setMain(testDispatcher)
        factsRepository = mockk()
        contentShare = mockk()

        homeViewModel = HomeViewModel(factsRepository, networkStateFlow, contentShare)
    }

    @Test
    fun `onSearchQueryChange should publish the new state to the searchQueryFlow`() {
        val searchValue = "dota"

        homeViewModel.onSearchQueryChange("")
        assertEquals("", homeViewModel.searchQueryFlow.value)

        homeViewModel.onSearchQueryChange(searchValue)

        assertEquals(searchValue, homeViewModel.searchQueryFlow.value)
    }

    @Test
    fun `searchResultFlow should Result UIState Success when there is network and the API respond Success`() = runBlocking {
        val searchValue = "dota"
        val facts = listOf(
                Fact("00001", listOf("Games"), "Simple fact")
        )
        networkStateFlow.value = true

        coEvery { factsRepository.searchFact(searchValue) } returns RepositoryResult.Success(facts)

        val initialValue = homeViewModel.searchResultFlow.first()
        assertTrue(initialValue is UIState.None)

        homeViewModel.onSearchQueryChange(searchValue)

        val result = homeViewModel.searchResultFlow.take(2).toList()

        val loadingState = result[0]
        val successState = result[1]

        assertTrue(loadingState is UIState.Loading)
        assertTrue(successState is UIState.Success)
        assertEquals(facts, successState.value)
    }

    @Test
    fun `searchResultFlow should Result UIState NetworkNotAvailable when there is not network available`() = runBlocking {
        networkStateFlow.value = true

        val initialValue = homeViewModel.searchResultFlow.first()
        assertTrue(initialValue is UIState.None)

        networkStateFlow.value = false

        homeViewModel.onSearchQueryChange("dota")

        val result = homeViewModel.searchResultFlow.first()
        assertTrue(result is UIState.NetworkNotAvailable)
    }

    @Test
    fun `searchResultFlow should complete with Success State when the network is unavailable and turns on`() = runBlocking {
        val searchValue = "dota"
        val facts = listOf(
            Fact("00001", listOf("Games"), "Simple fact")
        )

        networkStateFlow.value = false

        coEvery { factsRepository.searchFact(searchValue) } returns RepositoryResult.Success(facts)


        homeViewModel.onSearchQueryChange(searchValue)

        val resultChannel = Channel<UIState<List<Fact>>>(UNLIMITED)

        val job = launch {
            homeViewModel.searchResultFlow.collect {
                resultChannel.offer(it)
            }
        }

        val networkNotAvailable = resultChannel.receive()
        assertTrue(networkNotAvailable is UIState.NetworkNotAvailable)

        networkStateFlow.value = true

        val loading = resultChannel.receive()
        assertTrue(loading is UIState.Loading)

        val success = resultChannel.receive()
        assertTrue(success is UIState.Success)
        assertEquals(facts, success.value)

        job.cancel()
    }

    @Test
    fun `searchResultFlow should Result UIState NetworkClientError when repository returns NetworkError with ApiError_ClientError`() = runBlocking {
        val searchValue = "dota"
        val clientErrorMessage = "error message"
        networkStateFlow.value = true

        coEvery { factsRepository.searchFact(searchValue) } returns RepositoryResult.NetworkError<List<Fact>>(
            ApiResult.ClientError<List<Fact>>(
                ResponseError(
                    error = "400",
                    message = clientErrorMessage,
                    status = 400,
                    violations = emptyMap(),
                    timestamp = "0000"
                ),
                Exception(),
                400
            )
        )

        val initialValue = homeViewModel.searchResultFlow.first()
        assertTrue(initialValue is UIState.None)

        homeViewModel.onSearchQueryChange(searchValue)

        val result = homeViewModel.searchResultFlow.take(2).toList()

        val loadingState = result[0]
        val errorState = result[1]

        assertTrue(loadingState is UIState.Loading)
        assertTrue(errorState is UIState.NetworkClientError)
        assertEquals(clientErrorMessage, errorState.message)
    }

    @Test
    fun `searchResultFlow should Result UIState Error when repository returns NetworkError that is not ApiError_ClientError`() = runBlocking {
        val searchValue = "dota"
        networkStateFlow.value = true
        val error = Throwable("error")

        coEvery { factsRepository.searchFact(searchValue) } returns RepositoryResult.NetworkError<List<Fact>>(
            ApiResult.ServerError<List<Fact>>(
                error,
                500
            )
        )

        val initialValue = homeViewModel.searchResultFlow.first()
        assertTrue(initialValue is UIState.None)

        homeViewModel.onSearchQueryChange(searchValue)

        val result = homeViewModel.searchResultFlow.take(2).toList()

        val loadingState = result[0]
        val errorState = result[1]

        assertTrue(loadingState is UIState.Loading)
        assertTrue(errorState is UIState.Error)
        assertEquals(error, errorState.error)
    }

    @Test
    fun `searchResultFlow should Result UIState Error when repository returns GenericError`() = runBlocking {
        val searchValue = "dota"
        networkStateFlow.value = true
        val error = Throwable("error")

        coEvery { factsRepository.searchFact(searchValue) } returns RepositoryResult.GenericError<List<Fact>>(
            error
        )

        val initialValue = homeViewModel.searchResultFlow.first()
        assertTrue(initialValue is UIState.None)

        homeViewModel.onSearchQueryChange(searchValue)

        val result = homeViewModel.searchResultFlow.take(2).toList()

        val loadingState = result[0]
        val errorState = result[1]

        assertTrue(loadingState is UIState.Loading)
        assertTrue(errorState is UIState.Error)
        assertEquals(error, errorState.error)
    }

    @Test
    fun `shareFact should share the Fact value`() = runBlocking {
        val fact = Fact("00001", listOf("Games"), "Simple fact")

        every { contentShare.shareText(fact.value) } returns Unit

        homeViewModel.shareFact(fact)

        verify { contentShare.shareText(fact.value) }
    }

}