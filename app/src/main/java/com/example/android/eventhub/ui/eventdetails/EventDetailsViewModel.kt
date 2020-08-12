package com.example.android.eventhub.ui.eventdetails

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.domain.Event
import java.time.format.DateTimeFormatter

class EventDetailsViewModel(application: Application, e: Event) : ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    val eventDateFormatted = Transformations.map(event) { event ->
        event.dateTime.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
    }

    val eventTimeFormatted = Transformations.map(event) { event ->
        event.dateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    init {
        _event.value = e
    }
}