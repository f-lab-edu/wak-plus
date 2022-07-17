package com.june0122.wakplus.data.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

class NetworkChecker @Inject constructor(
    applicationContext: Context
) {
    private val _networkState = MutableStateFlow<NetworkState>(NetworkState.None)
    val networkState: StateFlow<NetworkState> = _networkState

    private var previousNetworkState: NetworkState = NetworkState.None

    private val validTransportTypes = listOf(
        NetworkCapabilities.TRANSPORT_WIFI,
        NetworkCapabilities.TRANSPORT_CELLULAR
    )

    // https://developer.android.com/training/basics/network-ops/reading-network-state
    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)

            if (previousNetworkState == NetworkState.NotConnected) {
                _networkState.value = NetworkState.Reconnected.also {
                    previousNetworkState = it
                }
            } else {
                _networkState.value = NetworkState.Connected.also {
                    previousNetworkState = it
                }
            }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            _networkState.value = NetworkState.NotConnected
            previousNetworkState = NetworkState.NotConnected
        }
    }

    // connectivityManager: 앱에 시스템의 연결 상태를 알림
    private val connectivityManager: ConnectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    init {
        connectivityManager.run {
            initiateNetworkState(this)
            registerNetworkCallback(this)
        }
    }

    private fun initiateNetworkState(manager: ConnectivityManager) {
        val networkCapabilities = manager.activeNetwork?.let {
            manager.getNetworkCapabilities(it)
        }

        _networkState.value =
            if (networkCapabilities != null &&
                validTransportTypes.any { networkCapabilities.hasTransport(it) }
            ) {
                NetworkState.Connected.also { previousNetworkState = it }
            } else {
                NetworkState.NotConnected.also { previousNetworkState = it }
            }
    }

    // addTransportType(int transportType) : Adds the given transport requirement to this builder.
    private fun registerNetworkCallback(manager: ConnectivityManager) {
        val networkRequestBuilder = NetworkRequest.Builder().apply {
            validTransportTypes.forEach { addTransportType(it) }
        }
        manager.registerNetworkCallback(networkRequestBuilder.build(), networkCallback)
    }
}
