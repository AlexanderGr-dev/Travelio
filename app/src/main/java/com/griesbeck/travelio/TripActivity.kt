package com.griesbeck.travelio


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.datepicker.MaterialDatePicker
import com.griesbeck.travelio.databinding.ActivityTripBinding
import androidx.core.util.Pair
import java.text.SimpleDateFormat

class TripActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTripBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTripBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarTrip)
        supportActionBar?.setDisplayShowTitleEnabled(false)


        binding.inputDate.setOnClickListener {
            setDate()
        }


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
            binding.inputDate.setText(period)
        }
    }
}