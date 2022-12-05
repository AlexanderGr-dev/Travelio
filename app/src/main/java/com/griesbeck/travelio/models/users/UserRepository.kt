package com.griesbeck.travelio.models.users

import android.content.Context
import androidx.lifecycle.MutableLiveData

class UserRepository(private val store: UserStore) {

    fun getActualUserById(liveData: MutableLiveData<User>){
        return store.getActualUserById(liveData)
    }

    fun createUser(user: User){
        return store.create(user)
    }

    fun updateUser(user: User){
        return store.update(user)
    }

    fun deleteCurrentUser(context: Context, user: User){
        return store.deleteCurrentUser(context, user)
    }
}