package com.example.traveldiary.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.traveldiary.R

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignInActivity : AppCompatActivity() {

    private val firebase: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    // Initialize Firebase Auth
    private val auth = FirebaseAuth.getInstance()

    private lateinit var usernameEmail : String
    private lateinit var password : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val signInButton: Button = findViewById(R.id.signInAuthButton)

        signInButton.setOnClickListener {
            // Retrieve username/email and password
            val usernameEditText: EditText = findViewById(R.id.usernameEditText)
            val passwordEditText: EditText = findViewById(R.id.passwordEditText)

            usernameEmail = usernameEditText.text.toString()
            password = passwordEditText.text.toString()

            // Perform authentication using the provided credentials
            // This is where you would typically check the credentials against your authentication system
            // For simplicity, let's just print the entered values for now
//            println("Username/Email: $usernameEmail")
//            println("Password: $password")

            // Here you can implement the logic to authenticate the user
            // If authentication is successful, you may proceed to the next part of your app
            // Try to sign in with email
            auth.signInWithEmailAndPassword(usernameEmail, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        // Save user token to local storage
                        val user = auth.currentUser
                        user?.getIdToken(true)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val token = task.result?.token
                                token?.let { saveUserToken(it) }
                                // Proceed to main activity
                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()
                            } else {
                                // Handle error
                            }
                        }


                        // Sign in success, update UI with the signed-in user's information
//                        Toast.makeText(this, "Sign in with email successful.", Toast.LENGTH_SHORT).show()
                        // Update UI or navigate to the next screen
//                        startActivity(Intent(this, HomeActivity::class.java))
//                        finish()
                    } else {
                        // If sign in with email fails, try signing in with username

                        // ...

                        Toast.makeText(this, "Sorry, your email or password is wrong", Toast.LENGTH_SHORT).show()
                        signInWithUsername(usernameEmail, password)
                    }
                }
        }

        val clickableTextView: TextView = findViewById(R.id.clickable_register)
        clickableTextView.setOnClickListener { onTextViewClick(it) }

    }

    private fun signInWithUsername(usernameEmail: String, password: String) {
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
    }

    companion object {
        val auth = FirebaseAuth.getInstance()
    }

    fun onTextViewClick(view: View) {
        // Handle the click event here
        // For example, show a toast message
        startActivity(Intent(this, SignUpActivity::class.java))
        finish()
//        showToast("TextView clicked!")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Save user token to SharedPreferences after successful sign-in
    fun saveUserToken(token: String) {
        val sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("userToken", token).apply()
    }


}