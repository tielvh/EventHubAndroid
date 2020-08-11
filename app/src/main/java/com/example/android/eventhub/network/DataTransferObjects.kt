package com.example.android.eventhub.network

import com.squareup.moshi.JsonClass
import java.time.LocalDateTime

@JsonClass(generateAdapter = true)
data class NetworkEvent(
    val eventId: Int,
    val name: String,
    val dateTime: LocalDateTime,
    val place: String,
    val description: String,
    val imgPath: String
)