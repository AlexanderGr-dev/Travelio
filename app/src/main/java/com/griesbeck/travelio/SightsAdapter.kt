package com.griesbeck.travelio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPhotoRequest
import com.google.android.libraries.places.api.net.FetchPhotoResponse
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.griesbeck.travelio.databinding.CardSightBinding
import com.griesbeck.travelio.databinding.CardTripBinding
import com.griesbeck.travelio.models.Sight
import com.griesbeck.travelio.models.Trip
import com.squareup.picasso.Picasso

class SightsAdapter(private val sights: List<Sight>) :
    RecyclerView.Adapter<SightsAdapter.ViewHolder>() {



    class ViewHolder(private val binding: CardSightBinding) : RecyclerView.ViewHolder(binding.root) {

        val placesClient = Places.createClient(itemView.context)

        fun bind(sight: Sight) {
            val placeId = sight.placeId

// Specify fields. Requests for photos must always have the PHOTO_METADATAS field.
            val fields = listOf(Place.Field.PHOTO_METADATAS,Place.Field.NAME)

// Get a Place object (this example uses fetchPlace(), but you can also use findCurrentPlace())
            val placeRequest = FetchPlaceRequest.newInstance(placeId, fields)

            placesClient.fetchPlace(placeRequest)
                .addOnSuccessListener { response: FetchPlaceResponse ->
                    val place = response.place

                    // Get the photo metadata.
                    val metada = place.photoMetadatas
                    if (metada == null || metada.isEmpty()) {
                        return@addOnSuccessListener
                    }
                    val photoMetadata = metada.first()

                    // Get the attribution text.
                    val attributions = photoMetadata?.attributions

                    // Create a FetchPhotoRequest.
                    val photoRequest = FetchPhotoRequest.builder(photoMetadata)
                        .setMaxWidth(500) // Optional.
                        .setMaxHeight(300) // Optional.
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
        }

    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        /*val view = LayoutInflater.from(viewGroup.context)
            .inflate(CardTripBinding, viewGroup, false)*/
        val view = CardSightBinding
            .inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        val sight = sights[viewHolder.adapterPosition]
        viewHolder.bind(sight)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = sights.size


}