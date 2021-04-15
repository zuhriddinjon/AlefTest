package ru.alef.test.imagegallery.ui.gallery.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.alef.test.imagegallery.R
import ru.alef.test.imagegallery.data.NetworkManager
import ru.alef.test.imagegallery.data.NetworkStatus
import java.net.UnknownHostException

interface IGalleryViewModel {
    val liveStatus: LiveData<NetworkStatus>
    val livePhotos: LiveData<List<String>>
    fun loadPhotos()
}

class GalleryViewModel : ViewModel(), IGalleryViewModel {
    override val liveStatus = MutableLiveData<NetworkStatus>()
    override val livePhotos = MutableLiveData<List<String>>()
    private val apiService = NetworkManager.getInstance()

    override fun loadPhotos() {
        viewModelScope.launch {
            try {
                liveStatus.postValue(NetworkStatus.Loading)
                delay(5000)
                val photos = apiService.getPhotoList()
                liveStatus.postValue(NetworkStatus.Success)
                livePhotos.postValue(photos)
            } catch (e: Exception) {
                Log.e(javaClass.simpleName, e.message ?: "Unknown error")
                if (e is UnknownHostException)
                    liveStatus.postValue(NetworkStatus.Error(R.string.error_offline))
                else
                    liveStatus.postValue(NetworkStatus.Error(R.string.error_server))
            }
        }
    }
}