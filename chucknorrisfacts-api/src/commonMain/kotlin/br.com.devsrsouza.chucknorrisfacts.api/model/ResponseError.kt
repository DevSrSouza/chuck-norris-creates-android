package br.com.devsrsouza.chucknorrisfacts.api.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseError(
    val error: String,
    val message: String,
    val status: Int,
    val timestamp: String,
    val violations: Map<String, String>
)