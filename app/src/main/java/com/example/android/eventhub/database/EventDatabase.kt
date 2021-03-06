package com.example.android.eventhub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.android.eventhub.database.daos.CommentDao
import com.example.android.eventhub.database.daos.EventDao
import com.example.android.eventhub.domain.Comment
import com.example.android.eventhub.domain.Event

@Database(entities = [Event::class, Comment::class], version = 2, exportSchema = false)
@TypeConverters(Converters::class)
abstract class EventDatabase : RoomDatabase() {
    abstract val eventDao: EventDao

    abstract val commentDao: CommentDao

    companion object {
        @Volatile
        private var INSTANCE: EventDatabase? = null

        fun getInstance(context: Context): EventDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        EventDatabase::class.java,
                        "event_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}