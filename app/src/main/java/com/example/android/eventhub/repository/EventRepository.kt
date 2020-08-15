package com.example.android.eventhub.repository

import com.example.android.eventhub.database.EventDatabase
import com.example.android.eventhub.network.EventApi
import com.example.android.eventhub.network.NetworkPostEvent
import com.example.android.eventhub.network.asEvent
import com.example.android.eventhub.network.asEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.time.format.DateTimeFormatter

class EventRepository(private val database: EventDatabase) {
    val events = database.eventDao.getAll()

    suspend fun refreshEvents() {
        withContext(Dispatchers.IO) {
            val events = EventApi.retrofitService.getAllEventsAsync().await()
            database.eventDao.clear()
            database.eventDao.insert(events.asEvents())
        }
    }

    suspend fun addEvent(event: NetworkPostEvent) {
        withContext(Dispatchers.IO) {
            val namePart = MultipartBody.Part.createFormData("name", event.name)
            val placePart = MultipartBody.Part.createFormData("place", event.place)
            val dateTimePart = MultipartBody.Part.createFormData(
                "dateTime",
                event.dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
            val descriptionPart =
                MultipartBody.Part.createFormData("description", event.description)
            val imagePart = MultipartBody.Part.createFormData(
                "image", event.image.name, RequestBody.create(
                    MediaType.parse("image/*"), event.image
                )
            )

            val retEvent = EventApi.retrofitService.postEventAsync(
                namePart,
                placePart,
                dateTimePart,
                descriptionPart,
                imagePart
            ).await()

            database.eventDao.insert(retEvent.asEvent())
        }
    }
}