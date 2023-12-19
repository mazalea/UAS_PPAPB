package com.example.uas_ppapb

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Movie(
    val title: String = "",
    val director: String = "",
    val time: String = "",
    val rate: String = "",
    val imageUrl: String = ""
)



