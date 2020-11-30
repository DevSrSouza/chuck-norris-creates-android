package br.com.devsrsouza.chucknorrisfact.repository

import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.result.RepositoryResult

class FakeChuckNorrisFactsRepository : ChuckNorrisFactsRepository {

    private lateinit var repositoryResult: RepositoryResult<List<Fact>>

    fun setSuccess(result: List<Fact>) {
        repositoryResult = RepositoryResult.Success(result)
    }

    override suspend fun searchFact(queryValue: String): RepositoryResult<List<Fact>> {
        return repositoryResult
    }
}