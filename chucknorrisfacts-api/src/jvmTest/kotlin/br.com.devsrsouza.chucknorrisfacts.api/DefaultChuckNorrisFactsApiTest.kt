package br.com.devsrsouza.chucknorrisfacts.api

import br.com.devsrsouza.chucknorrisfacts.api.model.ChuckNorrisFact
import br.com.devsrsouza.chucknorrisfacts.api.model.ChuckNorrisFactsSearchResult
import br.com.devsrsouza.chucknorrisfacts.api.model.ResponseError
import br.com.devsrsouza.chucknorrisfacts.api.result.ApiResult
import io.ktor.client.engine.*
import io.ktor.client.engine.mock.*
import io.ktor.http.*
import io.mockk.mockk
import kotlinx.coroutines.test.runBlockingTest
import java.lang.IllegalArgumentException
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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

        assertTrue(result is ApiResult.Success)
        assertEquals(expectedResult, result.value)
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

    @Test
    fun `Should return result ClientError when get a 400 status code`() = runBlockingTest {
        val searchValue = "dot"
        val expectedEndpoint = "https://api.chucknorris.io/jokes/search?query=dot"
        val expectedResponseError = ResponseError(
                error="Bad Request",
                message="search.query: tamanho deve estar entre 3 e 120",
                status=400,
                timestamp="2020-11-24T16:23:52.453Z",
                violations=mapOf("search.query" to "tamanho deve estar entre 3 e 120")
        )


        val engineMock = MockEngine {
            if(it.url.fullUrl == expectedEndpoint) {
                respond(
                        content = """{"timestamp":"2020-11-24T16:23:52.453Z","status":400,"error":"Bad Request","message":"search.query: tamanho deve estar entre 3 e 120","violations":{"search.query":"tamanho deve estar entre 3 e 120"}}""",
                        status = HttpStatusCode.BadRequest
                )
            } else {
                error("Endpoint requested for search not expected in test!")
            }
        }

        val defaultApi = DefaultChuckNorrisFactsApi(engineMock)
        val result = defaultApi.search(searchValue)

        assertTrue(result is ApiResult.ClientError)
        assertEquals(expectedResponseError, result.responseError)
    }

    @Test
    fun `Should return result Redirect when get a 300 status code`() = runBlockingTest {
        val searchValue = "dota"
        val expectedEndpoint = "https://api.chucknorris.io/jokes/search?query=dota"

        val engineMock = MockEngine {
            if(it.url.fullUrl == expectedEndpoint) {
                respondError(HttpStatusCode.UseProxy)
            } else {
                error("Endpoint requested for search not expected in test!")
            }
        }

        val defaultApi = DefaultChuckNorrisFactsApi(engineMock)
        val result = defaultApi.search(searchValue)

        assertTrue(result is ApiResult.Redirect)
    }

    @Test
    fun `Should return result ServerError when get a 500 status code`() = runBlockingTest {
        val searchValue = "dota"
        val expectedEndpoint = "https://api.chucknorris.io/jokes/search?query=dota"

        val engineMock = MockEngine {
            if(it.url.fullUrl == expectedEndpoint) {
                respondError(HttpStatusCode.InternalServerError)
            } else {
                error("Endpoint requested for search not expected in test!")
            }
        }

        val defaultApi = DefaultChuckNorrisFactsApi(engineMock)
        val result = defaultApi.search(searchValue)

        assertTrue(result is ApiResult.ServerError)
    }

    @Test
    fun `Should return result UnknownServerError when get a 600+ status code`() = runBlockingTest {
        val searchValue = "dota"
        val expectedEndpoint = "https://api.chucknorris.io/jokes/search?query=dota"

        val engineMock = MockEngine {
            if(it.url.fullUrl == expectedEndpoint) {
                respondError(HttpStatusCode(600, "Error"))
            } else {
                error("Endpoint requested for search not expected in test!")
            }
        }

        val defaultApi = DefaultChuckNorrisFactsApi(engineMock)
        val result = defaultApi.search(searchValue)

        assertTrue(result is ApiResult.UnknownServerError)
    }

    @Test
    fun `Should return result UnknownError when get a json parse error`() = runBlockingTest {
        val searchValue = "dota"
        val expectedEndpoint = "https://api.chucknorris.io/jokes/search?query=dota"
        val responseJson = """
            {"total":1}
        """.trimIndent()

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

        assertTrue(result is ApiResult.UnknownError)
    }

}

