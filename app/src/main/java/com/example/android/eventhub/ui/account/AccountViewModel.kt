package com.example.android.eventhub.ui.account

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.domain.User
import com.example.android.eventhub.repository.UserRepository

class AccountViewModel(application: Application) : ViewModel() {
    private val userRepository = UserRepository(application)

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?>
        get() = _user

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    val userFullName: LiveData<String> = Transformations.map(_user) {
        "${it?.firstName} ${it?.lastName}"
    }

    init {
        if (userRepository.isLoggedIn()) _user.value = userRepository.getUser()
        else _user.value = null
    }

    fun onLogout() {
        userRepository.logout()
        _navigateBack.value = true
    }

    fun doneNavigating() {
        _navigateBack.value = false
    }
}