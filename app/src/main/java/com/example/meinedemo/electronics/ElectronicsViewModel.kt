package com.example.meinedemo.electronics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.meinedemo.bands.BandInfo
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.net.HttpURLConnection

class ElectronicsViewModel : ViewModel() {
    companion object {
        private val contentType = "application/json".toMediaType()
    }

    private val _electronicsFlow: MutableStateFlow<List<Electronic>> = MutableStateFlow(emptyList())
    val electronicsFlow: StateFlow<List<Electronic>> = _electronicsFlow
    private val _currentElectronic: MutableSharedFlow<BandInfo?> = MutableSharedFlow()
    val currentElectronic: Flow<BandInfo?> = _currentElectronic
    private val retrofit = Retrofit.Builder()
        .client(OkHttpClient().newBuilder().build())
        .addConverterFactory(Json.asConverterFactory(contentType))
        .baseUrl("https://restful-api.dev/")
        .build()
    private val electronicsService = retrofit.create(ElectronicsApiService::class.java)
    fun requestElectronicsFromServer() {
        viewModelScope.launch {
            val electronics = getElectronicsFromServer()
            electronics?.let {
                _electronicsFlow.emit(electronics)
            }
        }
    }

    private suspend fun getElectronicsFromServer(): List<Electronic>? {
        return withContext(Dispatchers.IO) {
            val response = electronicsService.getElectronics()
            if (response.code() == HttpURLConnection.HTTP_OK) {
                response.body().orEmpty()
            } else {
                null
            }
        }
    }

}