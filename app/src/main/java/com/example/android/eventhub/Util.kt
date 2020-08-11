package com.example.android.eventhub

import android.app.Application
import com.example.android.eventhub.database.EventDatabase
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun getDatabase(application: Application): EventDatabase {
    return EventDatabase.getInstance(application)
}

class LocalDateTimeAdapter {
    @ToJson
    fun toJson(value: LocalDateTime): String {
        return FORMATTER.format(value)
    }

    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return FORMATTER.parse(value, LocalDateTime::from)
    }

    companion object {
        private val FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }
}