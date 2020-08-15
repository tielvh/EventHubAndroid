package com.example.android.eventhub.ui.eventdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
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

        val args = EventDetailsFragmentArgs.fromBundle(requireArguments())
        val event = args.event

        val viewModelFactory = EventDetailsViewModelFactory(application, event)
        viewModel =
            ViewModelProviders.of(this, viewModelFactory).get(EventDetailsViewModel::class.java)

        val adapter = CommentListAdapter()
        binding.listComments.adapter = adapter
        viewModel.comments.observe(viewLifecycleOwner, Observer {
            it?.let { adapter.submitList(it) }
        })

        viewModel.networkError.observe(viewLifecycleOwner, Observer {
            it?.let {
                onNetworkError()
            }
        })

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    private fun onNetworkError() {
        Toast.makeText(activity, R.string.network_error, Toast.LENGTH_LONG).show()
    }
}