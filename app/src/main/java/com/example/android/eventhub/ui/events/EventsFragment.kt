package com.example.android.eventhub.ui.events

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.android.eventhub.R
import com.example.android.eventhub.databinding.FragmentEventsBinding

class EventsFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =  DataBindingUtil.inflate<FragmentEventsBinding>(inflater, R.layout.fragment_events, container, false)

        return binding.root
    }
}