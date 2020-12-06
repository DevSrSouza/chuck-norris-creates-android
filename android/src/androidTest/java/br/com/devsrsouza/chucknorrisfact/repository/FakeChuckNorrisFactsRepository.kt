package br.com.devsrsouza.chucknorrisfact.repository

import br.com.devsrsouza.chucknorrisfacts.api.model.ResponseError
import br.com.devsrsouza.chucknorrisfacts.api.result.ApiResult
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.result.RepositoryResult
import kotlinx.coroutines.delay

class FakeChuckNorrisFactsRepository : ChuckNorrisFactsRepository {

    private var repositoryResult: RepositoryResult<List<Fact>>? = null
    private var delayInMs: Long = 0

    fun setSuccess(result: List<Fact>) {
        repositoryResult = RepositoryResult.Success(result)
    }

    fun setClientError(message: String) {
        repositoryResult = RepositoryResult.NetworkError<List<Fact>>(ApiResult.ClientError<List<Fact>>(
            responseError = ResponseError("400", message, 400, "00000", emptyMap()),
            throwable = Throwable(),
            responseCode = 400
        ))
    }

    fun setError() {
        repositoryResult = RepositoryResult.GenericError<List<Fact>>(Throwable())
    }

    fun setResponseDelay(delay: Long) {
        delayInMs = delay
    }

    fun reset() {
        repositoryResult = null
        delayInMs = 0
    }

    override suspend fun searchFact(queryValue: String): RepositoryResult<List<Fact>> {
        delay(delayInMs)

        return repositoryResult!!
    }
}