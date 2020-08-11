package com.example.android.eventhub.network

import com.example.android.eventhub.domain.Event
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

fun NetworkEvent.asEvent(): Event {
    return Event(eventId, name, dateTime, place, description, imgPath)
}