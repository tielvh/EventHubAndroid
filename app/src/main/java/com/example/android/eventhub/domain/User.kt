package com.example.android.eventhub.domain

data class User(
    val id: Int = 0,
    val username: String,
    val firstName: String,
    val lastName: String,
    val token: String
)