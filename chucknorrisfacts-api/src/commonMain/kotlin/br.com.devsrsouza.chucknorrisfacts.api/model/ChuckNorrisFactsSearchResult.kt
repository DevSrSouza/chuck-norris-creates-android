package br.com.devsrsouza.chucknorrisfacts.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ChuckNorrisFactsSearchResult(
        val result: List<ChuckNorrisFact>,
        val total: Int
)