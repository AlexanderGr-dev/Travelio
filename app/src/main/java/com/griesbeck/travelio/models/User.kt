package com.griesbeck.travelio.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(var id: String = "",
                var email: String = "",
                var name: String = "",
                var photo: String = ""): Parcelable
