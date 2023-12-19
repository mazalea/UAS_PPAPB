package com.example.uas_ppapb

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.uas_ppapb.databinding.FragmentRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RegisterFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RegisterFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var sharePreferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        auth = Firebase.auth

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth

        sharePreferences = requireActivity().getSharedPreferences("user_data", Context.MODE_PRIVATE)
        val editor = sharePreferences.edit()

        val email: EditText = binding.edtEmailReg
        val password: EditText = binding.edtPasswordReg
        val confirmPassword: EditText = binding.edtConfirmPassword
        val registerNow: Button = binding.btnRegister

        registerNow.setOnClickListener {
            if (email.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "PLEASE FILL THE EMAIl", Toast.LENGTH_SHORT)
                    .show()
            }
            if (password.text.toString().isEmpty()) {
                Toast.makeText(requireActivity(), "PLEASE FILL THE PASSWORD", Toast.LENGTH_SHORT)
                    .show()
            }


            if (password.text.toString() == confirmPassword.text.toString()) {
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            editor.putString("email", email.text.toString())
                            editor.putString("password", password.text.toString())
                            editor.apply()

                            email.text?.clear()
                            password.text?.clear()
                            confirmPassword.text?.clear()
                            Toast.makeText(
                                requireActivity(),
                                "Login Successfull!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
            } else {
                Log.d("NEW", "${password.text.toString()} ${confirmPassword.text.toString()}")
                Log.d("NEW", "${password.text.toString()} ${confirmPassword.text.toString()}")
                Log.d("NEW", "${password.text.toString()} ${confirmPassword.text.toString()}")

                Toast.makeText(requireActivity(),"MATCH THE PASSWORD PLEASE!",Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MateriFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RegisterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}