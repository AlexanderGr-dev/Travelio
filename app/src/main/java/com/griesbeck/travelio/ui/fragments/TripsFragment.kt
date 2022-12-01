package com.griesbeck.travelio.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.griesbeck.travelio.TripAdapter
import com.griesbeck.travelio.ui.activities.TripDetailActivity
import com.griesbeck.travelio.TripListener
import com.griesbeck.travelio.databinding.FragmentTripsBinding
import com.griesbeck.travelio.models.Trip
import com.griesbeck.travelio.ui.activities.TripActivity
import com.griesbeck.travelio.ui.viewmodels.TripsViewModel

class TripsFragment : Fragment(), TripListener {

    private var _binding: FragmentTripsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    //private val tripsViewModel = ViewModelProvider(this).get(TripsViewModel::class.java)
    private val layoutManager = GridLayoutManager(this.context,2)

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
            binding.rvTrips.layoutManager = layoutManager
            binding.rvTrips.adapter = TripAdapter(trips,this)
        })

        return root
    }

    override fun onResume() {
        super.onResume()

        val tripsViewModel = ViewModelProvider(this).get(TripsViewModel::class.java)

        tripsViewModel.trips.observe(viewLifecycleOwner, Observer { trips ->
            binding.rvTrips.layoutManager = GridLayoutManager(this.context,2)
            binding.rvTrips.adapter = TripAdapter(trips,this)
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onTripClick(trip: Trip) {
        val tripDetailIntent = Intent(this.context, TripDetailActivity::class.java)
        tripDetailIntent.putExtra("trip_detail",trip)
        startActivity(tripDetailIntent)
    }

}