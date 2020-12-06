package br.com.devsrsouza.chucknorrisfacts

import android.app.Application
import br.com.devsrsouza.chucknorrisfacts.di.ServiceLocator
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import kotlinx.coroutines.flow.StateFlow

class ChuckNorrisFactsApplication : Application() {

    val factsRepository: ChuckNorrisFactsRepository
        get() = ServiceLocator.provideFactsRepository()

    val networkStateFlow: StateFlow<Boolean>
        get() = ServiceLocator.provideNetworkStateFlow(this)

    override fun onCreate() {
        super.onCreate()
    }
}