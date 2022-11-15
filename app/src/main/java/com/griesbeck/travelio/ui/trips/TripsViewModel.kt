package com.griesbeck.travelio.ui.trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griesbeck.travelio.models.Trip

class TripsViewModel : ViewModel() {

    private var _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> get() = _trips
}