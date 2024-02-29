package com.example.traveldiary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Realtime Database
        databaseRef = FirebaseDatabase.getInstance().getReference("Users")

        // Load user data
        loadUserData()

        // Set onClickListener for the "Update Your Info" button
        val updateInfoButton: Button = findViewById(R.id.updateInfoButton)
        updateInfoButton.setOnClickListener {
             val intent = Intent(this, SettingsActivity::class.java)
             startActivity(intent)
        }
    }

    private fun loadUserData() {
        val user = auth.currentUser
        val encodedEmail = user?.email.toString().replace(".", "_dot_").replace("@", "_at_")
        val userRef = databaseRef.child(encodedEmail)

        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val fullName = snapshot.child("fullName").value.toString()
                    val username = snapshot.child("username").value.toString()
                    val email = user?.email

                    // Update UI with user information
                    val fullNameTextView: TextView = findViewById(R.id.fullNameTextView)
                    val usernameTextView: TextView = findViewById(R.id.usernameTextView)
                    val emailTextView: TextView = findViewById(R.id.emailTextView)
                    fullNameTextView.text = fullName
                    usernameTextView.text = username
                    emailTextView.text = email
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })
    }
}

