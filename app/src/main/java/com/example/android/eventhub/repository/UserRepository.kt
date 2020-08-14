package com.example.android.eventhub.repository

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.auth0.android.jwt.JWT
import com.example.android.eventhub.R
import com.example.android.eventhub.domain.User
import com.example.android.eventhub.network.EventApi
import com.example.android.eventhub.network.NetworkCredentials
import com.example.android.eventhub.network.asUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.lang.IllegalStateException
import java.util.*

class UserRepository(private val application: Application) {
    private var preferences: SharedPreferences = application.getSharedPreferences(
        application.getString(R.string.shared_preferences_name),
        Context.MODE_PRIVATE
    )

    init {
        val token = getToken()
        if (token != null && !isTokenValid(token)) logout()
    }

    fun isLoggedIn(): Boolean {
        val token = getToken()
        return token != null && isTokenValid(token)
    }

    private fun getToken(): String? {
        return preferences.getString(application.getString(R.string.saved_user_token_key), null)
    }

    private fun getUserName(): String? {
        return preferences.getString(application.getString(R.string.saved_user_username_key), null)
    }

    private fun getFirstName(): String? {
        return preferences.getString(
            application.getString(R.string.saved_user_first_name_key),
            null
        )
    }

    private fun getLastName(): String? {
        return preferences.getString(application.getString(R.string.saved_user_last_name_key), null)
    }

    private fun getUserId(): Int? {
        val userId = preferences.getInt(application.getString(R.string.saved_user_user_id_key), 0)
        if (userId == 0) return null
        return userId
    }

    fun getUser(): User {
        if (!isLoggedIn()) throw IllegalStateException("No user is logged in")

        return User(getUserId()!!, getUserName()!!, getFirstName()!!, getLastName()!!, getToken()!!)
    }

    private fun isTokenValid(token: String): Boolean {
        val jwt = JWT(token)
        return jwt.expiresAt != null && jwt.expiresAt!! > Calendar.getInstance().time
    }

    private fun getEditor(): SharedPreferences.Editor {
        return preferences.edit()
    }

    suspend fun login(username: String, password: String) {
        logout()
        withContext(Dispatchers.IO) {
            try {
                val user =
                    EventApi.retrofitService.authenticateAsync(
                        NetworkCredentials(
                            username,
                            password
                        )
                    )
                        .await().asUser()
                saveUser(user)
            } catch (error: HttpException) {
                // Do nothing
            }
        }
    }

    private fun saveUser(user: User) {
        logout()
        getEditor().putString(application.getString(R.string.saved_user_token_key), user.token)
            .putString(application.getString(R.string.saved_user_first_name_key), user.firstName)
            .putString(application.getString(R.string.saved_user_last_name_key), user.lastName)
            .putString(application.getString(R.string.saved_user_username_key), user.username)
            .putInt(application.getString(R.string.saved_user_user_id_key), user.id)
            .apply()
    }

    fun logout() {
        getEditor().clear().apply()
    }
}