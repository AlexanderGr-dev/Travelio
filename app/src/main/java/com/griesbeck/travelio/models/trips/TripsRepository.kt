package com.griesbeck.travelio.models.trips

import androidx.lifecycle.MutableLiveData


class TripsRepository(private val store: TripStore) {

    fun fetchTrips(liveData: MutableLiveData<List<Trip>>) {
        return store.fetchTripData(liveData)
    }

    fun addTrip(trip: Trip) {
        return store.create(trip)
    }

    fun updateTrip(trip: Trip) {
        return store.update(trip)
    }

    fun removeTrip(trip: Trip){
        return store.delete(trip)
    }

    fun removeSight(liveData: MutableLiveData<List<Sight>>, sight: Sight, trip: Trip) {
        return store.removeSight(liveData, sight, trip)
    }
}