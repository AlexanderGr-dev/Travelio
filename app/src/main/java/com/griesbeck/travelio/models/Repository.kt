package com.griesbeck.travelio.models



class Repository(private val store: TripStore) {

    fun getTrips(): List<Trip> {
        return store.findAll()
    }

    fun addTrip(trip: Trip) {
        return store.create(trip)
    }

    fun updateTrip(trip: Trip) {
        return store.update(trip)
    }
}