package com.griesbeck.travelio.ui.trips.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griesbeck.travelio.models.trips.Sight
import com.griesbeck.travelio.models.trips.Trip
import com.griesbeck.travelio.models.trips.TripFirebaseStore
import com.griesbeck.travelio.models.trips.TripsRepository

class TripsViewModel: ViewModel() {

    private val repo : TripsRepository = TripsRepository(TripFirebaseStore())

    private val _trips = MutableLiveData<List<Trip>>()
    val trips: LiveData<List<Trip>> get() = _trips

    private val _sights = MutableLiveData<List<Sight>>()
    val sights: LiveData<List<Sight>> get() = _sights


    init{
        fetchTrips(_trips)
    }


    private fun fetchTrips(liveData: MutableLiveData<List<Trip>>){
        repo.fetchTrips(liveData)
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
        repo.removeSight(_sights,sight,trip)
    }

    fun getSights(trip: Trip) {
        _sights.value = trip.sights
    }


}