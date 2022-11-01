package com.alramlawi.unitonetask.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alramlawi.unitonetask.data.CityRepository
import com.alramlawi.unitonetask.models.City
import com.alramlawi.unitonetask.models.Slider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class HomeViewModel internal constructor(
    private val cityRepository: CityRepository
) : ViewModel() {

    private val _isLogin = MutableLiveData<Boolean>(false)
    val isLogin: LiveData<Boolean>
        get() = _isLogin

    private val _snackbar = MutableLiveData<String?>()
    val snackbar: LiveData<String?>
        get() = _snackbar

    private val _spinner = MutableLiveData<Boolean>(false)
    val spinner: LiveData<Boolean>
        get() = _spinner


    private val _sliders = MutableLiveData<List<Slider>>(emptyList())
    val sliders: LiveData<List<Slider>?>
        get() = _sliders

    private val _cities = MutableLiveData<List<City>>(emptyList())
    val cities: LiveData<List<City>>
        get() = _cities

    init {
        val auth = FirebaseAuth.getInstance()
        _isLogin.postValue(auth.currentUser != null)
        getData()
    }

    private fun getData() {
        viewModelScope.launch {
            try {
                _spinner.value = true
                val data = cityRepository.fetchApiData()
                _sliders.postValue(data.slider ?: emptyList())
                _cities.postValue(data.allCities ?: emptyList())
                
            } catch (error: Throwable) {
                _snackbar.value = error.message
            } finally {
                _spinner.value = false
            }
        }
    }
}