package com.example.android.eventhub.ui.events

import android.app.Application
import androidx.lifecycle.*
import com.example.android.eventhub.domain.Event
import com.example.android.eventhub.getDatabase
import com.example.android.eventhub.repository.EventRepository
import com.example.android.eventhub.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class EventsViewModel(application: Application) : ViewModel(), LifecycleObserver {
    private val eventRepository = EventRepository(getDatabase(application))

    private val userRepository = UserRepository(application)

    val events = eventRepository.events

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val _eventNetworkError = MutableLiveData<Boolean>()
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    private val _navigateToDetails = MutableLiveData<Event>()
    val navigateToDetails: LiveData<Event>
        get() = _navigateToDetails

    private val _navigateToEventCreation = MutableLiveData<Boolean>()
    val navigateToEventCreation: LiveData<Boolean>
        get() = _navigateToEventCreation

    private val _addEventButtonVisible = MutableLiveData<Boolean>()
    val addEventButtonVisible: LiveData<Boolean>
        get() = _addEventButtonVisible

    init {
        refreshDataFromNetwork()
        _addEventButtonVisible.value = userRepository.isLoggedIn()
    }

    private fun refreshDataFromNetwork() = viewModelScope.launch {
        try {
            eventRepository.refreshEvents()
            _eventNetworkError.value = false
        } catch (networkError: IOException) {
            _eventNetworkError.value = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    private fun onLifeCycleResume() {
        _addEventButtonVisible.value = userRepository.isLoggedIn()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onRefresh() {
        refreshDataFromNetwork()
    }

    fun onNavigateToDetails(event: Event) {
        _navigateToDetails.value = event
    }

    fun doneNavigating() {
        _navigateToDetails.value = null
        _navigateToEventCreation.value = null
    }

    fun onCreateEvent() {
        _navigateToEventCreation.value = true
    }
}