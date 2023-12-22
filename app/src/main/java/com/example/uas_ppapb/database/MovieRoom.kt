package com.example.uas_ppapb.database

import android.support.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_table")
data class MovieRoom(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    var id: Int=0,
    @ColumnInfo(name = "title")
    var title: String,
    @ColumnInfo(name = "director")
    var director: String,
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "rate")
    var rate: String,
    @ColumnInfo(name = "synopsis")
    var synopsis: String,
    @ColumnInfo(name = "imageUrl")
    var imageUrl: String
) {
    constructor() : this(0, "", "", "", "", "", "")
}

