package com.example.traveldiary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class HomeActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle

    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

//        updateUserInformation(user)

        val homeLayout : DrawerLayout = findViewById(R.id.homeLayout)
        val navView : NavigationView = findViewById(R.id.home_nav_view)

        toggle = ActionBarDrawerToggle(this, homeLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
        homeLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
//                R.id.menu_diary -> Toast.makeText(this, "Diary", Toast.LENGTH_SHORT).show()
//                R.id.menu_settings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
//                R.id.menu_profile -> Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                R.id.menu_logOut -> Toast.makeText(this, "Logging out", Toast.LENGTH_SHORT).show()
            }

            true

        }

    }

    private fun updateUserInformation(user: FirebaseUser?) {
        if (user != null) {
            // Get the TextView elements from the nav_header layout
            val userNameTextView: TextView = findViewById(R.id.nav_headerTextViewUserName)
            val userEmailTextView: TextView = findViewById(R.id.nav_headerTextViewEmail)

            // Check for nullability before updating TextViews
//            user.displayName?.let { userNameTextView.text = it }
//            userEmailTextView.text = user.email
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (toggle.onOptionsItemSelected(item)) {
            return true
        }

        return super.onOptionsItemSelected(item)
    }

}