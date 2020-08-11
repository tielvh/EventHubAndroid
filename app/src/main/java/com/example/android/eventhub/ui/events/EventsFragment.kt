package com.example.android.eventhub.ui.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.android.eventhub.R
import com.example.android.eventhub.databinding.FragmentEventsBinding

class EventsFragment : Fragment() {
    private lateinit var viewModel: EventsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentEventsBinding>(
            inflater,
            R.layout.fragment_events,
            container,
            false
        )

        val application = requireNotNull(this.activity).application

        val viewModelFactory = EventsViewModelFactory(application)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(EventsViewModel::class.java)

        val adapter = EventListAdapter()
        binding.listEvents.adapter = adapter
        viewModel.events.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.eventNetworkError.observe(viewLifecycleOwner, Observer {
            it?.let {
                if (it) onNetworkError()
            }
        })

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.onRefresh()
    }

    private fun onNetworkError() {
        Toast.makeText(activity, activity?.getString(R.string.network_error), Toast.LENGTH_LONG)
            .show()
    }
}