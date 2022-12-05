package com.griesbeck.travelio.models.trips

import androidx.lifecycle.MutableLiveData

interface TripStore {
    fun fetchTripData(liveData: MutableLiveData<List<Trip>>)
    fun create(trip: Trip)
    fun update(trip: Trip)
    fun delete(trip: Trip)
    fun removeSight(liveData: MutableLiveData<List<Sight>>, sight: Sight, trip: Trip)
}