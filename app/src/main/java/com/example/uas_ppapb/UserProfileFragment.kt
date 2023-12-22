package com.example.uas_ppapb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.uas_ppapb.databinding.FragmentUserProfileBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var sharePreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        auth = Firebase.auth
        sharePreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)

        val currentUser = auth.currentUser
        if (currentUser != null) {
            binding.edtProfileEmail.setText(currentUser.email)
        }

//        binding.edtProfileEmail.setText(auth.currentUser!!.email)

        binding.btnLogout.setOnClickListener{
            sharePreferences.edit().putBoolean("isLoggedIn", false).apply()

            startActivity(Intent(requireActivity(), MainActivity::class.java))
            Firebase.auth.signOut()
        }
        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}