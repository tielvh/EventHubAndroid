package com.example.android.eventhub.repository

import com.example.android.eventhub.database.EventDatabase
import com.example.android.eventhub.network.EventApi
import com.example.android.eventhub.network.asEvents
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class EventRepository(private val database: EventDatabase) {
    val events = database.eventDao.getAll()

    suspend fun refreshEvents() {
        withContext(Dispatchers.IO) {
            val events = EventApi.retrofitService.getAllEventsAsync().await()
            database.eventDao.clear()
            database.eventDao.insert(events.asEvents())
        }
    }

    suspend fun addEvent() {
        withContext(Dispatchers.IO) {
            // TODO: send API request
        }
    }
}