package com.griesbeck.travelio.ui.trips

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
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
    val trips = ArrayList<Trip>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        /*val tripsViewModel =
            ViewModelProvider(this).get(TripsViewModel::class.java)*/

        _binding = FragmentTripsBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //Testdata
        trips.add(Trip("Regensburg", "24.11 - 12.12.2022", "Hotel Donaublick"))
        trips.add(Trip("Regensburg", "24.11 - 12.12.2022", "Hotel Donaublick"))
        trips.add(Trip("München", "24.11 - 12.12.2022", "Hotel Donaublick"))
        trips.add(Trip("München", "24.11 - 12.12.2022", "Hotel Donaublick"))
        trips.add(Trip("München", "24.11 - 12.12.2022", "Hotel Donaublick"))
        trips.add(Trip("München", "24.11 - 12.12.2022", "Hotel Donaublick"))

        /*val textView: TextView = binding.textGallery
        tripsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        tripsViewModel.trips.observe(viewLifecycleOwner) { trips ->
            val layoutManager = GridLayoutManager(this.context,2)
            binding.rvTrips.layoutManager = layoutManager
            binding.rvTrips.adapter = TripAdapter(trips)
        }
        */



        val layoutManager = GridLayoutManager(this.context,2)
        binding.rvTrips.layoutManager = layoutManager
        binding.rvTrips.adapter = TripAdapter(trips)


        return root
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}