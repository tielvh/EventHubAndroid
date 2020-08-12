package com.example.android.eventhub.ui.eventdetails

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.android.eventhub.R
import com.example.android.eventhub.databinding.FragmentEventDetailsBinding

class EventDetailsFragment : Fragment() {
    private lateinit var viewModel: EventDetailsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentEventDetailsBinding>(
            inflater,
            R.layout.fragment_event_details,
            container,
            false
        )

        val application = requireNotNull(this.activity).application

        val args = EventDetailsFragmentArgs.fromBundle(arguments!!)
        val event = args.event

        val viewModelFactory = EventDetailsViewModelFactory(application, event)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EventDetailsViewModel::class.java)

        return binding.root
    }
}