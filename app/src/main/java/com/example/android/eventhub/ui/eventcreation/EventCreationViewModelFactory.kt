package com.example.android.eventhub.ui.eventcreation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
class EventCreationViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EventCreationViewModel::class.java)) {
            return EventCreationViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}