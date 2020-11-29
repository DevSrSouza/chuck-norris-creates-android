package br.com.devsrsouza.chucknorrisfacts

import android.app.Application
import br.com.devsrsouza.chucknorrisfacts.di.ServiceLocator
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository

class ChuckNorrisFactsApplication : Application() {

    val factsRepository: ChuckNorrisFactsRepository
        get() = ServiceLocator.provideFactsRepository()

    override fun onCreate() {
        super.onCreate()
    }
}