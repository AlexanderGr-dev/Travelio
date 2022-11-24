package com.griesbeck.travelio.models

interface TripStore {
    fun findAll(): List<Trip>
    fun create(trip: Trip)
    fun update(trip: Trip)
    fun delete(trip: Trip)
    fun removeSight(sight: Sight, trip: Trip)
}