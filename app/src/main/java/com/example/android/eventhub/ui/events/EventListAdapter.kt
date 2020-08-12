package com.example.android.eventhub.ui.events

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.eventhub.OnItemClickListener
import com.example.android.eventhub.databinding.ListItemEventBinding
import com.example.android.eventhub.domain.Event
import com.example.android.eventhub.ui.events.EventListAdapter.ViewHolder.Companion.from
import java.time.format.DateTimeFormatter

class EventListAdapter(val listener: OnItemClickListener<Event>) : ListAdapter<Event, EventListAdapter.ViewHolder>(EventDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, listener)
    }

    class ViewHolder private constructor(val binding: ListItemEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Event, listener: OnItemClickListener<Event>) {
            binding.event = item
            itemView.setOnClickListener {
                listener.onItemClick(item)
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding =ListItemEventBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class EventDiffCallback : DiffUtil.ItemCallback<Event>() {
    override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
        return oldItem == newItem
    }
}

@BindingAdapter("eventDateTime")
fun TextView.setEventDateTime(item: Event) {
    text = item.dateTime.format(DateTimeFormatter.ofPattern("dd MMM yy HH:mm"))
}