package com.alramlawi.unitonetask.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.alramlawi.unitonetask.data.CityRepository
import com.alramlawi.unitonetask.data.NetworkService

class PlantsViewModelFactory(
    private val repository: CityRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>) = HomeViewModel(repository) as T
}

object DefaultViewModelProvider : ViewModelFactoryProvider {

    private fun networkService() = NetworkService()

    private fun getPlantRepository(): CityRepository {
        return CityRepository.getInstance(
            networkService()
        )
    }

    override fun provideHomeViewModelFactory(): PlantsViewModelFactory {
        val repository = getPlantRepository()
        return PlantsViewModelFactory(repository)
    }
}
interface ViewModelFactoryProvider {
    fun provideHomeViewModelFactory(): PlantsViewModelFactory
}
