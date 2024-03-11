package com.example.traveldiary.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.traveldiary.R
import com.example.traveldiary.databinding.ActivityHomeBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseRef : DatabaseReference

    private lateinit var binding : ActivityHomeBinding

    private lateinit var username : TextView
    private lateinit var email : TextView

    private lateinit var locationButton : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_home)

        val homeLayout : DrawerLayout = findViewById(R.id.homeLayout)
        val navView : NavigationView = findViewById(R.id.home_nav_view)

        toggle = ActionBarDrawerToggle(this, homeLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        homeLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().getReference("Users")

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.menu_diary -> {
                    val intent = Intent(this, DiaryActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_settings -> {
                    val intent = Intent(this, SettingsActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_profile -> {
                    val intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                }
                R.id.menu_logOut -> {
//                    Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show()
                    signOut()
                }
            }

            true

        }

        updateUserInformation()

        locationButton = findViewById(R.id.buttonLocation)
        locationButton.setOnClickListener {
            Toast.makeText(this, "Welcome to map", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MapsActivity::class.java))
//            finish()
        }

    }

    private fun updateUserInformation() {
        val user = auth.currentUser

        if (user != null) {
//            Toast.makeText(this, "Hey user ${user.email}", Toast.LENGTH_SHORT).show()
            val copy_email = user.email
            val encodedEmail = copy_email.toString().replace(".", "_dot_").replace("@", "_at_")
            val userRef =  databaseRef.child(encodedEmail)
            userRef.addValueEventListener(object: ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val username = snapshot.child("username").value.toString()
                        val email = snapshot.child("email").value.toString()
                        val navHeaderBinding = binding.homeNavView.getHeaderView(0)
                        val usernameTextView: TextView = navHeaderBinding.findViewById(R.id.nav_headerTextViewUserName)
                        val emailTextView: TextView = navHeaderBinding.findViewById(R.id.nav_headerTextViewEmail)

                        val unameTextView : TextView = findViewById(R.id.usernameTextView)
                        val eTextView : TextView = findViewById(R.id.emailTextView)

                        usernameTextView.text = username
                        unameTextView.text = username
                        emailTextView.text = email
                        eTextView.text = email

                        // Create and show a Toast message on the main (UI) thread
//                        val mainHandler = Handler(Looper.getMainLooper())
//                        val myRunnable = Runnable {
//                            Toast.makeText(applicationContext, "Data loaded successfully", Toast.LENGTH_SHORT).show()
//                        }
//                        mainHandler.post(myRunnable)

                    } else {
                        val mainHandler = Handler(Looper.getMainLooper())
                        val myRunnable = Runnable {
                            Toast.makeText(applicationContext, "Error in data loading", Toast.LENGTH_SHORT).show()
                        }
                        mainHandler.post(myRunnable)
//                        println("User data does not exist")
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }
            })
        } else {
//            println("User not authenticated")
            Toast.makeText(this, "Error: No such User", Toast.LENGTH_SHORT).show()
        }
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return when (item.itemId) {
            R.id.menu_logOut -> {
                // Handle log out menu item click
                signOut()
                true
            }
            R.id.menu_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

        return super.onOptionsItemSelected(item)
    }

    private fun signOut() {
        // Optionally clear any locally stored user data
        clearSharedPreferences()

        // Redirect to sign-in activity
        val intent = Intent(this, SignInActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        auth.signOut()
        startActivity(intent)
        finish()
    }

    // Example method to clear locally stored user data
    private fun clearSharedPreferences() {
        val sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        return when (item.itemId) {
//            R.id.menu_logOut -> {
//                // Handle log out menu item click
//                signOut()
//                true
//            }
//            R.id.menu_settings -> {
//                val intent = Intent(this, SettingsActivity::class.java)
//                startActivity(intent)
//                finish()
//                true
//            }
//            else -> super.onOptionsItemSelected(item)
//        }
//    }

}