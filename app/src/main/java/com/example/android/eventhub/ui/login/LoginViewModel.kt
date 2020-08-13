package com.example.android.eventhub.ui.login

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.repository.UserRepository

class LoginViewModel(application: Application) : ViewModel() {
    private val userRepository = UserRepository(application)

    private val isAuthenticating = MutableLiveData<Boolean>()

    private val isValid = MutableLiveData<Boolean>()

    val isLoginButtonEnabled = MediatorLiveData<Boolean>()

    val username = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    init {
        isLoginButtonEnabled.addSource(isAuthenticating) {
            it || !isValid.value!!
        }

        isLoginButtonEnabled.addSource(isValid) {
            !it || isAuthenticating.value!!
        }

        isValid.value = false
        isAuthenticating.value = false
    }
}