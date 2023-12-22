package com.example.uas_ppapb

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_ppapb.database.MovieDao
import com.example.uas_ppapb.database.MovieRoom
import com.example.uas_ppapb.database.MovieRoomDatabase
import com.example.uas_ppapb.databinding.FragmentUserDashboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UserDashboardFragment : Fragment() {
    private lateinit var binding: FragmentUserDashboardBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieList: ArrayList<MovieRoom>
    private lateinit var database: DatabaseReference
    private lateinit var movieAdapterUser: MovieAdapterUser

    private lateinit var roomMovieDao: MovieDao
    private lateinit var roomMovieDatabase: MovieRoomDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Inisialisasi Room Database dan DAO
        roomMovieDatabase = MovieRoomDatabase.getDatabase(requireContext())!!
        roomMovieDao = roomMovieDatabase.movieDao()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment using the View Binding
        binding = FragmentUserDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = binding.rvMovieUser
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        movieList = arrayListOf()
        movieAdapterUser = MovieAdapterUser(movieList)
        recyclerView.adapter = movieAdapterUser


        //initialize firebase database
        database = FirebaseDatabase.getInstance().getReference("Movie")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                movieList.clear()

                for (dataSnapshot in snapshot.children) {
                    val item = dataSnapshot.getValue(MovieRoom::class.java)
                    if (item != null) {
                        movieList.add(item)
                    }
                }
                // Simpan data ke Room
                saveMoviesToRoom(movieList)
                movieAdapterUser.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Data retrieval failed!", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun saveMoviesToRoom(roomMovies: List<MovieRoom>) {
        // Simpan data ke Room
        lifecycleScope.launch(Dispatchers.IO) {
            // Hapus semua data di Room terlebih dahulu
            roomMovieDao.deleteAllMovies()
            roomMovieDao.insertMovies(roomMovies)

            // convert list movie room ke list movie
            var roomMoviesGet:List<MovieRoom> = roomMovieDao.getAllMovies()
            Log.d("roomMoviesGet", roomMoviesGet.toString())
            movieList.clear()
            movieList.addAll(roomMoviesGet)
        }
    }
}
