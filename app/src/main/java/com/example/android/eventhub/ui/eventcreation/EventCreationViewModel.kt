package com.example.android.eventhub.ui.eventcreation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.App
import com.example.android.eventhub.R
import com.example.android.eventhub.network.NetworkPostEvent
import com.example.android.eventhub.repository.EventRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class EventCreationViewModel(private val application: Application) : ViewModel() {
    companion object {
        private val DATE_NOT_SET = LocalDate.MIN
        private val TIME_NOT_SET = LocalTime.MIN
    }

    @Inject
    lateinit var eventRepository: EventRepository

    private val viewModelJob = SupervisorJob()

    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    val eventName = MutableLiveData<String>()

    val eventPlace = MutableLiveData<String>()

    val eventDescription = MutableLiveData<String>()

    private val eventDate = MutableLiveData<LocalDate>()

    private val eventTime = MutableLiveData<LocalTime>()

    private val _eventImage = MutableLiveData<File>()
    val eventImage: LiveData<File>
        get() = _eventImage

    val eventImageName: LiveData<String> = Transformations.map(eventImage) {
        it.name
    }

    val dateButtonText: LiveData<String> = Transformations.map(eventDate) {
        if (it == DATE_NOT_SET) application.getString(R.string.date)
        else it.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    val timeButtonText: LiveData<String> = Transformations.map(eventTime) {
        if (it == TIME_NOT_SET) application.getString(R.string.time)
        else it.format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    private val _showDatePicker = MutableLiveData<Boolean>()
    val showDatePicker: LiveData<Boolean>
        get() = _showDatePicker

    private val _datePickerError = MutableLiveData<String>()
    val datePickerError: LiveData<String>
        get() = _datePickerError

    private val _timePickerError = MutableLiveData<String>()
    val timePickerError: LiveData<String>
        get() = _timePickerError

    private val _showTimePicker = MutableLiveData<Boolean>()
    val showTimePicker: LiveData<Boolean>
        get() = _showTimePicker

    private val _showImagePicker = MutableLiveData<Boolean>()
    val showImagePicker
        get() = _showImagePicker

    private val _nameError = MutableLiveData<String>()
    val nameError: LiveData<String>
        get() = _nameError

    private val _placeError = MutableLiveData<String>()
    val placeError: LiveData<String>
        get() = _placeError

    private val _descriptionError = MutableLiveData<String>()
    val descriptionError: LiveData<String>
        get() = _descriptionError

    private val _imageError = MutableLiveData<String>()
    val imageError: LiveData<String>
        get() = _imageError

    private val _createButtonEnabled = MutableLiveData<Boolean>()
    val createButtonEnabled: LiveData<Boolean>
        get() = _createButtonEnabled

    private val _navigateBack = MutableLiveData<Boolean>()
    val navigateBack: LiveData<Boolean>
        get() = _navigateBack

    private val _networkError = MutableLiveData<Boolean>()
    val networkError: LiveData<Boolean>
        get() = _networkError

    init {
        (application as App).appComponent.inject(this)
        eventDate.value = DATE_NOT_SET
        eventTime.value = TIME_NOT_SET
        _createButtonEnabled.value = true
    }

    fun onShowDatePicker() {
        _showDatePicker.value = true
    }

    fun setDate(d: LocalDate) {
        _showDatePicker.value = false
        eventDate.value = d
    }

    fun onShowTimePicker() {
        _showTimePicker.value = true
    }

    fun onShowImagePicker() {
        _showImagePicker.value = true
    }

    fun setTime(t: LocalTime) {
        _showTimePicker.value = false
        eventTime.value = t
    }

    fun setImage(image: File) {
        _showImagePicker.value = false
        _eventImage.value = image
    }

    fun onCreate() = viewModelScope.launch {
        _createButtonEnabled.postValue(false)

        if (validate()) {
            val event = NetworkPostEvent(
                eventName.value!!,
                LocalDateTime.of(eventDate.value!!, eventTime.value!!),
                eventPlace.value!!,
                eventDescription.value!!.trim(),
                eventImage.value!!
            )

            try {
                eventRepository.addEvent(event)
            } catch (error: IOException) {
                _networkError.value = true
            }

            _navigateBack.value = true
        }

        _createButtonEnabled.postValue(true)
    }

    private fun validate(): Boolean {
        resetErrors()
        var hasError = false

        val name = eventName.value
        if (!isEventNameValid(name)) {
            hasError = true
            _nameError.postValue(application.getString(R.string.name_error))
        }

        val place = eventPlace.value
        if (!isEventPlaceValid(place)) {
            hasError = true
            _placeError.postValue(application.getString(R.string.place_error))
        }

        val date = eventDate.value
        if (!isEventDateValid(date)) {
            hasError = true
            _datePickerError.postValue(application.getString(R.string.date_error))
        }

        val time = eventTime.value
        if (!isEventTimeValid(time)) {
            hasError = true
            _timePickerError.postValue(application.getString(R.string.time_error))
        }

        val description = eventDescription.value
        description?.trim()
        if (!isEventDescriptionValid(description)) {
            hasError = true
            _descriptionError.postValue(application.getString(R.string.description_error))
        }

        val image = eventImage.value
        if (!isEventImageValid(image)) {
            hasError = true
            _imageError.postValue(application.getString(R.string.image_error))
        }

        return !hasError
    }

    private fun resetErrors() {
        _datePickerError.value = ""
        _timePickerError.value = ""
        _nameError.value = ""
        _placeError.value = ""
        _descriptionError.value = ""
        _imageError.value = ""
    }

    private fun isEventNameValid(name: String?): Boolean {
        return !name.isNullOrBlank()
    }

    private fun isEventPlaceValid(place: String?): Boolean {
        return !place.isNullOrBlank()
    }

    private fun isEventDescriptionValid(description: String?): Boolean {
        return !description.isNullOrBlank()
    }

    private fun isEventDateValid(date: LocalDate?): Boolean {
        return date != null && date.isAfter(LocalDate.now())
    }

    private fun isEventTimeValid(time: LocalTime?): Boolean {
        return time != null && time != TIME_NOT_SET
    }

    private fun isEventImageValid(image: File?): Boolean {
        return image != null && image.exists()
    }

    fun doneNavigating() {
        _navigateBack.value = false
    }
}