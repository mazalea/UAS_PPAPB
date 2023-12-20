package com.example.uas_ppapb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_ppapb.databinding.ActivityAdminDashboardBinding
import com.example.uas_ppapb.databinding.ActivityUserDashboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class UserDashboardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityUserDashboardBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieList: ArrayList<Movie>
    private lateinit var movieAdapterUser: MovieAdapterUser
    private lateinit var database : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = findViewById(R.id.rv_movie_user)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        movieList = arrayListOf()  // Move this line above the Log.d line
        movieAdapterUser = MovieAdapterUser(movieList)
        recyclerView.adapter = movieAdapterUser

        // Pass the onItemClick function to the MovieAdapter constructor
        database = FirebaseDatabase.getInstance().getReference("Movie")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing list
                movieList.clear()

                // Iterate through the snapshot and add items to the list
                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(Movie::class.java)
                    if (item != null) {
                        movieList.add(item)
                    }
                }

                movieAdapterUser.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if needed
                Toast.makeText(applicationContext, "Data retrieval failed!", Toast.LENGTH_SHORT).show()
            }
        })

    }
}