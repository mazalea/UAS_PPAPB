package com.example.uas_ppapb

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_ppapb.databinding.ActivityAdminDashboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore

class AdminDashboardActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAdminDashboardBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var itemList : ArrayList<Movie>
    private lateinit var database : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)



        Log.d("NEW222","INGPO ITEMLIST")
        Log.d("NEW222","INGPO ITEMLIST")
        Log.d("NEW222","INGPO ITEMLIST")


        recyclerView = findViewById(R.id.rv_movie_admin)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        itemList = arrayListOf()  // Move this line above the Log.d line
        movieAdapter = MovieAdapter(itemList)
        recyclerView.adapter = movieAdapter



        with(binding){
            floatingButton.setOnClickListener{
                startActivity(Intent(this@AdminDashboardActivity, AddMovieActivity::class.java))
            }
        }


        // Pass the onItemClick function to the MovieAdapter constructor

        database = FirebaseDatabase.getInstance().getReference("Movie")
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing list
                itemList.clear()

                // Iterate through the snapshot and add items to the list
                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(Movie::class.java)
                    if (item != null) {
                        itemList.add(item)
                    }
                }

                movieAdapter.notifyDataSetChanged()

                Log.d("msg",itemList.size.toString())
                Log.d("NEW",itemList.toString())
                Log.d("NEW",itemList.toString())
                Log.d("NEW",itemList.toString())

            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors if needed
                Toast.makeText(applicationContext, "Data retrieval failed!", Toast.LENGTH_SHORT).show()
            }
        })


    }
}