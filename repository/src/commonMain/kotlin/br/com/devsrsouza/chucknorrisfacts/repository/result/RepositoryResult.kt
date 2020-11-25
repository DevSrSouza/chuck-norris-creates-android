package br.com.devsrsouza.chucknorrisfacts.repository.result

import br.com.devsrsouza.chucknorrisfacts.api.result.ApiResult

sealed class RepositoryResult<T> {
    data class Success<T>(val value: T) : RepositoryResult<T>()
    data class NetworkError<T>(val error: ApiResult.Error<*>) : RepositoryResult<T>()
    data class GenericError<T>(val error: Throwable) : RepositoryResult<T>()
}