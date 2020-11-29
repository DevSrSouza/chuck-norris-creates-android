package br.com.devsrsouza.chucknorrisfacts.repository.model

import br.com.devsrsouza.chucknorrisfacts.api.model.ChuckNorrisFactsSearchResult

data class Fact(
    val categories: List<String>,
    val value: String
)

fun ChuckNorrisFactsSearchResult.toFactList() = result.map {
    Fact(
            categories = it.categories,
            value = it.value
    )
}