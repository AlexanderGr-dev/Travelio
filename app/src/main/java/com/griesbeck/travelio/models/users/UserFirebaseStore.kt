package com.griesbeck.travelio.models.users


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class UserFirebaseStore: UserStore {

    private val database = Firebase.database
    private val dbRef = database.getReference("users")

    override fun getActualUserById(liveData: MutableLiveData<User>) {

        dbRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val currentUserId = FirebaseAuth.getInstance().currentUser?.uid.toString()

                // Gets the current in Firebase authenticated user data
                    val actualUser =
                        dataSnapshot.child(currentUserId).child("user_data").getValue<User>()

                if(actualUser != null) {
                    liveData.postValue(actualUser)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    override fun create(user: User) {
        dbRef.child(user.id).child("user_data").setValue(user)
    }

    override fun update(user: User) {
       dbRef.child(user.id).child("user_data").setValue(user)

        val firebaseUser = FirebaseAuth.getInstance().currentUser
        val profileUpdateBuilder = UserProfileChangeRequest.Builder()
        val updates = profileUpdateBuilder.setDisplayName(user.name).setPhotoUri(user.photo.toUri()).build()

        firebaseUser?.updateProfile(updates)
        firebaseUser?.updateEmail(user.email)
    }

    override fun deleteCurrentUser(context: Context, user: User) {
        if(dbRef.child(user.id).key != null) {
            dbRef.child(user.id).removeValue()
        }

        AuthUI.getInstance().delete(context).addOnCompleteListener {
            Toast.makeText(context, "Account was deleted successfully.", Toast.LENGTH_LONG).show()
        }

    }
}