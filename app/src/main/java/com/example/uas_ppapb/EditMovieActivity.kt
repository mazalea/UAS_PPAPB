package com.example.uas_ppapb

import android.content.Intent
import android.net.Uri
import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.uas_ppapb.databinding.ActivityEditMovieBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class EditMovieActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditMovieBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var storageReference: StorageReference
    private lateinit var imageUri : Uri

    private val getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri != null) {
                imageUri = uri
                binding.imgEditPoster.setImageURI(uri)
                // Optionally, you can call uploadData(imageUri) here if needed
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val originalImageUrl = intent.getStringExtra("imgId")
        Glide.with(this@EditMovieActivity)
            .load(originalImageUrl)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.imgEditPoster)
        val backEditButton: ImageView = findViewById(R.id.btn_back_edit)

        val title = binding.edtEditTitle
        val director = binding.edtEditDirector
        val time = binding.edtEditTime
        val rating = binding.edtEditRating

        title.setText(intent.getStringExtra("title"))
        director.setText(intent.getStringExtra("director"))
        time.setText(intent.getStringExtra("time"))
        rating.setText(intent.getStringExtra("rating"))


        backEditButton.setOnClickListener {
            // Navigate to AdminDashboardActivity
            val intent = Intent(this@EditMovieActivity, AdminDashboardActivity::class.java)
            startActivity(intent)
            finish() // Optional: Close the current activity to avoid going back to it with the back button
        }

        binding.imgEditPoster.setOnClickListener{
            getContent.launch("image/*")
        }

        val updateButton: Button = findViewById(R.id.btn_update)
        updateButton.setOnClickListener {
            uploadData(imageUri)
        }

        val deleteButton: Button = findViewById(R.id.btn_delete)
        deleteButton.setOnClickListener {
            deleteDataAndImage()
        }
    }

    private fun uploadData(imageUri: Uri? = null) {
        val updatedTitle = binding.edtEditTitle.text.toString()
        val updatedDirector = binding.edtEditDirector.text.toString()
        val updatedTime = binding.edtEditTime.text.toString()
        val updatedRating = binding.edtEditRating.text.toString()

        databaseReference = FirebaseDatabase.getInstance().getReference("Movie")

        if (imageUri != null) {
            // Generate a unique ID for the image
            val imageId = Uri.parse(intent.getStringExtra("imgId")).lastPathSegment?.removePrefix("images/")

            // Upload image to Firebase Storage with the generated ID
            storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")
            val uploadTask: UploadTask = storageReference.putFile(imageUri)

            uploadTask.addOnSuccessListener {
                // Image uploaded successfully, now get the download URL
                storageReference.downloadUrl.addOnSuccessListener { imageUrl ->
                    val item = Movie(updatedTitle, updatedDirector, updatedTime, updatedRating, imageUrl.toString())
                    databaseReference.child(imageId!!).setValue(item)
                        .addOnCompleteListener {
                            // Handle completion, e.g., clear input fields and   show a success message
                            binding.edtEditTitle.text!!.clear()
                            binding.edtEditDirector.text!!.clear()
                            binding.edtEditTime.text!!.clear()
                            binding.edtEditRating.text!!.clear()
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
            // If no new image is selected, update the data without uploading a new image
            val imageId = Uri.parse(intent.getStringExtra("imgId")).lastPathSegment?.removePrefix("images/")

            val updatedList = mapOf<String, String>(
                "title" to updatedTitle,
                "director" to updatedDirector,
                "time" to updatedTime,
                "rate" to updatedRating
            )

            // Update the data with the new title
            databaseReference.child(imageId!!).updateChildren(updatedList)
                .addOnCompleteListener {
                    // Handle completion, e.g., clear input fields and show a success message
                    binding.edtEditTitle.text!!.clear()
                    binding.edtEditDirector.text!!.clear()
                    binding.edtEditTime.text!!.clear()
                    binding.edtEditRating.text!!.clear()
                    Toast.makeText(this, "Data Updated Successfully", Toast.LENGTH_SHORT).show()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Updating Data Failed!", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun deleteDataAndImage() {
        val imageId = Uri.parse(intent.getStringExtra("imgId")).lastPathSegment?.removePrefix("images/")

        if (imageId.isNullOrEmpty()) {
            Toast.makeText(this, "Image ID is null or empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a reference to the image in Firebase Storage
        val storageReference = FirebaseStorage.getInstance().reference.child("images/$imageId")

        // Delete the image from Firebase Storage
        storageReference.delete()
            .addOnSuccessListener {
                // Delete data from Firebase Realtime Database after successfully deleting the image
                val databaseReference = FirebaseDatabase.getInstance().getReference("Movie") // Update the reference
                databaseReference.child(imageId).removeValue()
                    .addOnCompleteListener {
                        // Handle completion, e.g., show a success message
                        Toast.makeText(this, "Data Deleted Successfully", Toast.LENGTH_SHORT).show()

                        // Optionally, you can navigate back to AdminDashboardActivity or finish the current activity
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Deleting Data Failed!", Toast.LENGTH_SHORT).show()
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Deleting Image Failed!", Toast.LENGTH_SHORT).show()
            }
    }

}