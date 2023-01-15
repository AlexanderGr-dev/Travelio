package com.griesbeck.travelio.ui.trips.activities

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.api.Status

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PointOfInterest
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.griesbeck.travelio.BuildConfig
import com.griesbeck.travelio.R
import com.griesbeck.travelio.databinding.ActivityMapsBinding
import com.griesbeck.travelio.models.trips.Sight
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var sights: MutableList<Sight> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY, Locale.US) }


        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG))

        autocompleteFragment.view?.setBackgroundColor(getColor(R.color.primary_container))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val loc = place.latLng
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 15f))
                // TODO: Get info about the selected place.
                Log.i(ContentValues.TAG, "Place: ${place.name}, ${place.id}")
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.i(ContentValues.TAG, "An error occurred: $status")
            }
        })

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Enable zoom buttons.
        map.uiSettings.isZoomControlsEnabled = true

        // Start point on maps is regensburg.
        val regensburg = LatLng(49.0, 12.1)
        map.moveCamera(CameraUpdateFactory.newLatLng(regensburg))

        // Set clickListener on all points of interests to be able to add sights.
        map.setOnPoiClickListener { poi ->
            // Zoom to the point of interest
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(poi.latLng, 15f))
            addSightDialog(poi)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {

        val resultIntent = Intent()
        // Send all added sights back with resultCallback
        resultIntent.putParcelableArrayListExtra("sights", ArrayList(sights))
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
        super.onBackPressed()
    }

    private fun addSightDialog(poi: PointOfInterest) {

        // Setup dialog to ask user, if he really wants to add poi to sights list
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Add")
        builder.setMessage("Do you want to add ${poi.name} to your sights list?")
        builder.setNeutralButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setPositiveButton("Add") { dialog, _ ->
            val sight = Sight(poi.placeId)
            sights.add(sight)
            dialog.dismiss()
            Snackbar.make(findViewById(R.id.rl_maps), "${poi.name} has been added to sights.", Snackbar.LENGTH_LONG).show()
        }
        builder.show()
    }

}