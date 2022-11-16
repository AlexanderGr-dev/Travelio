package com.griesbeck.travelio.ui.trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griesbeck.travelio.models.Repository
import com.griesbeck.travelio.models.Trip
import com.griesbeck.travelio.models.TripMemStore

class TripsViewModel: ViewModel() {

    private val repo : Repository = Repository(TripMemStore.getInstance())
    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> get() = _trips

    init{
        getTrips()
    }

    private fun getTrips(){
        _trips.value = repo.getTrips()
    }

    fun addTrip(trip: Trip){
        repo.addTrip(trip)
    }



}