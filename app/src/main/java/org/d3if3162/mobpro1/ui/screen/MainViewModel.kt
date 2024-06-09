package org.d3if3162.mobpro1.ui.screen

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.d3if3162.mobpro1.model.Kucing
import org.d3if3162.mobpro1.network.KucingApi

class MainViewModel : ViewModel() {
    var data = mutableStateOf(emptyList<Kucing>())
        private set

    init {
        retrieveData()
    }

    private fun retrieveData() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                data.value = KucingApi.service.getKucing()
            } catch (e: Exception) {
                Log.d("MainViewModel", "Failure: ${e.message}")
            }
        }
    }
}
