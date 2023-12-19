package com.example.uas_ppapb

import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class EditMovieActivity : AppCompatActivity() {
    private lateinit var titleEditText: EditText
    private lateinit var directorEditText: EditText
    private lateinit var timeEditText: EditText
    private lateinit var ratingEditText: EditText
    private lateinit var updateButton: Button
    private lateinit var currentMovie: Movie

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_movie)


    }


}