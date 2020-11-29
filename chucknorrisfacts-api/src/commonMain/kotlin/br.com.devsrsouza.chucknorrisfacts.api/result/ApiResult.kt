package br.com.devsrsouza.chucknorrisfacts.api.result

import br.com.devsrsouza.chucknorrisfacts.api.model.ResponseError

sealed class ApiResult<T> {
    class Success<T>(val value: T) : ApiResult<T>()

    abstract class Error<T>(
        val throwable: Throwable,
        val responseCode: Int
    ) : ApiResult<T>()

    // 3XX code
    class Redirect<T>(
        throwable: Throwable,
        responseCode: Int,
    ) : Error<T>(throwable, responseCode)

    // 4XX code
    class ClientError<T>(
        val responseError: ResponseError,
        throwable: Throwable,
        responseCode: Int,
    ) : Error<T>(throwable, responseCode)

    // 5XX code
    class ServerError<T>(
        throwable: Throwable,
        responseCode: Int,
    ) : Error<T>(throwable, responseCode)

    // 600+ code
    class UnknownServerError<T>(
        throwable: Throwable,
        responseCode: Int,
    ) : Error<T>(throwable, responseCode)

    // Non server response error, could be a json parsing error.
    class UnknownError<T>(
        throwable: Throwable,
    ) : Error<T>(throwable, -1)
}