package com.griesbeck.travelio.ui.trips.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.griesbeck.travelio.models.trips.Trip

class SharedTripViewModelFactory : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        with(modelClass){
            when {
                isAssignableFrom(SharedTripViewModel::class.java) -> SharedTripViewModel.getInstance()
                else -> throw IllegalArgumentException("Unknown viewModel class $modelClass")
            }
        } as T


    companion object {
        private var instance : SharedTripViewModelFactory? = null
        fun getInstance() =
            instance ?: synchronized(SharedTripViewModelFactory::class.java){
                instance ?: SharedTripViewModelFactory().also { instance = it }
            }
    }
}

class SharedTripViewModel : ViewModel() {

    private val _selectedTrip = MutableLiveData<Trip>()
    val selectedTrip: LiveData<Trip> get() = _selectedTrip

    fun setSelectedTrip(trip: Trip){
        _selectedTrip.value = trip
    }

    companion object {
        private var instance : SharedTripViewModel? = null
        fun getInstance() =
            instance ?: synchronized(SharedTripViewModel::class.java){
                instance ?: SharedTripViewModel().also { instance = it }
            }
    }
}