package com.griesbeck.travelio.ui.trips.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.griesbeck.travelio.R
import com.griesbeck.travelio.databinding.CardTripBinding
import com.griesbeck.travelio.helpers.stringToBitMap
import com.griesbeck.travelio.models.trips.Trip

interface TripListener {
    fun onTripClick(trip: Trip, pair: Pair<View, String>)
}

class TripAdapter(private val trips: List<Trip>, private val listener: TripListener) :
    RecyclerView.Adapter<TripAdapter.ViewHolder>() {


    class ViewHolder(private val binding: CardTripBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(trip: Trip, listener: TripListener) {
                if(trip.image.isNotEmpty()) {
                    binding.ivLocation.setImageBitmap(stringToBitMap(trip.image))
                }else{
                    binding.ivLocation.setImageResource(R.drawable.placeholder)
                }
                binding.ivLocation.transitionName = trip.location
                val period = "${trip.startDate} - ${trip.endDate}"
                binding.locationTitle.text = trip.location
                binding.tvTripPeriod.text = period
                binding.tripAccommodation.text = trip.accommodation
                binding.root.setOnClickListener {
                    // Initialize pair for shared element transition
                    val pair = Pair(
                        binding.ivLocation as View,
                        binding.ivLocation.transitionName
                    )
                    listener.onTripClick(trip,pair)
                }
            }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = CardTripBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val trip = trips[viewHolder.adapterPosition]
        viewHolder.bind(trip, listener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = trips.size

}