package br.com.devsrsouza.chucknorrisfacts.api

import br.com.devsrsouza.chucknorrisfacts.api.model.ChuckNorrisFactsSearchResult
import br.com.devsrsouza.chucknorrisfacts.api.model.ResponseError
import br.com.devsrsouza.chucknorrisfacts.api.result.ApiResult
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.util.cio.*
import kotlinx.serialization.json.Json

class DefaultChuckNorrisFactsApi(
    private val engine: HttpClientEngine
) : ChuckNorrisFactsApi {
    private val httpClient = HttpClient(engine) {
        install(JsonFeature) {
            serializer = KotlinxSerializer()
            accept(ContentType.Application.Json)
        }
    }

    override suspend fun search(
        queryValue: String
    ): ApiResult<ChuckNorrisFactsSearchResult> {
        require(queryValue.isNotBlank()) { "queryValue should not be blank!" }

        val endpoint = ChuckNorrisFactsApi.searchEndpointWithQuery(queryValue)

        return handleError { httpClient.get(endpoint) }
    }

    private suspend inline fun <T> handleError(block: () -> T): ApiResult<T> {
        try {
            return ApiResult.Success(block())
        } catch (err: RedirectResponseException) {
            return ApiResult.Redirect(
                err,
                err.response.status.value
            )
        } catch (err: ClientRequestException) {
            val res = err.response
            val errorJson = res.content.toByteArray().decodeToString()

            // Can be a json parse error
            try {
                val responseError = Json.decodeFromString(ResponseError.serializer(), errorJson)

                return ApiResult.ClientError(
                    responseError,
                    err,
                    res.status.value
                )
            } catch (thw: Throwable) {
                return ApiResult.UnknownError(thw)
            }
        } catch (err: ServerResponseException) {
            return ApiResult.ServerError(
                err,
                err.response.status.value
            )
        } catch (err: ResponseException) {
            return ApiResult.UnknownServerError(
                err,
                err.response.status.value
            )
        } catch (thw: Throwable) {
            return ApiResult.UnknownError(thw)
        }
    }


}
