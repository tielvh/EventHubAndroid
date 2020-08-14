package com.example.android.eventhub.ui.eventcreation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.android.eventhub.R
import com.example.android.eventhub.databinding.FragmentEventCreationBinding

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

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}