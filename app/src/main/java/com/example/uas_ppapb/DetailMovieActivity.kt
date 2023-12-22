package com.example.uas_ppapb

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.uas_ppapb.databinding.ActivityDetailMovieBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.storage.StorageReference

class DetailMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailMovieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val originalImageUrl = intent.getStringExtra("imgId")
        Glide.with(this@DetailMovieActivity)
            .load(originalImageUrl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imgDetailPoster)
        val title = binding.edtDetailTitle
        val director = binding.edtDetailDirector
        val time = binding.edtDetailTime
        val rate = binding.edtDetailRate
        val synopsis = binding.edtDetailSynopsis

        title.setText(intent.getStringExtra("title"))
        director.setText(intent.getStringExtra("director"))
        time.setText(intent.getStringExtra("time"))
        rate.setText(intent.getStringExtra("rate"))
        synopsis.setText(intent.getStringExtra("synopsis"))

        binding.btnDetailBack.setOnClickListener {
            // Navigate to AdminDashboardActivity
            val intent = Intent(this@DetailMovieActivity, UserDashboardActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the current activity to avoid going back to it with the back button
        }
    }
}