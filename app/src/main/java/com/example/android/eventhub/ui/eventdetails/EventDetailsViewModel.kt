package com.example.android.eventhub.ui.eventdetails

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.App
import com.example.android.eventhub.domain.Comment
import com.example.android.eventhub.domain.Event
import com.example.android.eventhub.network.NetworkPostComment
import com.example.android.eventhub.repository.CommentRepository
import com.example.android.eventhub.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EventDetailsViewModel(application: Application, e: Event) : ViewModel() {
    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var commentRepository: CommentRepository

    val comments: LiveData<List<Comment>>

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

    private val _commentsVisible = MutableLiveData<Boolean>()
    val commentsVisible: LiveData<Boolean>
        get() = _commentsVisible

    private val _commentsOpen = MutableLiveData<Boolean>()
    val commentsOpen: LiveData<Boolean>
        get() = _commentsOpen

    val commentText = MutableLiveData<String>()

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean>
        get() = _networkError

    private val _sending = MutableLiveData<Boolean>()
    val sending: LiveData<Boolean>
        get() = _sending

    val commentTextValid: LiveData<Boolean> = Transformations.map(commentText) {
        it != null && !it.trim().isBlank()
    }

    init {
        (application as App).appComponent.inject(this)
        comments = commentRepository.getComments(e.id)
        _event.value = e
        _commentsVisible.value = userRepository.isLoggedIn()
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

    fun onCommentsToggle() {
        if (_commentsOpen.value != null && _commentsOpen.value!!) {
            // Close comments
            _commentsOpen.value = false
        } else {
            // Open comments
            refreshCommentsFromNetwork()
            _commentsOpen.value = true
        }
    }

    fun onSendComment() {
        _sending.value = true
        val text = commentText.value!!.trim()

        viewModelScope.launch {
            try {
                commentRepository.addComment(NetworkPostComment(event.value!!.id, text))
                commentText.postValue(null)
            } catch (error: IOException) {
                _networkError.value = true
            }

            _sending.postValue(false)
        }
    }
}