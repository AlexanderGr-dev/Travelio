package com.griesbeck.travelio.ui.trips

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griesbeck.travelio.models.Repository
import com.griesbeck.travelio.models.Sight
import com.griesbeck.travelio.models.Trip
import com.griesbeck.travelio.models.TripMemStore

class TripsViewModel: ViewModel() {

    private val repo : Repository = Repository(TripMemStore.getInstance())
    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> get() = _trips
    private val _sights = MutableLiveData<List<Sight>>()
    val sights: LiveData<List<Sight>> get() = _sights

    init{
        getTrips()
    }


    private fun getTrips(){
        _trips.value = repo.getTrips()
    }


    fun addTrip(trip: Trip){
        repo.addTrip(trip)
    }

    fun updateTrip(trip: Trip){
        repo.updateTrip(trip)
    }

    fun deleteTrip(trip: Trip){
        repo.removeTrip(trip)
    }

    fun deleteSight(sight: Sight, trip: Trip) {
        repo.removeSight(sight,trip)
        _sights.value = trip.sights
    }

    fun getSights(trip: Trip) {
        _sights.value = trip.sights
    }



}