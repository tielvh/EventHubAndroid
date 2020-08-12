package com.example.android.eventhub.ui.eventdetails

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.android.eventhub.domain.Event
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class EventDetailsViewModelFactory(private val application: Application, private val event: Event) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventDetailsViewModel::class.java)) {
            return EventDetailsViewModel(application, event) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}