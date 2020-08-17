package com.example.android.eventhub

import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.android.eventhub.database.EventDatabase
import com.example.android.eventhub.database.daos.EventDao
import com.example.android.eventhub.domain.Event
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class EventDatabaseTest {
    private lateinit var eventDao: EventDao
    private lateinit var db: EventDatabase

    @Before
    fun createDb() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        db = Room.inMemoryDatabaseBuilder(context, EventDatabase::class.java)
            .allowMainThreadQueries().build()
        eventDao = db.eventDao
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun testInsertAndGetEvent() {
        val event =
            Event(1, "Test", LocalDateTime.of(2020, 8, 11, 12, 0, 0), "here", "test", "test")
        eventDao.insert(event)
        val thisEvent = eventDao.getOne(1)
        assertEquals(event.name, thisEvent?.name)
    }
}