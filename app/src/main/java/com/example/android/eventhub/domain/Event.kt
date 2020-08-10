package com.example.android.eventhub.domain

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity
data class Event(
    @PrimaryKey(autoGenerate = true)
    val eventId: Int = 0,
    val name: String,
    val dateTime: LocalDateTime,
    val place: String,
    val description: String,
    val imgPath: String
)