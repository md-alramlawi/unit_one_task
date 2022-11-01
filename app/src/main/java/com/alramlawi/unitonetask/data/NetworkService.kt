package com.alramlawi.unitonetask.data

import com.alramlawi.unitonetask.models.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class NetworkService {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://104.248.27.63/")
        .client(OkHttpClient())
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val unitOneService = retrofit.create(UnitOneService::class.java)

    suspend fun apiData(): ApiResponse.Data? = withContext(Dispatchers.Default) {
        val result = unitOneService.getData().data
        result
    }

}

interface UnitOneService {
    @GET("/home/public")
    suspend fun getData(): ApiResponse
}