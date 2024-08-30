package com.example.noteapp.ui.activity

import android.os.Bundle
import android.text.TextUtils
import android.text.TextUtils.replace
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.noteapp.R
import com.example.noteapp.databinding.ActivityMainBinding
import com.example.noteapp.ui.fragments.note.NoteAppFragment
import com.example.noteapp.ui.fragments.onboard.onBoardFragment
import com.example.noteapp.utils.PreferenceHelper
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var sharedPreferences: PreferenceHelper
    private lateinit var navController: NavController
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        auth = Firebase.auth
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        sharedPreferences = PreferenceHelper()
        sharedPreferences.unit(this)

        if (!sharedPreferences.onBoardShow) {
            navController.navigate(R.id.onBoardFragment)
        } else {
            Enter()
        }
    }

    private fun Enter() {
        val currentUser = auth.currentUser
        if (!sharedPreferences.onBoardShow) {
            navController.navigate(R.id.onBoardFragment)
            sharedPreferences.onBoardShow = true
        } else if (currentUser == null) {
            navController.navigate(R.id.signInFragment)
        } else {
            navController.navigate(R.id.noteAppFragment)
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.noteAppFragment) {
            finish()
        } else {
            super.onBackPressed()
        }
    }
}