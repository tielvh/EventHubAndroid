package com.example.android.eventhub.network

import com.example.android.eventhub.domain.Comment
import com.example.android.eventhub.domain.Event
import com.example.android.eventhub.domain.User
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

fun List<NetworkEvent>.asEvents(): List<Event> = this.map { it.asEvent() }

@JsonClass(generateAdapter = true)
data class NetworkComment(
    val fullUserName: String,
    val timeStamp: LocalDateTime,
    val content: String
)

fun NetworkComment.asComment(eventId: Int): Comment {
    return Comment(user = fullUserName, timeStamp = timeStamp, content = content, eventId = eventId)
}

fun List<NetworkComment>.asComments(eventId: Int): List<Comment> =
    this.map { it.asComment(eventId) }

@JsonClass(generateAdapter = true)
data class NetworkCredentials(
    val userName: String,
    val password: String
)

@JsonClass(generateAdapter = true)
data class NetworkUser(
    val userId: Int,
    val username: String,
    val firstName: String,
    val lastName: String,
    val token: String
)

fun NetworkUser.asUser(): User {
    return User(userId, username, firstName, lastName, token)
}