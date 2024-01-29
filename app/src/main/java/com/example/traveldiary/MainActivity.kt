// BISMILLAH

package com.example.traveldiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.firebase.FirebaseApp

import android.content.Intent
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseApp.initializeApp(this)

        // Sign In Button Click
        val signInButton: Button = findViewById(R.id.signInAuthButton)
        signInButton.setOnClickListener {
            // Handle Sign In button click
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }

        // Sign Up Button Click
        val signUpButton: Button = findViewById(R.id.signUpButton)
        signUpButton.setOnClickListener {
            // Handle Sign In button click
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}