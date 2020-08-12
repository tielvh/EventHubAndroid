package com.example.android.eventhub.ui.eventdetails

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.domain.Comment
import com.example.android.eventhub.domain.Event
import com.example.android.eventhub.getDatabase
import com.example.android.eventhub.repository.CommentRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.format.DateTimeFormatter

class EventDetailsViewModel(application: Application, e: Event) : ViewModel() {
    private val commentRepository = CommentRepository(getDatabase(application))

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    val eventDateFormatted: LiveData<String> = Transformations.map(event) { event ->
        event.dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }

    val eventTimeFormatted: LiveData<String> = Transformations.map(event) { event ->
        event.dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    private val _comments = MutableLiveData<List<Comment>>()
    val comments: LiveData<List<Comment>>
        get() = _comments

    init {
        _event.value = e
    }

    private fun refreshCommentsFromNetwork() = viewModelScope.launch {
        event.value?.let {
            try {
                commentRepository.refreshComments(it.id)
            } catch (networkError: IOException) {
                // Do not display network error message to avoid cluttering the UI
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onRefreshComments() {
        refreshCommentsFromNetwork()
    }
}