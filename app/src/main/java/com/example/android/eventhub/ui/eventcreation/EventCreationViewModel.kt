package com.example.android.eventhub.ui.eventcreation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.android.eventhub.R
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EventCreationViewModel(private val application: Application) : ViewModel() {
    companion object {
        private val DATE_NOT_SET = LocalDate.MIN
        private val TIME_NOT_SET = LocalTime.MIN
    }

    val eventName = MutableLiveData<String>()

    val eventPlace = MutableLiveData<String>()

    val eventDescription = MutableLiveData<String>()

    private val eventDate = MutableLiveData<LocalDate>()

    private val eventTime = MutableLiveData<LocalTime>()

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

    private val _nameError = MutableLiveData<String>()
    val nameError: LiveData<String>
        get() = _nameError

    private val _placeError = MutableLiveData<String>()
    val placeError: LiveData<String>
        get() = _placeError

    private val _descriptionError = MutableLiveData<String>()
    val descriptionError: LiveData<String>
        get() = _descriptionError

    init {
        eventDate.value = DATE_NOT_SET
        eventTime.value = TIME_NOT_SET
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

    fun setTime(t: LocalTime) {
        _showTimePicker.value = false
        eventTime.value = t
    }

    fun onCreate() {
        if (validate()) {
            
        }
    }

    private fun validate(): Boolean {
        resetErrors()
        var hasError = false

        val name = eventName.value
        if (!isEventNameValid(name)) {
            hasError = true
            _nameError.value = application.getString(R.string.name_error)
        }

        val place = eventPlace.value
        if (!isEventPlaceValid(place)) {
            hasError = true
            _placeError.value = application.getString(R.string.place_error)
        }

        val date = eventDate.value
        if (!isEventDateValid(date)) {
            hasError = true
            _datePickerError.value = application.getString(R.string.date_error)
        }

        val time = eventTime.value
        if (!isEventTimeValid(time)) {
            hasError = true
            _timePickerError.value = application.getString(R.string.time_error)
        }

        val description = eventDescription.value
        if (!isEventDescriptionValid(description)) {
            hasError = true
            _descriptionError.value = application.getString(R.string.description_error)
        }

        return hasError
    }

    private fun resetErrors() {
        _datePickerError.value = ""
        _timePickerError.value = ""
        _nameError.value = ""
        _placeError.value = ""
        _descriptionError.value = ""
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
}