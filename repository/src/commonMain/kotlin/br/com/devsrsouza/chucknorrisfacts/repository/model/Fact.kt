package br.com.devsrsouza.chucknorrisfacts.repository.model

import br.com.devsrsouza.chucknorrisfacts.api.model.ChuckNorrisFactsSearchResult

data class Fact(
    val id: String,
    val categories: List<String>,
    val value: String
)

val Fact.mainCategoryOrNull: String? get() = categories.firstOrNull()

fun ChuckNorrisFactsSearchResult.toFactList() = result.map {
    Fact(
            id = it.id,
            categories = it.categories,
            value = it.value
    )
}