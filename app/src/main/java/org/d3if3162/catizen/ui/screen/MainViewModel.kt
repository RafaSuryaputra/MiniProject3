package org.d3if3162.catizen.ui.screen

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import org.d3if3162.catizen.model.Kucing
import org.d3if3162.catizen.network.KucingApi

class MainViewModel : ViewModel() {
    var data = mutableStateOf(emptyList<Kucing>())
        private set

    var status = MutableStateFlow(KucingApi.ApiStatus.LOADING)
        private set

    var errorMessage = mutableStateOf<String?>(null)
        private set

    private var idCounter = 0

    fun getNextId(): String {
        idCounter++
        return idCounter.toString()
    }


    fun retrieveData(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            status.value = KucingApi.ApiStatus.LOADING
            try {
                data.value = KucingApi.service.getKucing(userId)
                status.value = KucingApi.ApiStatus.SUCCESS
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                status.value = KucingApi.ApiStatus.FAILED
            }
        }
    }
    fun saveData(userId: String, id: String, nama: String, namaPemilik: String,
                 mine: Int, bitmap: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val result = KucingApi.service.postKucing(
                    userId,
                    id.toRequestBody("text/plain".toMediaTypeOrNull()),
                    nama.toRequestBody("text/plain".toMediaTypeOrNull()),
                    namaPemilik.toRequestBody("text/plain".toMediaTypeOrNull()),
                    mine,
                    bitmap.toMultipartBody()
                )

                if (result.status == "success")
                    retrieveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }

    fun deleteData(userId: String, id: String) {
        viewModelScope.launch ( Dispatchers.IO ) {
            try {
                val result = KucingApi.service.deleteHewan(userId, id)
                if (result.status == "success")
                    retrieveData(userId)
                else
                    throw Exception(result.message)
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
                errorMessage.value = "Error: ${e.message}"
            }
        }
    }


    private fun Bitmap.toMultipartBody(): MultipartBody.Part {
        val stream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 80, stream)
        val byteArray = stream.toByteArray()
        val requestBody = byteArray.toRequestBody(
            "image/jpg".toMediaTypeOrNull(), 0, byteArray.size)
        return MultipartBody.Part.createFormData(
            "image", "image.jpg", requestBody)
    }

    fun clearMessage() { errorMessage.value = null }
}
