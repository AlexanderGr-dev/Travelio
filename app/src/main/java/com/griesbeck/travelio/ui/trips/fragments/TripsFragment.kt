package com.griesbeck.travelio.ui.trips.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.button.MaterialButton
import com.griesbeck.travelio.ui.trips.adapters.TripAdapter
import com.griesbeck.travelio.ui.trips.adapters.TripListener
import com.griesbeck.travelio.databinding.FragmentTripsBinding
import com.griesbeck.travelio.models.trips.Trip
import com.griesbeck.travelio.ui.trips.activities.TripActivity
import com.griesbeck.travelio.ui.trips.activities.TripDetailActivity
import com.griesbeck.travelio.ui.trips.viewmodels.TripsViewModel
import com.griesbeck.travelio.ui.trips.viewmodels.SharedTripViewModel
import com.griesbeck.travelio.ui.trips.viewmodels.SharedTripViewModelFactory
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class TripsFragment : Fragment(), TripListener {

    private var _binding: FragmentTripsBinding? = null
    private val binding get() = _binding!!
    private val currentLayoutManager = GridLayoutManager(this.context,2)
    private val upcomingLayoutManager = GridLayoutManager(this.context,2)
    private val pastLayoutManager = GridLayoutManager(this.context,2)
    private var trips: List<Trip>? = null
    private val tripsViewModel: TripsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTripsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabAdd.setOnClickListener {
            val tripIntent = Intent(view?.context, TripActivity::class.java)
            startActivity(tripIntent)
        }

        tripsViewModel.trips.observe(viewLifecycleOwner) { trips ->
            this.trips = trips
            val checkedButtonId = binding.toggleButton.checkedButtonId
            val checkedButton: MaterialButton = binding.toggleButton.findViewById(checkedButtonId)
            onToggleGroupClick(trips, checkedButton)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTripClick(trip: Trip, pair: Pair<View, String>) {
        val tripDetailIntent = Intent(this.context, TripDetailActivity::class.java)
        val options =
            ActivityOptionsCompat.makeSceneTransitionAnimation(
                requireActivity(),
                pair.first,
                pair.second
            )

        val tripViewModel: SharedTripViewModel =
            ViewModelProvider(this, SharedTripViewModelFactory.getInstance())[SharedTripViewModel::class.java]
        tripViewModel.setSelectedTrip(trip)
        tripDetailIntent.putExtra("transition",pair.second)
        startActivity(tripDetailIntent, options.toBundle())
    }

    private fun onToggleGroupClick(trips: List<Trip>?, checkedButton: MaterialButton) {

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
                    if (startDate != null && endDate != null) {
                        if(startDate > currentDate){
                            upcomingTrips.add(trip)
                        }else if(endDate < currentDate){
                            pastTrips.add(trip)
                        }else{
                            currentTrips.add(trip)
                        }
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
            binding.rvTrips.layoutManager = currentLayoutManager
            binding.rvTrips.adapter = TripAdapter(currentTrips,this)
        }

    }

}