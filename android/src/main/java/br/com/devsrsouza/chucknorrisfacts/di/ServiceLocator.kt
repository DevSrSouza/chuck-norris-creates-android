package br.com.devsrsouza.chucknorrisfacts.di

import androidx.annotation.VisibleForTesting
import br.com.devsrsouza.chucknorrisfacts.api.DefaultChuckNorrisFactsApi
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.repository.DefaultChuckNorrisFactsRepository
import io.ktor.client.engine.android.*
import kotlinx.coroutines.Dispatchers

object ServiceLocator {

    var factsRepository: ChuckNorrisFactsRepository? = null
        @VisibleForTesting set

    fun provideFactsRepository(): ChuckNorrisFactsRepository {
        return factsRepository ?: createFactsRepository()
    }

    private fun createFactsRepository(): ChuckNorrisFactsRepository {
        factsRepository = DefaultChuckNorrisFactsRepository(
            DefaultChuckNorrisFactsApi(
                Android.create {
                    connectTimeout = 30_000
                    socketTimeout = 30_000
                }
            ),
            Dispatchers.IO
        )

        return factsRepository!!
    }
}