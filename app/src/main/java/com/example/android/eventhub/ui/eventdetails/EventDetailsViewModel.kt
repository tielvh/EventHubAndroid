package com.example.android.eventhub.ui.eventdetails

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.domain.Event

class EventDetailsViewModel(application: Application, e: Event) : ViewModel() {
    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    init {
        _event.value = e
    }
}