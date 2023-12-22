package com.example.uas_ppapb

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.uas_ppapb.databinding.FragmentLoginBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        auth = Firebase.auth

        val email: EditText = binding.edtEmail
        val password: EditText = binding.edtPassword
        val loginBtn : Button = binding.btnLogin

        // Check if user credentials are saved in SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("email", null)
        val savedPassword = sharedPreferences.getString("password", null)
        val editor = sharedPreferences.edit()

        loginBtn.setOnClickListener {
            if (email.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Please Fill the Email!", Toast.LENGTH_SHORT).show()
            }
            if (password.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "Please Fill the Password!", Toast.LENGTH_SHORT).show()
            }

            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
                .addOnCompleteListener(requireActivity()) { taskk ->
                    val currentUser = auth.currentUser
                    if (taskk.isSuccessful) {
                        if (currentUser != null) {
                            // Save user credentials in SharedPreferences
                            // Check if the user is an admin based on their email address
                            if (currentUser.email == "admin@gmail.com") {
                                // User is an admin, redirect to Admin activity
                                editor.putString("email", email.text.toString())
                                editor.putString("password", password.text.toString())
                                editor.apply()
                                startActivity(Intent(requireActivity(), AdminDashboardActivity::class.java))
                            } else {
                                // User is not an admin, redirect to User activity
                                startActivity(Intent(requireActivity(), UserDashboardActivity::class.java))
                            }
                        }
                    } else {
                        Toast.makeText(requireActivity(), "Login Failed", Toast.LENGTH_SHORT).show()
                    }
                }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        }










}