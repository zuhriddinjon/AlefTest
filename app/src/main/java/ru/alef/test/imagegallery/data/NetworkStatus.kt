package ru.alef.test.imagegallery.data

import androidx.annotation.StringRes

sealed class NetworkStatus {
    object Loading : NetworkStatus()
    object Success : NetworkStatus()
    class Error(@StringRes val errorMsg: Int) : NetworkStatus()
}