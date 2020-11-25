package br.com.devsrsouza.chucknorrisfacts.repository

import br.com.devsrsouza.chucknorrisfacts.api.ChuckNorrisFactsApi
import br.com.devsrsouza.chucknorrisfacts.api.result.ApiResult
import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact
import br.com.devsrsouza.chucknorrisfacts.repository.model.toFactList
import br.com.devsrsouza.chucknorrisfacts.repository.result.RepositoryResult
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class DefaultChuckNorrisFactsRepository(
        private val networkSource: ChuckNorrisFactsApi,
        private val ioDispatcher: CoroutineDispatcher
) : ChuckNorrisFactsRepository {

    override suspend fun searchFact(
            queryValue: String
    ): RepositoryResult<List<Fact>> = withContext(ioDispatcher) {
        require(queryValue.isNotBlank()) { "queryValue should not be blank!" }

        try {
            val result = networkSource.search(queryValue)

            when(result) {
                is ApiResult.Success -> RepositoryResult.Success(result.value.toFactList())
                is ApiResult.Error -> RepositoryResult.NetworkError(result)

            }
        } catch (error: Throwable) {
            RepositoryResult.GenericError(error)
        }
    }

}