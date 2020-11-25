package br.com.devsrsouza.chucknorrisfacts.repository

import br.com.devsrsouza.chucknorrisfacts.api.ChuckNorrisFactsApi
import br.com.devsrsouza.chucknorrisfacts.api.model.ChuckNorrisFact
import br.com.devsrsouza.chucknorrisfacts.api.model.ChuckNorrisFactsSearchResult
import br.com.devsrsouza.chucknorrisfacts.api.result.ApiResult
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.result.RepositoryResult
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Rule
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class DefaultChuckNorrisFactsRepositoryTest {

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Test
    fun `searchFact should result in NetworkError when its a error related to API`() = mainCoroutineRule.runBlockingTest {
        val api = mockk<ChuckNorrisFactsApi>()
        val expectedError = ApiResult.ServerError<ChuckNorrisFactsSearchResult>(Exception(), 500)

        coEvery { api.search("dota") } returns expectedError

        val repository = DefaultChuckNorrisFactsRepository(
                api,
                Dispatchers.Main
        )

        val result = repository.searchFact("dota")

        assertTrue(result is RepositoryResult.NetworkError)
        assertSame(expectedError, result.error)
    }

    @Test
    fun `searchFact should result in Success when its got a valid response from the API`() = mainCoroutineRule.runBlockingTest {
        val api = mockk<ChuckNorrisFactsApi>()

        val categories = listOf("Games")
        val value = "DOTA: 1st kill- First Blood 2nd kill- Double Kill 3rd kill- Triple Kill 4th kill- Monster Kill 5th kill- god-like 6th kill- Beyond god-like 7th kill- Chuck Norris-like"
        val apiResponse = ChuckNorrisFactsSearchResult(
                total = 1,
                result = listOf(
                        ChuckNorrisFact(
                                categories = categories,
                                createdAt = "",
                                iconUrl = "",
                                id = "",
                                updatedAt = "",
                                url = "",
                                value = value
                        )
                )
        )
        val expectedFactList = listOf(
                Fact(
                        categories = categories,
                        value = value
                )
        )

        coEvery { api.search("dota") } returns ApiResult.Success(apiResponse)

        val repository = DefaultChuckNorrisFactsRepository(
                api,
                Dispatchers.Main
        )

        val result = repository.searchFact("dota")

        assertTrue(result is RepositoryResult.Success)
        assertEquals(expectedFactList, result.value)
    }

}