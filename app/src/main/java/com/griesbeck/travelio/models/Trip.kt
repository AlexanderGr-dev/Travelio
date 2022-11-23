package com.griesbeck.travelio.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Trip(var id: Long = 0,
                var image: Uri = Uri.EMPTY,
                var location: String = "",
                var period: String = "",
                var accomodation: String = "",
                var costs: String = "",
                var sights: List<Sight> = listOf()) : Parcelable
