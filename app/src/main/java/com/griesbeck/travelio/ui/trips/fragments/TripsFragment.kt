package com.griesbeck.travelio.ui.trips.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.button.MaterialButton
import com.griesbeck.travelio.TripAdapter
import com.griesbeck.travelio.TripListener
import com.griesbeck.travelio.databinding.FragmentTripsBinding
import com.griesbeck.travelio.models.trips.Trip
import com.griesbeck.travelio.ui.trips.activities.TripActivity
import com.griesbeck.travelio.ui.trips.activities.TripDetailActivity
import com.griesbeck.travelio.ui.trips.viewmodels.TripsViewModel
import com.griesbeck.travelio.ui.viewmodels.SharedTripViewModel
import com.griesbeck.travelio.ui.viewmodels.SharedTripViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class TripsFragment : Fragment(), TripListener {

    private var _binding: FragmentTripsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //private val tripsViewModel = ViewModelProvider(this).get(TripsViewModel::class.java)
    private val currentlayoutManager = GridLayoutManager(this.context,2)
    val upcomingLayoutManager = GridLayoutManager(this.context,2)
    val pastLayoutManager = GridLayoutManager(this.context,2)
    private var trips: List<Trip>? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val tripsViewModel = ViewModelProvider(this).get(TripsViewModel::class.java)

        _binding = FragmentTripsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabAdd.setOnClickListener {
            val tripIntent = Intent(view?.context, TripActivity::class.java)
            startActivity(tripIntent)
        }


        tripsViewModel.trips.observe(viewLifecycleOwner, Observer { trips ->
            this.trips = trips
            val checkedButtonId = binding.toggleButton.checkedButtonId
            val checkedButton: MaterialButton = binding.toggleButton.findViewById(checkedButtonId)
            onToggleGroupClick(trips,checkedButton)
        })

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTripClick(trip: Trip) {
        val tripViewModel = ViewModelProvider(this, SharedTripViewModelFactory.getInstance()).get(
            SharedTripViewModel::class.java)
        val tripDetailIntent = Intent(this.context, TripDetailActivity::class.java)
        tripViewModel.setSelectedTrip(trip)
        startActivity(tripDetailIntent)
    }

    fun onToggleGroupClick(trips: List<Trip>?, checkedButton: MaterialButton) {

        val upcomingTrips = ArrayList<Trip>()
        val pastTrips = ArrayList<Trip>()
        val currentTrips = ArrayList<Trip>()
        val format = SimpleDateFormat("yyyy/MM/dd")
        val currentFormat = SimpleDateFormat("yyyy-MM-dd")
        val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        if (trips != null) {
            for (trip in trips){
                try {
                    val startDate = format.parse(trip.startDate)
                    val endDate = format.parse(trip.endDate)
                    val currentDateTime = LocalDateTime.now().format(pattern)
                    val currentDate = currentFormat.parse(currentDateTime)
                    if(startDate > currentDate){
                        upcomingTrips.add(trip)
                    }else if(endDate < currentDate){
                        pastTrips.add(trip)
                    }else{
                        currentTrips.add(trip)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
            upcomingTrips.sortBy { it.startDate }
            currentTrips.sortBy { it.startDate }
            pastTrips.sortByDescending { it.startDate }
        }
        if(binding.btnCurrent == checkedButton){
            binding.rvTrips.layoutManager = GridLayoutManager(this.context,2)
            binding.rvTrips.adapter = TripAdapter(currentTrips,this)
        }else if(binding.btnUpcoming == checkedButton){
            binding.rvTrips.layoutManager = GridLayoutManager(this.context,2)
            binding.rvTrips.adapter = TripAdapter(upcomingTrips,this)
        }else if(binding.btnPast == checkedButton){
            binding.rvTrips.layoutManager = GridLayoutManager(this.context,2)
            binding.rvTrips.adapter = TripAdapter(pastTrips,this)
        }

        binding.btnUpcoming.setOnClickListener{
            binding.rvTrips.layoutManager = upcomingLayoutManager
            binding.rvTrips.adapter = TripAdapter(upcomingTrips,this)
        }

        binding.btnPast.setOnClickListener{
            binding.rvTrips.layoutManager = pastLayoutManager
            binding.rvTrips.adapter = TripAdapter(pastTrips,this)
        }

        binding.btnCurrent.setOnClickListener{
            binding.rvTrips.layoutManager = currentlayoutManager
            binding.rvTrips.adapter = TripAdapter(currentTrips,this)
        }

    }

}