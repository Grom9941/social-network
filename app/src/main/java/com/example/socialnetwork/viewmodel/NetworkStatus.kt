package com.example.socialnetwork.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.LiveData

class NetworkStatus(context: Context) : LiveData<Boolean>() {

    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    private val builder: NetworkRequest.Builder = NetworkRequest.Builder()

    private val networkStateObject = object : ConnectivityManager.NetworkCallback() {

        override fun onAvailable(network: Network) {
            postValue(true)
        }

        override fun onLost(network: Network) {
            postValue(false)
        }
    }

    override fun onActive() {
        super.onActive()
        connectivityManager.registerNetworkCallback(builder.build(), networkStateObject)
    }

    override fun onInactive() {
        super.onInactive()
        try {
            connectivityManager.unregisterNetworkCallback(networkStateObject)
        } catch (exception: IllegalArgumentException) {
            Log.i(NETWORK_STATUS_TAG, "unregister error")
        }
    }

    companion object {
        private const val NETWORK_STATUS_TAG = "com.example.socialnetwork.viewmodel_NetworkStatus"
    }
}