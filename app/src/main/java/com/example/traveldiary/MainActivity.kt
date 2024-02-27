// BISMILLAH

package com.example.traveldiary

import android.content.Context
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

        // Check if user is signed in
        if (isUserSignedIn()) {
            // Proceed with automatic sign-in
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            // Redirect to sign-in activity
            // Sign In Button Click
            val signInButton: Button = findViewById(R.id.signInAuthButton)
            signInButton.setOnClickListener {
                // Handle Sign In button click
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                finish()
            }

            // Sign Up Button Click
            val signUpButton: Button = findViewById(R.id.signUpButton)
            signUpButton.setOnClickListener {
                // Handle Sign In button click
                val intent = Intent(this, SignUpActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    // Retrieve user token from SharedPreferences
    fun getUserToken(): String? {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getString("userToken", null)
    }

    // Check if user is signed in
    fun isUserSignedIn(): Boolean {
        return getUserToken() != null
    }
}