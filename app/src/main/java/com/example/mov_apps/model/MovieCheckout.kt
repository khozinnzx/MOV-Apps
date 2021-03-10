package com.example.mov_apps.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieCheckout(
    var uid: String?,
    var title: String?,
    var rating: Int?,
    var dateAndTime: String?,
    var place: String?,
    var seat: MutableList<Checkout>?,
    var poster: String?,
    var harga: Double,
    var status: String?
) : Parcelable {
    // empty constructor to allow deserialization
    constructor() : this(null, null, 0, null, null, null, null, 0.0, null)

}

