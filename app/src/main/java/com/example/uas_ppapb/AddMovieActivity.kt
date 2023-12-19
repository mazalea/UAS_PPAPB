package com.example.uas_ppapb

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.uas_ppapb.databinding.ActivityAddMovieBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.util.UUID

class AddMovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddMovieBinding
    private lateinit var database: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri: Uri

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.imgAddPoster.setImageURI(uri)
                // Optionally, you can call uploadData(imageUri) here if needed
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            uploadData(imageUri)
        }

        binding.imgAddPoster.setOnClickListener {
            getContent.launch("image/*")
        }

        val backAddButton: ImageView = findViewById(R.id.btn_back)

        backAddButton.setOnClickListener {
            // Navigate to AdminDashboardActivity
            val intent = Intent(this@AddMovieActivity, AdminDashboardActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the current activity to avoid going back to it with the back button
        }

    }

    private fun uploadData(imageUri: Uri? = null) {
        val title: String = binding.edtAddTitle.text.toString()
        val director: String = binding.edtAddDirector.text.toString()
        val time: String = binding.edtAddTime.text.toString()
        val rate: String = binding.edtAddRating.text.toString()

        val imageId = UUID.randomUUID().toString()

        if (title.isNotEmpty() && director.isNotEmpty() && time.isNotEmpty() && rate.isNotEmpty() && imageUri != null) {
            // Generate a unique ID for the image

            // Upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
            val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                // Image uploaded successfully, now get the download URL
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val item = Movie(title, director, time, rate, imageUrl.toString())
                    database = FirebaseDatabase.getInstance().getReference("Movie")
                    database.child(imageId).setValue(item)
                        .addOnCompleteListener {
                            binding.edtAddTitle.text!!.clear()
                            binding.edtAddDirector.text!!.clear()
                            binding.edtAddTime.text!!.clear()
                            binding.edtAddRating.text!!.clear()

//                            showNotification("Data Uploaded Successfully")

                            startActivity(Intent(this, AdminDashboardActivity::class.java))
                            Toast.makeText(this, "Data Uploaded Successfully", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(this, "Adding Data Failed!", Toast.LENGTH_SHORT).show()
                        }
                }
            }.addOnFailureListener {
                Toast.makeText(this, "Image Upload Failed!", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show()
        }
    }



}