package com.example.android.eventhub.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.android.eventhub.R
import com.example.android.eventhub.repository.UserRepository

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_default, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.title == getString(R.string.login)) {
            val userRepository = UserRepository(application)
            val navController = this.findNavController(R.id.navHostFragment)
            if (userRepository.isLoggedIn()) {
                // TODO: navigate to account fragment
            } else {
                navController.navigate(R.id.loginFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}