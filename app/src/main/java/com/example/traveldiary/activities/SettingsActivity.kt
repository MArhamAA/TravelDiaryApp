package com.example.traveldiary.activities

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.traveldiary.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class SettingsActivity : AppCompatActivity() {

    private lateinit var editTextFullName: EditText
    private lateinit var editTextUsername: EditText
    private lateinit var editTextOldPassword: EditText
    private lateinit var editTextNewPassword: EditText
    private lateinit var auth: FirebaseAuth

    private val databaseRef = FirebaseDatabase.getInstance().getReference("Users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // Initialize views
        editTextFullName = findViewById(R.id.editTextFullName)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextOldPassword = findViewById(R.id.editTextOldPassword)
        editTextNewPassword = findViewById(R.id.editTextNewPassword)

        // Populate input fields with existing user data
//        populateUserData()

        // Set click listener for update button
        findViewById<Button>(R.id.buttonUpdate).setOnClickListener {
            updateUserInformation()
        }
    }

    private fun populateUserData() {
        // Retrieve user data from Firebase Authentication
        val user = auth.currentUser
        val userRef = user?.let { databaseRef.child(it.uid) }
        if (userRef != null) {
            userRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.child("username").value.toString()
                        val fullname = snapshot.child("fullname").value.toString()
                        editTextFullName.setText(fullname)
                        editTextUsername.setText(username)
                    } else {

                    }
                }
                override fun onCancelled(error: DatabaseError) {

                }
            })
        }
    }


    private fun updateUserInformation() {
        val fullName = editTextFullName.text.toString().trim()
        val username = editTextUsername.text.toString().trim()
        val oldPassword = editTextOldPassword.text.toString().trim()
        val newPassword = editTextNewPassword.text.toString().trim()

        // Update user information in Firebase Authentication
        val user = auth.currentUser

        val copy_email = user?.email
        val encodedEmail = copy_email.toString().replace(".", "_dot_").replace("@", "_at_")
        val userRef =  databaseRef.child(encodedEmail)

        if (user != null) {
            auth.signInWithEmailAndPassword(user.email.toString(), oldPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        val newData = HashMap<String, Any>()
                        newData["fullname"] = fullName
                        newData["username"] = username
                        newData["password"] = newPassword

                        userRef.updateChildren(newData)
                            .addOnSuccessListener {
                                // Data updated successfully
                                Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { e ->
                                // Failed to update data
                                Toast.makeText(this, "Failed to update data: ${e.message}", Toast.LENGTH_SHORT).show()
                            }

                        user.updatePassword(newPassword)
                            ?.addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    // Password updated successfully
                                    Toast.makeText(this, "Pass Updated successfully", Toast.LENGTH_SHORT).show()
                                } else {
                                    // Failed to update password
                                    Toast.makeText(this, "Failed to update: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                                }
                            }

//                        Toast.makeText(this, "Error: Wrong Password", Toast.LENGTH_SHORT).show()
                    } else {
                        // If sign in with email fails, try signing in with username

                        // ...

                        Toast.makeText(this, "Error: Wrong Password", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

}

