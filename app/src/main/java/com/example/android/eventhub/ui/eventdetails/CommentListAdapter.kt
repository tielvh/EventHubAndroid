package com.example.android.eventhub.ui.eventdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.eventhub.databinding.ListItemCommentBinding
import com.example.android.eventhub.domain.Comment
import com.example.android.eventhub.ui.eventdetails.CommentListAdapter.ViewHolder.Companion.from
import java.time.format.DateTimeFormatter

class CommentListAdapter :
    ListAdapter<Comment, CommentListAdapter.ViewHolder>(CommentDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = getItem(position)
        holder.bind(comment)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Comment) {
            binding.comment = item
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemCommentBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class CommentDiffCallback : DiffUtil.ItemCallback<Comment>() {
    override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
        return oldItem.id == newItem.id
    }
}

@BindingAdapter("commentDateTime")
fun TextView.setCommentDateTime(item: Comment) {
    text = item.timeStamp.format(DateTimeFormatter.ofPattern("dd MMM yyyy HH:mm"))
}