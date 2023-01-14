package com.griesbeck.travelio.models.trips

import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trip(@get: Exclude
                var id: String = "",
                var locLat: Double = 0.0,
                var locLon: Double = 0.0,
                var image: String = "",
                var location: String = "",
                var startDate: String = "",
                var endDate: String = "",
                var accommodation: String = "",
                var costs: String = "",
                var sights: List<Sight> = listOf() ) : Parcelable
