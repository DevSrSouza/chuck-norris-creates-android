package br.com.devsrsouza.chucknorrisfacts.repository

import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.result.RepositoryResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ChuckNorrisFactsRepository {
    suspend fun searchFact(queryValue: String): RepositoryResult<List<Fact>>
}