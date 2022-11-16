package com.griesbeck.travelio.models

import android.net.Uri

data class Trip(var id: Long = 0,
                var location: String = "",
                var period: String = "",
                var accomodation: String = "",
                var costs: String = "")
