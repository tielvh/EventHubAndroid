package com.example.android.eventhub.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : ViewModel() {
    private val userRepository = UserRepository(application)

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _errorVisible = MutableLiveData<Boolean>()
    val errorVisible: LiveData<Boolean>
        get() = _errorVisible

    val username = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    fun onLogin() {
        _errorVisible.value = false

        val usr = username.value
        val pwd = password.value

        if (usr != null && pwd != null) {
            viewModelScope.launch {
                userRepository.login(usr, pwd)
                if (userRepository.isLoggedIn()) {
                    // TODO: navigate
                } else {
                    _errorVisible.value = true
                }
            }
        }
    }
}