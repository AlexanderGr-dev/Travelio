package com.griesbeck.travelio.ui.trips.activities


import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.util.Pair
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.griesbeck.travelio.BuildConfig
import com.griesbeck.travelio.R
import com.griesbeck.travelio.databinding.ActivityTripBinding
import com.griesbeck.travelio.helpers.bitMapToString
import com.griesbeck.travelio.helpers.stringToBitMap
import com.griesbeck.travelio.models.trips.Sight
import com.griesbeck.travelio.models.trips.Trip
import com.griesbeck.travelio.ui.trips.adapters.SightDeleteListener
import com.griesbeck.travelio.ui.trips.adapters.SightsAdapter
import com.griesbeck.travelio.ui.trips.viewmodels.SharedTripViewModel
import com.griesbeck.travelio.ui.trips.viewmodels.SharedTripViewModelFactory
import com.griesbeck.travelio.ui.trips.viewmodels.TripsViewModel
import java.math.RoundingMode
import java.text.SimpleDateFormat
import java.util.*

class TripActivity : AppCompatActivity(), SightDeleteListener {

    private lateinit var binding: ActivityTripBinding
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private val layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
    var trip = Trip()
    private val tripsViewModel: TripsViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit = false
        binding = ActivityTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTrip)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val tripViewModel: SharedTripViewModel =
            ViewModelProvider(this, SharedTripViewModelFactory.getInstance())[SharedTripViewModel::class.java]

        tripsViewModel.sights.observe(this) { sights ->
            binding.rvSights.layoutManager = layoutManager
            binding.rvSights.adapter = SightsAdapter(sights, this)
        }

        if(intent.hasExtra("trip_edit")){
            edit = true
            binding.btnAddOrSave.text = getString(R.string.btn_save_trip)
            tripViewModel.selectedTrip.observe(this) { trip ->
                bindTripEditData(trip)
            }
        }

        initializePlacesAutocompleteFragment()


        binding.etDate.setOnClickListener {
            setDate()
        }

        binding.btnAddOrSave.setOnClickListener {
            if(isUserInputCorrect()) {
                addTripData()
            }else{
                return@setOnClickListener
            }
            if(!edit) {
                tripsViewModel.addTrip(trip)
                finish()
            }else{
                tripsViewModel.updateTrip(trip)
                tripViewModel.setSelectedTrip(trip)
                val tripDetailIntent = Intent(this, TripDetailActivity::class.java)
                startActivity(tripDetailIntent)
            }
        }


        binding.btnAddSights.setOnClickListener {
            val mapsIntent = Intent(this, MapsActivity::class.java)
            mapIntentLauncher.launch(mapsIntent)
        }

        registerMapCallback()


    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_trip_add, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_previous -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDate(){

        // Get current date and date in one week for default dateRange
        val calendar = Calendar.getInstance()
        val today = calendar.timeInMillis
        calendar.add(Calendar.DATE, 7)
        val oneWeekFromToday = calendar.timeInMillis

        //Initialize datePicker with standard range
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select range")
                .setSelection(
                    Pair(
                        today,
                        oneWeekFromToday
                    )
                )
                .build()

        dateRangePicker.show(supportFragmentManager, "material_date_picker")

        dateRangePicker.addOnPositiveButtonClickListener {

            // Get start and end date of selected period in yyyy/MM/dd format
            val formatter = SimpleDateFormat("yyyy/MM/dd")
            val startDate: String = formatter.format(it.first)
            val endDate: String = formatter.format(it.second)
            trip.startDate = startDate
            trip.endDate = endDate
            val period = "$startDate - $endDate"
            binding.etDate.setText(period)
        }
    }

    private fun addTripData(){
        trip.location = binding.etLocation.text.toString()
        trip.accommodation = binding.etAccommodation.text.toString()
        trip.costs = binding.etCosts.text.toString()
    }

    private fun bindTripEditData(trip: Trip){
        this.trip.id = trip.id
        this.trip.image = trip.image
        this.trip.location = trip.location
        this.trip.startDate = trip.startDate
        this.trip.endDate = trip.endDate
        this.trip.accommodation = trip.accommodation
        this.trip.locLon = trip.locLon
        this.trip.locLat = trip.locLat
        this.trip.sights = trip.sights
        val period = "${trip.startDate} - ${trip.endDate}"

        if(trip.image.isNotEmpty()) {
            binding.ivTripImageChoose.setImageBitmap(stringToBitMap(trip.image))
        }else{
            binding.ivTripImageChoose.setImageResource(R.drawable.placeholder)
        }
        binding.etLocation.setText(trip.location)
        binding.etDate.setText(period)
        binding.etAccommodation.setText(trip.accommodation)
        binding.etCosts.setText(trip.costs)
        binding.rvSights.layoutManager = layoutManager
        binding.rvSights.adapter = SightsAdapter(trip.sights, this)
    }

    private fun isUserInputCorrect(): Boolean{
        var correctInput = true

        if(binding.etLocation.text!!.isEmpty()){
            binding.etLocation.error = "Trip name must not be empty."
            correctInput = false
        }
        if(binding.etDate.text!!.isEmpty()){
            binding.etDate.error = "Date must not be empty."
            correctInput = false
        }
        if(binding.etAccommodation.text!!.isEmpty()){
            binding.etAccommodation.error = "Accommodation name must not be empty."
            correctInput = false
        }
        if(binding.etCosts.text!!.isEmpty()){
            binding.etCosts.error = "Costs must not be empty."
            correctInput = false
        }

        return correctInput
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {

                            // Add all sights form mapCallback to trip
                            if(trip.sights.isEmpty()) {
                                trip.sights =
                                    result.data!!.extras?.getParcelableArrayList("sights")!!
                            }else{
                                // If trip sights are not empty
                                val sights: ArrayList<Sight> = result.data!!.extras?.getParcelableArrayList("sights")!!
                                val mSights = trip.sights.toMutableList()
                                sights.forEach { sight ->
                                    mSights.add(sight)
                                }
                                trip.sights = mSights
                            }

                            tripsViewModel.getSights(trip)

                        }
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    override fun onDeleteClick(sight: Sight) {
        deleteSightDialog(sight)
    }

    private fun deleteSightDialog(sight: Sight) {

        // Setup dialog to ask user, if he really wants to delete the sight
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Delete Sight")
        builder.setMessage("Do you really want to delete this sight?")
        builder.setNeutralButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        builder.setPositiveButton("Delete") { dialog, _ ->
            tripsViewModel.deleteSight(sight,trip)
            dialog.dismiss()
        }
        builder.show()
    }

    private fun initializePlacesAutocompleteFragment(){
        if (!Places.isInitialized()) {
            Places.initialize(this, BuildConfig.MAPS_API_KEY, Locale.US)
        }

        // Initialize the AutocompleteSupportFragment.
        val autocompleteFragment =
            supportFragmentManager.findFragmentById(R.id.places_autocomplete_fragment)
                    as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.PHOTO_METADATAS, Place.Field.NAME, Place.Field.LAT_LNG))

        autocompleteFragment.view?.setBackgroundColor(getColor(R.color.inputbox_bg))

        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {
                val placesClient: PlacesClient = Places.createClient(this@TripActivity)
                val loc = place.latLng
                if (loc != null) {
                    trip.locLat = loc.latitude.toBigDecimal().setScale(4,RoundingMode.HALF_DOWN).toDouble()
                    trip.locLon = loc.longitude.toBigDecimal().setScale(4,RoundingMode.HALF_DOWN).toDouble()
                }
                trip.location = place.name as String

                val metaData = place.photoMetadatas
                if (metaData == null || metaData.isEmpty()) {
                    return
                }
                val photoMetadata = metaData.first()

                // Create a FetchPhotoRequest.
                val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                    .setMaxWidth(500)
                    .setMaxHeight(300)
                    .build()
                placesClient.fetchPhoto(photoRequest)
                    .addOnSuccessListener { fetchPhotoResponse: FetchPhotoResponse ->
                        val bitmap = fetchPhotoResponse.bitmap
                        binding.ivTripImageChoose.setImageBitmap(bitmap)
                        binding.etLocation.setText(place.name)
                        trip.image = bitMapToString(bitmap)
                    }.addOnFailureListener { exception: Exception ->
                        if (exception is ApiException) {
                            val statusCode = exception.statusCode
                            TODO("Handle error with given status code.")
                        }
                    }

            }

            override fun onError(status: Status) {
                Log.i(ContentValues.TAG, "An error occurred: $status")
            }
        })
    }
}