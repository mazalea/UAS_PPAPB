package com.example.uas_ppapb

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.DocumentId

data class Movie(
    val title: String = "",
    val director: String = "",
    val time: String = "",
    val rate: String = "",
    val synopsis: String = "",
    val imageUrl: String = ""
): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(title)
        parcel.writeString(director)
        parcel.writeString(time)
        parcel.writeString(rate)
        parcel.writeString(synopsis)
        parcel.writeString(imageUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}



