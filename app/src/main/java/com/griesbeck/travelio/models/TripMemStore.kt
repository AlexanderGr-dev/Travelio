package com.griesbeck.travelio.models

import java.util.*

fun generateRandomId(): Long {
    return Random().nextLong()
}


class TripMemStore(): TripStore {

    var trips = mutableListOf<Trip>()

    init {
        //for testing
        trips.add(Trip(34534,"Regensburg", "24.11 - 12.12.2022", "Hotel Donaublick"))
        trips.add(Trip(34535,"Regensburg", "24.11 - 12.12.2022", "Hotel Donaublick"))
        trips.add(Trip(3344,"München", "24.11 - 12.12.2022", "Hotel Donaublick"))
    }


    override fun findAll(): List<Trip> {
        return trips
    }

    override fun create(trip: Trip) {
        trip.id = generateRandomId()
        trips.add(trip)
    }

    override fun update(trip: Trip) {
        TODO("Not yet implemented")
    }

    override fun delete(trip: Trip) {
        TODO("Not yet implemented")
    }

    companion object Factory{
        @Volatile private var instance: TripMemStore? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: TripMemStore().also { instance = it }
            }
    }

}