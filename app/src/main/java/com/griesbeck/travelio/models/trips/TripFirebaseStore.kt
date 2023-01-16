package com.griesbeck.travelio.models.trips

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class TripFirebaseStore: TripStore {

    private val database = Firebase.database
    private val dbRef = database.getReference(
        "users/${FirebaseAuth.getInstance().currentUser?.uid.
        toString()}/trips")


    override fun fetchTripData(liveData: MutableLiveData<List<Trip>>) {
        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val trips: MutableList<Trip> = mutableListOf()

                //Gets data from every saved trip in firebase und the dbRef and adds them to trips
                dataSnapshot.children.forEach { tripSnapshot ->
                    val trip = tripSnapshot.getValue<Trip>()
                    trip?.id = tripSnapshot.key.toString()
                    trips.add(trip!!)
                }
                liveData.postValue(trips)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Handle error")
            }

        })

    }


    override fun create(trip: Trip) {
        trip.id = dbRef.push().key.toString()

        dbRef.child(trip.id).setValue(trip)
    }

    override fun update(trip: Trip) {
        if(dbRef.child(trip.id).key != null) {
            dbRef.child(trip.id).setValue(trip)
        }
    }

    override fun delete(trip: Trip) {
        if(dbRef.child(trip.id).key != null) {
            dbRef.child(trip.id).removeValue()
        }
    }

    override fun removeSight(liveData: MutableLiveData<List<Sight>>, sight: Sight, trip: Trip) {
        if(sight in trip.sights){
            val temp = trip.sights.toMutableList()
            temp.remove(sight)
            trip.sights = temp
            liveData.postValue(trip.sights)
        }
    }


}