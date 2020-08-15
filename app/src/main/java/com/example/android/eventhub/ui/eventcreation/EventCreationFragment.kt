package com.example.android.eventhub.ui.eventcreation

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.eventhub.R
import com.example.android.eventhub.databinding.FragmentEventCreationBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import java.time.LocalDate
import java.time.LocalTime

class EventCreationFragment : Fragment() {
    private lateinit var viewModel: EventCreationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentEventCreationBinding>(
            inflater,
            R.layout.fragment_event_creation,
            container,
            false
        )

        val application = requireNotNull(activity).application

        val viewModelFactory = EventCreationViewModelFactory(application)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(EventCreationViewModel::class.java)

        viewModel.showDatePicker.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) showDatePicker()
            }
        })

        viewModel.showTimePicker.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) showTimePicker()
            }
        })

        viewModel.showImagePicker.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) showImagePicker()
            }
        })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }

    private fun showDatePicker() {
        val now = LocalDate.now()

        DatePickerDialog(
            requireContext(),
            DatePickerDialog.OnDateSetListener { _, year, month, day ->
                val date = LocalDate.of(year, month + 1, day)
                viewModel.setDate(date)
            },
            now.year,
            now.monthValue - 1,
            now.dayOfMonth
        ).show()
    }

    private fun showTimePicker() {
        val noon = LocalTime.NOON

        TimePickerDialog(
            requireContext(),
            TimePickerDialog.OnTimeSetListener { _, hours, minutes ->
                val time = LocalTime.of(hours, minutes)
                viewModel.setTime(time)
            },
            noon.hour,
            noon.minute,
            true
        ).show()
    }

    private fun showImagePicker() {
        ImagePicker.with(this)
            .galleryMimeTypes(
                mimeTypes = arrayOf(
                    "image/png",
                    "image/jpg",
                    "image/jpeg"
                )
            )
            .start { resultCode, data ->
                if (resultCode == Activity.RESULT_OK) {
                    val file = ImagePicker.getFile(data)
                    if (file != null) viewModel.setImage(file)
                }
            }
    }
}