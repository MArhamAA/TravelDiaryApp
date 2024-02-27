package com.example.traveldiary

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import com.example.traveldiary.SignInActivity.Companion.auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUpActivity : AppCompatActivity() {

    private val firebase: DatabaseReference = FirebaseDatabase.getInstance().getReference("Users")

    private lateinit var usernameET : EditText
    private lateinit var emailET : EditText
    private lateinit var passwordET : EditText
    private lateinit var fullnameET: EditText

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

        registerButton = findViewById(R.id.signUpButton)
        registerButton.setOnClickListener {
            saveUser()

            // get values
            val username = usernameET.text.toString()
            val email = emailET.text.toString()
            val fullname = fullnameET.text.toString()
            val password = passwordET.text.toString()

            if (username.isNotEmpty() && email.isNotEmpty() && fullname.isNotEmpty() && password.isNotEmpty()) {
                // Create user with email and password
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Sign up success, update UI with the signed-up user's information
                            val user = auth.currentUser
                            Toast.makeText(this, "Sign up successful", Toast.LENGTH_SHORT).show()
                            // You may want to navigate to the next screen or perform other actions
                            startActivity(Intent(this, HomeActivity::class.java))
                            finish()
                        } else {
                            // If sign up fails, display a message to the user.
                            val exception = task.exception
                            if (exception is FirebaseAuthException) {
                                // Handle specific Firebase Authentication errors
                                Toast.makeText(this, "Sign up failed: ${exception.message}", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                // Handle other exceptions
                                Toast.makeText(this, "Sign up failed.", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            }
                        }
                    }
            }
        }

        val clickableTextView: TextView = findViewById(R.id.clickable_login)
        clickableTextView.setOnClickListener { onTextViewClick(it) }

    }

    private fun saveUser() {
        // get values
        val username = usernameET.text.toString()
        val email = emailET.text.toString()
        val fullname = fullnameET.text.toString()
        val password = passwordET.text.toString()

        if (username.isEmpty()) {
            usernameET.error = "Please Enter Valid Username"
            return
        }

        if (email.isEmpty()) {
            usernameET.error = "Please Enter Valid Email"
            return
        }

        if (password.isEmpty()) {
            usernameET.error = "Please Enter Valid Password"
            return
        }

        if (fullname.isEmpty()) {
            usernameET.error = "Please Enter Valid Fullname"
            return
        }

//        val userId = firebase.push().key!!
        val copy_email = email
        val encodedEmail = copy_email.replace(".", "_dot_").replace("@", "_at_")
        val user = UserModel(email, username, fullname, password)

        firebase.child(encodedEmail).setValue(user)
            .addOnCompleteListener {
                Toast.makeText(this, "You are registered", Toast.LENGTH_LONG).show()

                usernameET.text.clear()
                emailET.text.clear()
                fullnameET.text.clear()
                passwordET.text.clear()

            }.addOnCanceledListener {
                Toast.makeText(this, "Error in registration", Toast.LENGTH_LONG).show()
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

}