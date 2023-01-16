package com.griesbeck.travelio.ui.trips.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import com.griesbeck.travelio.databinding.CardSightBinding
import com.griesbeck.travelio.models.trips.Sight


interface SightDeleteListener {
    fun onDeleteClick(sight: Sight)
}

class SightsAdapter(private val sights: List<Sight>, private val listener: SightDeleteListener) :
    RecyclerView.Adapter<SightsAdapter.ViewHolder>() {


    class ViewHolder(private val binding: CardSightBinding) : RecyclerView.ViewHolder(binding.root) {

        private val placesClient: PlacesClient = Places.createClient(itemView.context)

        fun bind(sight: Sight, listener: SightDeleteListener) {
            val placeId = sight.placeId
            val fields = listOf(Place.Field.PHOTO_METADATAS,Place.Field.NAME)
            val placeRequest = FetchPlaceRequest.newInstance(placeId, fields)
            placesClient.fetchPlace(placeRequest)
                .addOnSuccessListener { response: FetchPlaceResponse ->
                    val place = response.place

                    // Get the photo metadata.
                    val metaData = place.photoMetadatas
                    if (metaData == null || metaData.isEmpty()) {
                        return@addOnSuccessListener
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
                            binding.ivSight.setImageBitmap(bitmap)
                            binding.tvSightName.text = place.name
                        }.addOnFailureListener { exception: Exception ->
                            if (exception is ApiException) {
                                val statusCode = exception.statusCode
                                TODO("Handle error with given status code.")
                            }
                        }
                }
            binding.btnDeleteSight.setOnClickListener {
                listener.onDeleteClick(sight)
            }
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = CardSightBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val sight = sights[viewHolder.adapterPosition]
        viewHolder.bind(sight,listener)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = sights.size


}