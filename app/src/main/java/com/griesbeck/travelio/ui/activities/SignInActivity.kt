package com.griesbeck.travelio.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.griesbeck.travelio.R
import com.griesbeck.travelio.models.User
import com.griesbeck.travelio.ui.viewmodels.TripsViewModel
import com.griesbeck.travelio.ui.viewmodels.UsersViewModel

class SignInActivity : AppCompatActivity() {

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract()
    ) { res ->
        this.onSignInResult(res)
    }

    private val user: User = User()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createSignInIntent()
    }

    private fun createSignInIntent() {
        // [START auth_fui_create_intent]
        // Choose authentication providers
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(),
            AuthUI.IdpConfig.GoogleBuilder().build()
        )

        // Create and launch sign-in intent
        val signInIntent = AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .setLogo(R.drawable.travelio_logo)
            .setTheme(R.style.LoginTheme)
            .build()
        signInLauncher.launch(signInIntent)
        // [END auth_fui_create_intent]
    }

    // [START auth_fui_result]
    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {

        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            if(result.idpResponse?.isNewUser == true){
                val firebaseUser: FirebaseUser? = FirebaseAuth.getInstance().currentUser
                    user.id = firebaseUser?.uid.toString()
                    user.email = firebaseUser?.email.toString()
                    user.name = firebaseUser?.displayName.toString()
                    if(firebaseUser?.photoUrl.toString().isNotEmpty()){
                        user.photo = firebaseUser?.photoUrl.toString()
                    }
                val usersViewModel = ViewModelProvider(this).get(UsersViewModel::class.java)
                usersViewModel.addUser(user)
                }
            val mainIntent = Intent(this, MainActivity::class.java)
            startActivity(mainIntent)
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            // response.getError().getErrorCode() and handle the error.
            // ...
        }
    }


    private fun delete() {
        // [START auth_fui_delete]
        AuthUI.getInstance()
            .delete(this)
            .addOnCompleteListener {
                // ...
            }
        // [END auth_fui_delete]
    }
}