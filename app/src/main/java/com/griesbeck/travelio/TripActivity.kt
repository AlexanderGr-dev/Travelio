package com.griesbeck.travelio


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.datepicker.MaterialDatePicker
import com.griesbeck.travelio.databinding.ActivityTripBinding
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.griesbeck.travelio.models.Sight
import com.griesbeck.travelio.models.Trip
import com.griesbeck.travelio.ui.trips.TripsViewModel
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat

class TripActivity : AppCompatActivity(), SightDeleteListener {

    private lateinit var binding: ActivityTripBinding
    private lateinit var imageIntentLauncher : ActivityResultLauncher<Intent>
    private lateinit var mapIntentLauncher : ActivityResultLauncher<Intent>
    private val layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
    var trip = Trip()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var edit: Boolean = false
        binding = ActivityTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTrip)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val tripsViewModel =
            ViewModelProvider(this).get(TripsViewModel::class.java)

        if(intent.hasExtra("trip_edit")){
            edit = true
            binding.btnAdd.text = getString(R.string.btn_save_trip)
            if(trip.image != Uri.EMPTY) {
                binding.btnChooseImage.text = getString(R.string.btn_change_image)
            }
            trip = intent.extras?.getParcelable("trip_edit")!!
            bindTripEditData(trip)
        }


        binding.etDate.setOnClickListener {
            setDate()
        }

        binding.btnAdd.setOnClickListener {
            addTripData()
            if(!edit) {
                tripsViewModel.addTrip(trip)
                finish()
            }else{
                tripsViewModel.updateTrip(trip)
                val tripDetailIntent = Intent(this, TripDetailActivity::class.java)
                tripDetailIntent.putExtra("trip_detail",trip)
                startActivity(tripDetailIntent)
            }
        }

        binding.btnChooseImage.setOnClickListener {
          showImagePicker(imageIntentLauncher,this)
        }

        registerImagePickerCallback()

        binding.btnAddSights.setOnClickListener {
            val mapsIntent = Intent(this,MapsActivity::class.java)
            mapIntentLauncher.launch(mapsIntent)
        }

        registerMapCallback()


    }

 /*   override fun onResume() {
        super.onResume()

        val tripsViewModel = ViewModelProvider(this).get(TripsViewModel::class.java)

        tripsViewModel.trips.observe(viewLifecycleOwner, Observer { trips ->
            binding.rvSights.layoutManager = layoutManager
            binding.rvSights.adapter = SightsAdapter(trips)
        })
    }*/

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
        val dateRangePicker =
            MaterialDatePicker.Builder.dateRangePicker()
                .setTitleText("Select range")
                .setSelection(
                    Pair(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                    )
                )
                .build()

        dateRangePicker.show(supportFragmentManager, "material_date_picker")

        dateRangePicker.addOnPositiveButtonClickListener {

            val formatter = SimpleDateFormat("dd/MM/yyyy")
            val startdate: String = formatter.format(it.first)
            val endDate: String = formatter.format(it.second)
            val period = "${startdate}   -   ${endDate}"
            binding.etDate.setText(period)
        }
    }

    private fun addTripData(){
        trip.location = binding.etLocation.text.toString()
        trip.period = binding.etDate.text.toString()
        trip.accomodation = binding.etAccomodation.text.toString()
        trip.costs = binding.etCosts.text.toString()
    }

    private fun bindTripEditData(trip: Trip){
        Picasso.get().load(trip.image).into(binding.ivTripImageChoose)
        binding.etLocation.setText(trip.location)
        binding.etDate.setText(trip.period)
        binding.etAccomodation.setText(trip.accomodation)
        binding.etCosts.setText(trip.costs)
        binding.rvSights.layoutManager = layoutManager
        binding.rvSights.adapter = SightsAdapter(trip.sights,this)
    }

    private fun registerImagePickerCallback() {
        imageIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {

                            val image = result.data!!.data!!
                            contentResolver.takePersistableUriPermission(
                                image,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                            trip.image = image

                            Picasso.get()
                                .load(trip.image)
                                .into(binding.ivTripImageChoose)
                            binding.btnChooseImage.text = getString(R.string.btn_change_image)
                        }
                    }
                    RESULT_CANCELED -> {
                        Toast.makeText(this, "Task cancelled", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        Toast.makeText(this, "Some error occured", Toast.LENGTH_SHORT).show()
                    }
                }
            }
    }

    private fun registerMapCallback() {
        mapIntentLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult())
            { result ->
                when (result.resultCode) {
                    RESULT_OK -> {
                        if (result.data != null) {

                            if(trip.sights.isEmpty()) {
                                trip.sights =
                                    result.data!!.extras?.getParcelableArrayList("sights")!!
                            }else{
                                val sights: ArrayList<Sight> = result.data!!.extras?.getParcelableArrayList("sights")!!
                                val mSights = trip.sights.toMutableList()
                                sights.forEach { sight ->
                                    mSights.add(sight)
                                }
                                trip.sights = mSights
                            }
                            val tripsViewModel =
                                ViewModelProvider(this).get(TripsViewModel::class.java)

                            tripsViewModel.getSights(trip)

                            tripsViewModel.sights.observe(this) { sights ->
                                binding.rvSights.layoutManager = layoutManager
                                binding.rvSights.adapter = SightsAdapter(sights, this)
                            }


                        } // end of if
                    }
                    RESULT_CANCELED -> { } else -> { }
                }
            }
    }

    override fun onDeleteClick(sight: Sight) {
        deleteSightDialog(sight)
    }

    private fun deleteSightDialog(sight: Sight) {
        val builder = MaterialAlertDialogBuilder(this)
        builder.setTitle("Delete")
        builder.setMessage("Do you really want to delete this sight?")
        builder.setNeutralButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }
        builder.setPositiveButton("Delete") { dialog, which ->
            val tripsViewModel =
                ViewModelProvider(this).get(TripsViewModel::class.java)

            tripsViewModel.deleteSight(sight,trip)
            dialog.dismiss()
        }
        builder.show()
    }
}