package com.griesbeck.travelio.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trip(@get: Exclude
                var id: String = "",
                var image: String = "",
                var location: String = "",
                var period: String = "",
                var accomodation: String = "",
                var costs: String = "",
                var sights: List<Sight> = listOf() ) : Parcelable
