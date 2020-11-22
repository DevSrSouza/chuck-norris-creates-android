package br.com.devsrsouza.chucknorrisfacts.api

import br.com.devsrsouza.chucknorrisfacts.api.model.ChuckNorrisFactsSearchResult
import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.request.*
import io.ktor.http.*

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
    ): ChuckNorrisFactsSearchResult {
        require(queryValue.isNotBlank()) { "queryValue should not be blank!" }

        val endpoint = ChuckNorrisFactsApi.searchEndpointWithQuery(queryValue)

        return httpClient.get(endpoint)
    }


}
