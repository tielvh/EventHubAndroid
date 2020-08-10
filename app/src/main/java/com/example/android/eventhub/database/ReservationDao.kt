package com.example.android.eventhub.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.eventhub.domain.Event

@Dao
interface ReservationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(events: List<Event>)

    @Query("SELECT * FROM Event ORDER BY eventId DESC")
    fun getAll(): LiveData<List<Event>>

    @Query("DELETE FROM Event")
    fun clear()
}