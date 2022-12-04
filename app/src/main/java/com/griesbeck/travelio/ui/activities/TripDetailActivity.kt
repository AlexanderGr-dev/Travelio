package com.griesbeck.travelio.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.core.graphics.drawable.toBitmap
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.griesbeck.travelio.R
import com.griesbeck.travelio.ui.adapters.SightsDetailAdapter
import com.griesbeck.travelio.databinding.ActivityTripDetailBinding
import com.griesbeck.travelio.getResizedBitmap
import com.griesbeck.travelio.models.Trip
import com.griesbeck.travelio.stringToBitMap
import com.griesbeck.travelio.ui.adapters.WeatherAdapter
import com.griesbeck.travelio.ui.viewmodels.TripsViewModel
import com.griesbeck.travelio.ui.viewmodels.WeatherViewModel
import com.squareup.picasso.Picasso

class TripDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTripDetailBinding
    private val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    private var trip: Trip? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTripDetail)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val weatherViewModel =
            ViewModelProvider(this).get(WeatherViewModel::class.java)

        if(intent.hasExtra("trip_detail")) {
            trip = intent.extras?.getParcelable("trip_detail")
            bindTripDetailData(trip)
            weatherViewModel.getWeather(trip!!.locLat,trip!!.locLon)
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
                tripDetailIntent.putExtra("trip_edit",trip)
                startActivity(tripDetailIntent)
            }
            R.id.item_delete -> {
                deleteDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun bindTripDetailData(trip: Trip?){
        val ivBitmap = binding.tripImage.drawable.toBitmap()
        val bitmapWidth = ivBitmap.width
        val bitmapHeight = ivBitmap.height
        val bitmap = trip?.image?.let { stringToBitMap(it) }
        val resizedBitmap = bitmap?.let { getResizedBitmap(it,bitmapWidth,bitmapHeight) }
        binding.tripImage.setImageBitmap(resizedBitmap)
        binding.tvDetailLocationContent.text = trip?.location
        binding.tvDetailDateContent.text = trip?.period
        binding.tvDetailCostsContent.text = trip?.costs
        binding.tvDetailAccomodationContent.text = trip?.accomodation
        binding.rvSightsDetail.layoutManager = layoutManager
        binding.rvSightsDetail.adapter = SightsDetailAdapter(trip!!.sights)
    }

    private fun deleteDialog() {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Delete")
        builder.setMessage("Do you really want to delete this trip?")
        builder.setNeutralButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.setPositiveButton("Delete") { dialog, which ->
            val tripsViewModel =
                ViewModelProvider(this).get(TripsViewModel::class.java)
            trip?.let { tripsViewModel.deleteTrip(it) }
            dialog.dismiss()
            val mainView = Intent(this, MainActivity::class.java)
            startActivity(mainView)
        }
        builder.show()
    }
}