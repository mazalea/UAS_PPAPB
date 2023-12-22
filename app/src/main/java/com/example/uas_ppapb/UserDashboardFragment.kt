package com.example.uas_ppapb

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uas_ppapb.database.MovieRoom
import com.example.uas_ppapb.databinding.FragmentUserDashboardBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class UserDashboardFragment : Fragment() {
    private lateinit var binding: FragmentUserDashboardBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var movieList: ArrayList<MovieRoom>
    private lateinit var database: DatabaseReference
    private lateinit var movieAdapterUser: MovieAdapterUser

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
                movieAdapterUser.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(), "Data retrieval failed!", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
