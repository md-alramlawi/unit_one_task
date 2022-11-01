package com.alramlawi.unitonetask.models


data class ApiResponse(
    val `data`: Data?,
    val errors: List<Any>?,
    val message: String?,
    val status: Boolean?
) {
    data class Data(
        val allCities: List<City>?,
        val slider: List<Slider>?
    )
}