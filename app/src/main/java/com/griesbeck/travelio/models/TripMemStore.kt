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
        var foundTrip: Trip? = trips.find { t -> t.id == trip.id  }
        if(foundTrip != null){
            foundTrip.location = trip.location
            foundTrip.period = trip.period
            foundTrip.costs = trip.costs
            foundTrip.accomodation = trip.accomodation
        }
    }

    override fun delete(trip: Trip) {
        if(trip in trips) {
            trips.remove(trip)
        }
    }

    companion object Factory{
        @Volatile private var instance: TripMemStore? = null

        fun getInstance() =
            instance ?: synchronized(this) {
                instance ?: TripMemStore().also { instance = it }
            }
    }

}