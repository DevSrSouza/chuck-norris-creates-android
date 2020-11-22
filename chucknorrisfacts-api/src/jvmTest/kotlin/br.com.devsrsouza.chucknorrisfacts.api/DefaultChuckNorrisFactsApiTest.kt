package br.com.devsrsouza.chucknorrisfacts.api

import br.com.devsrsouza.chucknorrisfacts.api.model.ChuckNorrisFact
import br.com.devsrsouza.chucknorrisfacts.api.model.ChuckNorrisFactsSearchResult
import io.ktor.client.engine.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import java.lang.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultChuckNorrisFactsApiTest {
    @Test
    fun `Search should call search endpoint with the given query value`() = runBlockingTest {
        val searchValue = "dota"
        val expectedEndpoint = "https://api.chucknorris.io/jokes/search?query=dota"
        val responseJson = """
            {"total":1,"result":[{"categories":[],"created_at":"2020-01-05 13:42:28.664997","icon_url":"https://assets.chucknorris.host/img/avatar/chuck-norris.png","id":"_VBRL88ASyu1Z1mtpfQ0hA","updated_at":"2020-01-05 13:42:28.664997","url":"https://api.chucknorris.io/jokes/_VBRL88ASyu1Z1mtpfQ0hA","value":"DOTA: 1st kill- First Blood 2nd kill- Double Kill 3rd kill- Triple Kill 4th kill- Monster Kill 5th kill- god-like 6th kill- Beyond god-like 7th kill- Chuck Norris-like"}]}
        """.trimIndent()
        val expectedResult = ChuckNorrisFactsSearchResult(
            total = 1,
            result = listOf(
                ChuckNorrisFact(
                    categories = emptyList(),
                    createdAt = "2020-01-05 13:42:28.664997",
                    iconUrl = "https://assets.chucknorris.host/img/avatar/chuck-norris.png",
                    id = "_VBRL88ASyu1Z1mtpfQ0hA",
                    updatedAt = "2020-01-05 13:42:28.664997",
                    url = "https://api.chucknorris.io/jokes/_VBRL88ASyu1Z1mtpfQ0hA",
                    value = "DOTA: 1st kill- First Blood 2nd kill- Double Kill 3rd kill- Triple Kill 4th kill- Monster Kill 5th kill- god-like 6th kill- Beyond god-like 7th kill- Chuck Norris-like"
                )
            )
        )

        val engineMock = MockEngine {
            if(it.url.fullUrl == expectedEndpoint) {
                respond(
                    content = responseJson,
                    headers = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                )
            } else {
                error("Endpoint requested for search not expected in test!")
            }
        }

        val defaultApi = DefaultChuckNorrisFactsApi(engineMock)
        val result = defaultApi.search(searchValue)

        assertEquals(expectedResult, result)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Search should throw IllegalArgumentException when the query value is empty`() = runBlockingTest {
        val searchValue = ""

        val httpEngineMock = MockEngine { respondOk() }
        val defaultApi = DefaultChuckNorrisFactsApi(httpEngineMock)

        defaultApi.search(searchValue)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `Search should throw IllegalArgumentException when the query value is blank`() = runBlockingTest {
        val searchValue = "     "

        val httpEngineMock = MockEngine { respondOk() }
        val defaultApi = DefaultChuckNorrisFactsApi(httpEngineMock)

        defaultApi.search(searchValue)
    }
}

