package com.griesbeck.travelio.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Sight (var placeId: String = ""): Parcelable
