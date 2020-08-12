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
import androidx.navigation.fragment.findNavController
import com.example.android.eventhub.OnItemClickListener
import com.example.android.eventhub.R
import com.example.android.eventhub.databinding.FragmentEventsBinding
import com.example.android.eventhub.domain.Event

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

        val listener = object : OnItemClickListener<Event> {
            override fun onItemClick(item: Event) {
                viewModel.onNavigateToDetails(item)
            }
        }
        val adapter = EventListAdapter(listener)
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

        viewModel.navigateToDetails.observe(viewLifecycleOwner, Observer {
            it?.let {
                this.findNavController()
                    .navigate(EventsFragmentDirections.actionEventsFragmentToEventDetailsFragment(it))
                viewModel.doneNavigating()
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