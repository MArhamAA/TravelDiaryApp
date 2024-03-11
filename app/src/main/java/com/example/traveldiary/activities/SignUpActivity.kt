package com.example.traveldiary.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.traveldiary.R
import com.example.traveldiary.entities.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private val firebase: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    private lateinit var usernameET : EditText
    private lateinit var emailET : EditText
    private lateinit var passwordET : EditText
    private lateinit var fullnameET: EditText
    private lateinit var confirmPassET: EditText

    private lateinit var registerButton : Button

    private val auth = FirebaseAuth.getInstance()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        usernameET = findViewById(R.id.usernameEditText)
        emailET = findViewById(R.id.emailEditText)
        passwordET = findViewById(R.id.passwordEditText)
        fullnameET = findViewById(R.id.fullNameEditText)
        confirmPassET = findViewById(R.id.confirmPasswordEditText)

        registerButton = findViewById(R.id.signUpButton)
        registerButton.setOnClickListener {
            saveUser()

            // get values
            val username = usernameET.text.toString()
            val email = emailET.text.toString()
            val fullname = fullnameET.text.toString()
            val password = passwordET.text.toString()

            // Validate email
//            if (!isEmailValid(email)) {
//                // Show error message if email is not valid
//                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Validate password length
//            if (password.length < 6) {
//                // Password is too short, show error message
//                Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            // Check if password matches confirm password
//            if (password != confirmPass) {
//                // Passwords don't match, show error message
//                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

//            if (username.isNotEmpty() && email.isNotEmpty() && fullname.isNotEmpty() && password.isNotEmpty()) {
//
//            }
        }

        val clickableTextView: TextView = findViewById(R.id.clickable_login)
        clickableTextView.setOnClickListener { onTextViewClick(it) }

    }

    private fun saveUser() {
        val username = usernameET.text.toString()
        val email = emailET.text.toString()
        val fullname = fullnameET.text.toString()
        val password = passwordET.text.toString()
        val confirmPass = confirmPassET.text.toString()

        // Validate username, email, password, and fullname
        if (username.isEmpty()) {
            usernameET.error = "Please enter a valid username"
            return
        }

        if (!isEmailValid(email)) {
            emailET.error = "Please enter a valid email address"
            return
        }

        if (password.length < 6) {
            passwordET.error = "Password must be at least 6 characters long"
            return
        }

        // Check if password matches confirm password
        if (password != confirmPass) {
            // Passwords don't match, show error message
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return
        }

        if (fullname.isEmpty()) {
            fullnameET.error = "Please enter your full name"
            return
        }

        // Create the user in Firebase Authentication
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // User registration successful, proceed to save user data
                    val user = auth.currentUser
                    val encodedEmail = email.replace(".", "_dot_").replace("@", "_at_")
                    val userModel = UserModel(email, username, fullname)

                    // Save user data to Firebase Realtime Database
                    firebase.child(encodedEmail).setValue(userModel)
                        .addOnCompleteListener {
                            // Registration and data save successful
                            Toast.makeText(this, "You are registered", Toast.LENGTH_SHORT).show()

                            // Create user with email and password
//                            auth.createUserWithEmailAndPassword(email, password)
//                                .addOnCompleteListener(this) { task ->
//                                    if (task.isSuccessful) {
//                                        // Sign up success, update UI with the signed-up user's information
//                                        val user = auth.currentUser
//                                        Toast.makeText(this, "Sign up successful, check email to verify", Toast.LENGTH_SHORT).show()
//                                        // After successful user registration
//                                        user?.sendEmailVerification()
//                                            ?.addOnCompleteListener { task ->
//                                                if (task.isSuccessful) {
//                                                    // Email verification sent successfully
//                                                    Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show()
//                                                } else {
//                                                    // Failed to send verification email
//                                                    Toast.makeText(this, "Failed to send verification email", Toast.LENGTH_SHORT).show()
//                                                }
//                                            }
//                                        FirebaseAuth.getInstance().signOut()
//                                        startActivity(Intent(this, SignInActivity::class.java))
//                                        finish()
//                                        // You may want to navigate to the next screen or perform other actions
//                                        startActivity(Intent(this, HomeActivity::class.java))
//                                        finish()
//                                    } else {
//                                        // If sign up fails, display a message to the user.
//                                        val exception = task.exception
//                                        if (exception is FirebaseAuthException) {
//                                            // Handle specific Firebase Authentication errors
//                                            Toast.makeText(this, "Sign up failed: ${exception.message}", Toast.LENGTH_SHORT).show()
//                                            startActivity(Intent(this, MainActivity::class.java))
//                                            finish()
//                                        } else {
//                                            // Handle other exceptions
//                                            Toast.makeText(this, "Sign up failed.", Toast.LENGTH_SHORT).show()
//                                            startActivity(Intent(this, MainActivity::class.java))
//                                            finish()
//                                        }
//                                    }
//                                }
                                        FirebaseAuth.getInstance().signOut()
                                        startActivity(Intent(this, SignInActivity::class.java))
                                        finish()
                        }
                        .addOnFailureListener { e ->
                            // Error saving user data
                            Toast.makeText(this, "Error in registration: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                } else {
                    // User registration failed
                    val exception = task.exception
                    Toast.makeText(this, "Registration failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }


    fun onTextViewClick(view: View) {
        // Handle the click event here
        // For example, show a toast message
        startActivity(Intent(this, SignInActivity::class.java))
        finish()
//        showToast("TextView clicked!")
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // Function to validate email using regex
    private fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}")
        return emailRegex.matches(email)
    }

}