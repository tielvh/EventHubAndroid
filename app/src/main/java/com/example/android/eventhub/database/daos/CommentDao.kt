package com.example.android.eventhub.database.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.eventhub.domain.Comment

@Dao
interface CommentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(comments: List<Comment>)

    @Query("SELECT * FROM Comment WHERE eventId = :eventId ORDER BY timeStamp DESC")
    fun getMany(eventId: Int): LiveData<List<Comment>>

    @Query("DELETE FROM Comment where eventId = :eventId")
    fun clear(eventId: Int)
}