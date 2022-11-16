package com.griesbeck.travelio.ui.trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.griesbeck.travelio.TripAdapter
import com.griesbeck.travelio.databinding.FragmentTripsBinding
import com.griesbeck.travelio.models.Trip

class TripsFragment : Fragment() {

    private var _binding: FragmentTripsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val tripsViewModel = ViewModelProvider(this).get(TripsViewModel::class.java)
    private val layoutManager = GridLayoutManager(this.context,2)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentTripsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        tripsViewModel.trips.observe(viewLifecycleOwner, Observer { trips ->
            binding.rvTrips.layoutManager = layoutManager
            binding.rvTrips.adapter = TripAdapter(trips)
        })

        return root
    }

    override fun onResume() {
        super.onResume()

        tripsViewModel.trips.observe(viewLifecycleOwner, Observer { trips ->
            binding.rvTrips.layoutManager = layoutManager
            binding.rvTrips.adapter = TripAdapter(trips)
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}