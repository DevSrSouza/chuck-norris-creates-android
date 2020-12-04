package br.com.devsrsouza.chucknorrisfacts.model

import br.com.devsrsouza.chucknorrisfacts.api.result.ApiResult
import br.com.devsrsouza.chucknorrisfacts.repository.result.RepositoryResult

sealed class UIState<T> {
    class None<T> : UIState<T>()
    class Success<T>(val value: T) : UIState<T>()
    class Loading<T> : UIState<T>()


    abstract class UIStateError<T> : UIState<T>()

    class NetworkNotAvailable<T> : UIStateError<T>()
    class NetworkClientError<T>(val message: String) : UIStateError<T>()
    class Error<T>(val error: Throwable) : UIStateError<T>()
}

internal fun <T> repositoryResultAsUIState(
        result: RepositoryResult<T>
): UIState<T> =  when(result) {
    is RepositoryResult.Success -> UIState.Success(result.value)
    is RepositoryResult.NetworkError -> {
        val error = result.error
        when(error) {
            is ApiResult.ClientError -> UIState.NetworkClientError(error.responseError.message)
            else -> UIState.Error(error.throwable)
        }
    }
    is RepositoryResult.GenericError -> UIState.Error(result.error)
}