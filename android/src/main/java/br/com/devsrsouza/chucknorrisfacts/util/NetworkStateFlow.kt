package br.com.devsrsouza.chucknorrisfacts.util

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onCompletion

class NetworkStateFlow(
    private val context: Context,
    private val flow: MutableStateFlow<Boolean>
) : StateFlow<Boolean> by flow {

    private val networkReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            flow.value = context.isNetworkConnected
        }
    }

    init {
        flow.value = context.isNetworkConnected

        context.registerReceiver(
            networkReceiver,
            IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        onCompletion {
            context.unregisterReceiver(networkReceiver)
        }
    }

    private val Context.isNetworkConnected: Boolean
        get() = (getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager)
            ?.activeNetworkInfo?.isConnectedOrConnecting == true

}
