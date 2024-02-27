package com.example.traveldiary

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference

    private lateinit var editTextFullName: EditText
    private lateinit var editTextUsername: EditText
    private lateinit var editTextNewPassword: EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child("Users")

        editTextFullName = findViewById(R.id.editTextFullName)
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextNewPassword = findViewById(R.id.editTextNewPassword)

        loadUserData()

        val saveChangesButton: Button = findViewById(R.id.saveChangesButton)
        saveChangesButton.setOnClickListener {
            saveChanges()
        }

        val updatePasswordButton: Button = findViewById(R.id.updatePasswordButton)
        updatePasswordButton.setOnClickListener {
            updatePassword()
        }
    }

    private fun loadUserData() {
        val currentUser = auth.currentUser
        currentUser?.let { user ->
            editTextFullName.setText(user.displayName)
            editTextUsername.setText(user.email)
        }
    }

    private fun saveChanges() {
        val currentUser = auth.currentUser
        val newFullName = editTextFullName.text.toString()
        val newUsername = editTextUsername.text.toString()

        val encodedEmail = currentUser?.email.toString().replace(".", "_dot_").replace("@", "_at_")
        val userRef =  databaseRef.child(encodedEmail)

        currentUser?.let { user ->
            // Update display name in Firebase Authentication
            val profileUpdates = UserProfileChangeRequest.Builder()
                .setDisplayName(newFullName)
                .build()

            user.updateProfile(profileUpdates)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Update username in Realtime Database or Firestore
                        userRef.child("fullName").setValue(newFullName)
                        userRef.child("username").setValue(newUsername)

                        Toast.makeText(this, "Changes saved successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to save changes", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun updatePassword() {
        val currentUser = auth.currentUser
        val newPassword = editTextNewPassword.text.toString()

//        val encodedEmail = currentUser?.email.toString().replace(".", "_dot_").replace("@", "_at_")
//        val userRef =  databaseRef.child(encodedEmail)

        currentUser?.let { user ->
            user.updatePassword(newPassword)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }
}
