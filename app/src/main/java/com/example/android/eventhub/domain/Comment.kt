package com.example.android.eventhub.domain

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val user: String,
    val content: String
)