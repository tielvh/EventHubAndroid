package com.example.android.eventhub.repository

import androidx.lifecycle.LiveData
import com.example.android.eventhub.database.EventDatabase
import com.example.android.eventhub.domain.Comment
import com.example.android.eventhub.network.EventApi
import com.example.android.eventhub.network.NetworkPostComment
import com.example.android.eventhub.network.asComments
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MultipartBody

class CommentRepository(private val database: EventDatabase) {
    fun getComments(eventId: Int): LiveData<List<Comment>> {
        return database.commentDao.getMany(eventId)
    }

    suspend fun refreshComments(eventId: Int) {
        withContext(Dispatchers.IO) {
            val comments = EventApi.retrofitService.getCommentsAsync(eventId).await()
            database.commentDao.clear(eventId)
            database.commentDao.insert(comments.asComments(eventId))
        }
    }

    suspend fun addComment(comment: NetworkPostComment) {
        withContext(Dispatchers.IO) {
            val eventIdPart =
                MultipartBody.Part.createFormData("eventId", comment.eventId.toString())
            val contentPart = MultipartBody.Part.createFormData("content", comment.content)

            EventApi.retrofitService.postCommentAsync(eventIdPart, contentPart).execute()

            refreshComments(comment.eventId)
        }
    }
}