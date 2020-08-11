package com.example.android.eventhub.ui.events

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.getDatabase
import com.example.android.eventhub.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class EventsViewModel(application: Application): ViewModel() {
    private val eventRepository = EventRepository(getDatabase(application))

    val events = eventRepository.events

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _eventNetworkError = MutableLiveData<Boolean>()
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    init {
        refreshDataFromNetwork()
    }

    private fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
            eventRepository.refreshEvents()
            _eventNetworkError.value = false
        } catch (networkError: IOException) {
            _eventNetworkError.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onRefresh() {
        refreshDataFromNetwork()
    }
}