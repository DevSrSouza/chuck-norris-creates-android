package br.com.devsrsouza.chucknorrisfacts.api

import br.com.devsrsouza.chucknorrisfacts.api.model.ChuckNorrisFactsSearchResult
import br.com.devsrsouza.chucknorrisfacts.api.result.ApiResult
import io.ktor.client.engine.*

/**
 * @returns the default implementation for the [ChuckNorrisFactsApi]
 */
fun ChuckNorrisFactsApi(
    engine: HttpClientEngine
): ChuckNorrisFactsApi = DefaultChuckNorrisFactsApi(engine)

/**
 * Interface that represents the API from [api.chucknorris.io]
 */
interface ChuckNorrisFactsApi {
    /**
     * Search Chuck Norris Facts at the [api.chucknorris.io] API.
     */
    suspend fun search(queryValue: String): ApiResult<ChuckNorrisFactsSearchResult>

    companion object {
        const val API_BASE_URL = "https://api.chucknorris.io/jokes"

        const val API_SEARCH_ENDPOINT = "search"
        const val API_SEARCH_QUERY_PARAM = "query"

        /**
         * @returns the chucknorrisfacts endpoint url for the search with the given [queryValue] to param [API_SEARCH_ENDPOINT].
         */
        fun searchEndpointWithQuery(queryValue: String): String {
            return "$API_BASE_URL/$API_SEARCH_ENDPOINT?$API_SEARCH_QUERY_PARAM=$queryValue"
        }
    }
}