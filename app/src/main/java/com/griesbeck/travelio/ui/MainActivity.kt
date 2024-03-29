package com.griesbeck.travelio.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.firebase.ui.auth.AuthUI
import com.google.android.material.navigation.NavigationView
import com.griesbeck.travelio.R
import com.griesbeck.travelio.databinding.ActivityMainBinding
import com.griesbeck.travelio.ui.users.activities.SignInActivity
import com.griesbeck.travelio.ui.users.viewmodels.UsersViewModel
import com.squareup.picasso.Picasso


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val usersViewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_trips, R.id.nav_logout, R.id.nav_profile
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener ( this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Logout successfully", Toast.LENGTH_LONG).show()
                        val signInIntent = Intent(this, SignInActivity::class.java)
                        signInIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        startActivity(signInIntent)
                    } else {
                        Toast.makeText(this, "Logout failed. Try again later.", Toast.LENGTH_LONG)
                            .show()
                    }
                }
            return@setOnMenuItemClickListener true
        }


        usersViewModel.user.observe(this) { user ->
            if(user != null) {
                val header = navView.getHeaderView(0)
                val navPhoto = header.findViewById<ImageView>(R.id.iv_nav_header_photo)
                if (user.photo != "") {
                    Picasso.get()
                        .load(user.photo.toUri())
                        .into(navPhoto)
                }
                val navName = header.findViewById<TextView>(R.id.tv_nav_header_name)
                navName.text = user.name
                val navEmail = header.findViewById<TextView>(R.id.tv_nav_header_email)
                navEmail.text = user.email
            }
        }

    }


   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }
*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


}

