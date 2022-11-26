package com.griesbeck.travelio

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.griesbeck.travelio.databinding.CardTripBinding
import com.griesbeck.travelio.models.Trip
import com.squareup.picasso.Picasso

interface TripListener {
    fun onTripClick(trip: Trip)
}

class TripAdapter(private val trips: List<Trip>, private val listener: TripListener) :
    RecyclerView.Adapter<TripAdapter.ViewHolder>() {


    class ViewHolder(private val binding: CardTripBinding) : RecyclerView.ViewHolder(binding.root) {

            fun bind(trip: Trip, listener: TripListener) {
                Picasso.get().load(trip.image.toUri()).into(binding.ivLocation)
                binding.locationTitle.text = trip.location
                binding.tvTripPeriod.text = trip.period
                binding.tripAccomodation.text = trip.accomodation
                binding.root.setOnClickListener {
                    listener.onTripClick(trip)
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