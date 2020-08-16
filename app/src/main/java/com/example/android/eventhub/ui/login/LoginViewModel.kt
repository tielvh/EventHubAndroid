package com.example.android.eventhub.ui.login

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.App
import com.example.android.eventhub.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

class LoginViewModel(application: Application) : ViewModel() {
    @Inject
    lateinit var userRepository: UserRepository

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _usernameErrorVisible = MutableLiveData<Boolean>()
    val usernameErrorVisible: LiveData<Boolean>
        get() = _usernameErrorVisible

    private val _passwordErrorVisible = MutableLiveData<Boolean>()
    val passwordErrorVisible: LiveData<Boolean>
        get() = _passwordErrorVisible

    private val _loginErrorVisible = MutableLiveData<Boolean>()
    val loginErrorVisible: LiveData<Boolean>
        get() = _loginErrorVisible

    private val _loginButtonEnabled = MutableLiveData<Boolean>()
    val loginButtonEnabled: LiveData<Boolean>
        get() = _loginButtonEnabled

    private val _navigateToAccount = MutableLiveData<Boolean>()
    val navigateToAccount: LiveData<Boolean>
        get() = _navigateToAccount

    val username = MutableLiveData<String>()

    val password = MutableLiveData<String>()

    init {
        (application as App).appComponent.inject(this)
        _loginButtonEnabled.value = true
    }

    fun onLogin() {
        hideErrors()
        _loginButtonEnabled.value = false

        val usr = username.value
        val pwd = password.value

        var hasErrors = false

        if (!isUsernameValid(usr)) {
            _usernameErrorVisible.value = true
            hasErrors = true
        }

        if (!isPasswordValid(pwd)) {
            _passwordErrorVisible.value = true
            hasErrors = true
        }

        if (hasErrors) {
            _loginButtonEnabled.value = true
            return
        }

        viewModelScope.launch {
            userRepository.login(usr!!, pwd!!)
            if (userRepository.isLoggedIn()) {
                _navigateToAccount.postValue(true)
            } else {
                _loginErrorVisible.postValue(true)
            }
            _loginButtonEnabled.postValue(true)
        }
    }

    private fun isUsernameValid(username: String?): Boolean {
        return !username.isNullOrBlank()
    }

    private fun isPasswordValid(password: String?): Boolean {
        return !password.isNullOrBlank()
    }

    private fun hideErrors() {
        _loginErrorVisible.value = false
        _usernameErrorVisible.value = false
        _passwordErrorVisible.value = false
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun doneNavigating() {
        _navigateToAccount.value = false
    }
}