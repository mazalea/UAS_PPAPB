package com.example.uas_ppapb

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.uas_ppapb.databinding.ActivityUserDashboardBinding

class UserDashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding){
            val navController = findNavController(R.id.nav_user)
            bottomNavigationView.setupWithNavController(navController)
        }
    }


}