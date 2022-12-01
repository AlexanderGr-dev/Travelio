package com.griesbeck.travelio.ui.viewmodels


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.griesbeck.travelio.models.Trip
import com.griesbeck.travelio.models.User
import com.griesbeck.travelio.models.UserFirebaseStore
import com.griesbeck.travelio.models.UserRepository

class UsersViewModel: ViewModel() {

    private val repo : UserRepository = UserRepository(UserFirebaseStore())
    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user

    init {
        getActualUser(_user)
        Log.i("i",_user.value.toString())
    }

    private fun getActualUser(liveData: MutableLiveData<User>){
        repo.getActualUserById(liveData)
        Log.i("i",_user.value.toString())
    }

    fun addUser(user: User){
        repo.createUser(user)
    }

    fun updateUser(user: User){
        repo.updateUser(user)
    }

    fun deleteCurrentUser(context: Context, user: User){
        repo.deleteCurrentUser(context, user)
    }
}