package br.com.devsrsouza.chucknorrisfacts.di

import android.app.Activity
import android.content.Context
import androidx.annotation.VisibleForTesting
import br.com.devsrsouza.chucknorrisfacts.api.DefaultChuckNorrisFactsApi
import br.com.devsrsouza.chucknorrisfacts.repository.ChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.repository.DefaultChuckNorrisFactsRepository
import br.com.devsrsouza.chucknorrisfacts.util.share.ContentShare
import br.com.devsrsouza.chucknorrisfacts.util.NetworkStateFlow
import br.com.devsrsouza.chucknorrisfacts.util.share.ContentShareImpl
import io.ktor.client.engine.android.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object ServiceLocator {

    var factsRepository: ChuckNorrisFactsRepository? = null
        @VisibleForTesting set
    var networkStateFlow: StateFlow<Boolean>? = null
        @VisibleForTesting set
    var contentShare: ContentShare? = null
        @VisibleForTesting set

    fun provideFactsRepository(): ChuckNorrisFactsRepository = factsRepository ?: createFactsRepository()

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

    fun provideNetworkStateFlow(
        context: Context
    ): StateFlow<Boolean> = networkStateFlow ?: createNetworkStateFlow(context)

    private fun createNetworkStateFlow(
        context: Context
    ): StateFlow<Boolean> {
        networkStateFlow = NetworkStateFlow(context, MutableStateFlow(false))

        return networkStateFlow!!
    }

    fun provideContentShare(
            activity: Activity
    ): ContentShare = contentShare ?: createContentShare(activity)

    fun createContentShare(activity: Activity): ContentShare {
        contentShare = ContentShareImpl(activity)

        return contentShare!!
    }
}