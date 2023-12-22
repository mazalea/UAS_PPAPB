package com.example.uas_ppapb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.uas_ppapb.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

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
        auth = FirebaseAuth.getInstance()

        val email: EditText = binding.edtEmail
        val password: EditText = binding.edtPassword
        val loginBtn: Button = binding.btnLogin

        // Check if user is already logged in using SharedPreferences
        val sharedPreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)

        if (isLoggedIn) {
            // User is already logged in, navigate to the appropriate screen
            val userType = sharedPreferences.getString("userType", "guest")!!
            navigateToDashboard(userType)
        } else {
            loginBtn.setOnClickListener {
                if (email.text.toString().isEmpty()) {
                    Toast.makeText(requireActivity(), "Please Fill the Email!", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                val userEmail = email.text.toString()

                auth.signInWithEmailAndPassword(userEmail, password.text.toString())
                    .addOnCompleteListener(requireActivity()) { taskk ->
                        if (taskk.isSuccessful) {
                            val currentUser: FirebaseUser? = auth.currentUser
                            if (currentUser != null) {
                                val userType = getUserTypeFromEmail(currentUser.email!!)
                                // Save user type to SharedPreferences
                                saveUserTypeToSharedPreferences(userType, userEmail)

                                // Navigate to Dashboard
                                navigateToDashboard(userType)
                            }
                        } else {
                            Toast.makeText(
                                requireActivity(),
                                "Login Failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        return binding.root
    }

    private fun saveUserTypeToSharedPreferences(userType: String, email: String) {
        val sharedPreferences =
            requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putBoolean("isLoggedIn", true)
        editor.putString("userType", userType)
        editor.putString("email", email)
        editor.apply()
    }

    private fun navigateToDashboard(userType: String) {
        val intent = if (userType == "admin") {
            Intent(requireContext(), AdminDashboardActivity::class.java)
        } else {
            Intent(requireContext(), UserDashboardActivity::class.java)
        }

        startActivity(intent)
        requireActivity().finish()
    }

    private fun getUserTypeFromEmail(email: String): String {
        return if (email == "admin@gmail.com") {
            "admin"
        } else {
            "user"
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
