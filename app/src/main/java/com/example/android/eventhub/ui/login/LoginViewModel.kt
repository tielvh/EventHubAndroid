package com.example.android.eventhub.ui.login

import android.app.Activity
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.repository.UserRepository

class LoginViewModel(activity: Activity): ViewModel() {
    private val userRepository = UserRepository(activity)
}