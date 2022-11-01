package com.alramlawi.unitonetask.data

import com.alramlawi.unitonetask.models.ApiResponse

class CityRepository private constructor(
    private val plantService: NetworkService,
) {

    suspend fun fetchApiData(): ApiResponse.Data {
        val result =  plantService.apiData() ?: ApiResponse.Data(emptyList(), emptyList())
        return result
    }


    companion object {

        @Volatile
        private var instance: CityRepository? = null

        fun getInstance(service: NetworkService) =
            instance ?: synchronized(this) {
                instance ?: CityRepository(service).also { instance = it }
            }
    }
}
