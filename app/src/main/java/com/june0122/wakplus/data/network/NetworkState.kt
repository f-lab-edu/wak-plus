package com.june0122.wakplus.data.network

sealed class NetworkState {
    object None : NetworkState()
    object Connected : NetworkState()
    object Reconnected : NetworkState()
    object NotConnected : NetworkState()
}