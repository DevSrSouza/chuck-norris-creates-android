package br.com.devsrsouza.chucknorrisfacts.model

import br.com.devsrsouza.chucknorrisfacts.repository.model.Fact

sealed class UIState<T> {
    class None<T> : UIState<T>()
    class Success<T>(val value: T) : UIState<T>()
    class Loading<T> : UIState<T>()


    abstract class UIStateError<T> : UIState<T>()

    class NetworkNotAvailable<T> : UIStateError<T>()
    class NetworkClientError<T>(val message: String) : UIStateError<T>()
    class Error<T>(val error: Throwable) : UIStateError<T>()
}