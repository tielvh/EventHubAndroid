package com.example.android.eventhub.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.eventhub.domain.Event

@Dao
interface EventDao {
    @Insert()
    fun insert(event: Event)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(events: List<Event>)

    @Query("SELECT * FROM Event ORDER BY id DESC")
    fun getAll(): LiveData<List<Event>>

    @Query("SELECT * FROM Event WHERE id = :id")
    fun getOne(id: Int): Event?

    @Query("DELETE FROM Event")
    fun clear()
}