package com.griesbeck.travelio.models

import android.content.Context
import androidx.lifecycle.MutableLiveData

interface UserStore {
    //fun fetchUserData(liveData: MutableLiveData<List<User>>)
    fun getActualUserById(liveData: MutableLiveData<User>)
    fun create(user: User)
    fun update(user: User)
    fun deleteCurrentUser(context: Context, user: User)
}