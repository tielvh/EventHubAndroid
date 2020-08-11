package com.example.android.eventhub.database

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converters {
    @TypeConverter
    fun fromTimestamp(timestamp: String): LocalDateTime {
        return LocalDateTime.parse(timestamp)
    }

    @TypeConverter
    fun toTimestamp(dateTime: LocalDateTime): String {
        return dateTime.toString()
    }
}