package com.example.traveldiary.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.traveldiary.R
import com.example.traveldiary.databinding.ActivityImagesUploadBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class ImagesUploadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityImagesUploadBinding
    private lateinit var storageRef: StorageReference
    private lateinit var firebaseFirestore: FirebaseFirestore
    private var imageUri: Uri? = null
    private lateinit var diaryId: String
    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityImagesUploadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        diaryId = intent.getStringExtra("diary_id").toString()
        initVars()
        registerClickEvents()
    }

    private fun registerClickEvents() {
        binding.uploadBtn.setOnClickListener {
            uploadImage()
        }

        binding.showAllBtn.setOnClickListener {
            val intent = Intent(this, ImagesActivity::class.java)
            intent.putExtra("diary_id", diaryId)
            startActivity(intent)
        }

        binding.imageView.setOnClickListener {
            resultLauncher.launch("image/*")
        }
    }

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) {

        imageUri = it
        binding.imageView.setImageURI(it)
    }


    private fun initVars() {

        storageRef = FirebaseStorage.getInstance().reference.child("Images")
        firebaseFirestore = FirebaseFirestore.getInstance()
    }

    private fun uploadImage() {
        binding.progressBar.visibility = View.VISIBLE
        storageRef = storageRef.child(System.currentTimeMillis().toString())
        user = FirebaseAuth.getInstance().currentUser!!

        imageUri?.let {
            storageRef.putFile(it).addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    storageRef.downloadUrl.addOnSuccessListener { uri ->

                        val map = HashMap<String, Any>()
                        map["user_id"] = user.uid
                        map["diary_id"] = diaryId
                        map["img"] = uri.toString()

                        firebaseFirestore.collection("images").add(map).addOnCompleteListener { firestoreTask ->

                            if (firestoreTask.isSuccessful){
                                Toast.makeText(this, "Uploaded Successfully", Toast.LENGTH_SHORT).show()

                            }else{
                                Toast.makeText(this, firestoreTask.exception?.message, Toast.LENGTH_SHORT).show()

                            }
                            binding.progressBar.visibility = View.GONE
                            binding.imageView.setImageResource(R.drawable.vector)

                        }
                    }
                } else {
                    Toast.makeText(this, task.exception?.message, Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = View.GONE
                    binding.imageView.setImageResource(R.drawable.vector)
                }
            }
        }
    }

}