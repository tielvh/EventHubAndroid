package com.example.android.eventhub.ui.eventcreation

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class EventCreationViewModel(application: Application) : ViewModel() {
    val eventName = MutableLiveData<String>()

    val eventPlace = MutableLiveData<String>()

    val eventDescription = MutableLiveData<String>()
}