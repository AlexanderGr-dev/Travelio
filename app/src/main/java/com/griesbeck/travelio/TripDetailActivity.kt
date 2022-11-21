package com.griesbeck.travelio

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.griesbeck.travelio.databinding.ActivityTripDetailBinding
import com.griesbeck.travelio.models.Trip

class TripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTripDetailBinding
    private var trip: Trip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTripDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        if(intent.hasExtra("trip_detail")) {
            trip = intent.extras?.getParcelable("trip_detail")
            binding.tvDetailLocationContent.text = trip?.location
            binding.tvDetailDateContent.text = trip?.period
            binding.tvDetailCostsContent.text = trip?.costs
            binding.tvDetailAccomodationContent.text = trip?.accomodation
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_trip_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_previous -> {
                val mainView = Intent(this,MainActivity::class.java)
                startActivity(mainView)
            }
            R.id.item_edit -> {
                val tripDetailIntent = Intent(this, TripActivity::class.java)
                tripDetailIntent.putExtra("trip_edit",trip)
                startActivity(tripDetailIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}