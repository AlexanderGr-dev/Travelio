package com.griesbeck.travelio.ui.trips.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.griesbeck.travelio.R
import com.griesbeck.travelio.databinding.ActivityTripDetailBinding
import com.griesbeck.travelio.getResizedBitmap
import com.griesbeck.travelio.models.trips.Trip
import com.griesbeck.travelio.stringToBitMap
import com.griesbeck.travelio.ui.MainActivity
import com.griesbeck.travelio.ui.trips.adapters.SightsDetailAdapter
import com.griesbeck.travelio.ui.trips.viewmodels.TripsViewModel
import com.griesbeck.travelio.ui.viewmodels.SharedTripViewModel
import com.griesbeck.travelio.ui.viewmodels.SharedTripViewModelFactory
import com.griesbeck.travelio.ui.weather.adapters.WeatherAdapter
import com.griesbeck.travelio.ui.weather.viewmodels.WeatherViewModel

class TripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTripDetailBinding
    private val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    var trip = Trip()
    private val tripsViewModel: TripsViewModel by viewModels()
    private val weatherViewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripDetailBinding.inflate(layoutInflater)

        val tripViewModel: SharedTripViewModel =
            ViewModelProvider(this, SharedTripViewModelFactory.getInstance())[SharedTripViewModel::class.java]

        val extras = intent.extras
        if(extras!=null){
            val transition = extras.getString("transition")
            binding.tripImage.transitionName = transition
        }

        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTripDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        tripViewModel.selectedTrip.observe(this) { trip ->
            bindTripDetailData(trip)
            weatherViewModel.getWeather(trip.locLat,trip.locLon)
        }

        weatherViewModel.weather.observe(this) { weather ->
            val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            binding.rvWeather.layoutManager = layoutManager
            binding.rvWeather.adapter = WeatherAdapter(weather)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_trip_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_previous -> {
                val mainView = Intent(this, MainActivity::class.java)
                startActivity(mainView)
            }
            R.id.item_edit -> {
                val tripDetailIntent = Intent(this, TripActivity::class.java)
                tripDetailIntent.putExtra("trip_edit",true)
                startActivity(tripDetailIntent)
            }
            R.id.item_delete -> {
                deleteDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindTripDetailData(trip: Trip?){
        if (trip != null) {
            this.trip.image = trip.image
            this.trip.id = trip.id
            this.trip.location = trip.location
            this.trip.startDate = trip.startDate
            this.trip.endDate = trip.endDate
            this.trip.accommodation = trip.accommodation
            this.trip.locLon = trip.locLon
            this.trip.locLat = trip.locLat
        }
        val ivBitmap = binding.tripImage.drawable.toBitmap()
        val bitmapWidth = ivBitmap.width
        val bitmapHeight = ivBitmap.height
        val bitmap = trip?.image?.let { stringToBitMap(it) }
        val resizedBitmap = bitmap?.let { getResizedBitmap(it,bitmapWidth,bitmapHeight) }
        if(resizedBitmap != null) {
            binding.tripImage.setImageBitmap(resizedBitmap)
        }else{
            binding.tripImage.setImageResource(R.drawable.placeholder)
        }
        val period = "${trip?.startDate} - ${trip?.endDate}"
        val costs = trip?.costs + " €"
        binding.tvDetailLocationContent.text = trip?.location
        binding.tvDetailDateContent.text = period
        binding.tvDetailCostsContent.text = costs
        binding.tvDetailAccomodationContent.text = trip?.accommodation
        binding.rvSightsDetail.layoutManager = layoutManager
        binding.rvSightsDetail.adapter = SightsDetailAdapter(trip!!.sights)
    }

    private fun deleteDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Delete Trip")
        builder.setMessage("Do you really want to delete this trip?")
        builder.setNeutralButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.setPositiveButton("Delete") { dialog, which ->
            tripsViewModel.deleteTrip(this.trip)
            dialog.dismiss()
            val mainView = Intent(this, MainActivity::class.java)
            startActivity(mainView)
        }
        builder.show()
    }
}