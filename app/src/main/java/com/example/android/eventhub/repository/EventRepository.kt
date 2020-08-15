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
            val req = MultipartBody.Builder()
                .addFormDataPart("name", event.name)
                .addFormDataPart("place", event.place)
                .addFormDataPart("description", event.description)
                .addFormDataPart(
                    "dateTime", event.dateTime.format(
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME
                    )
                )
                .addFormDataPart(
                    "image", event.image.name, RequestBody.create(
                        MediaType.parse("image/*"), event.image
                    )
                ).build()

            val retEvent = EventApi.retrofitService.postEventAsync(req).await()
            database.eventDao.insert(retEvent.asEvent())
        }
    }
}