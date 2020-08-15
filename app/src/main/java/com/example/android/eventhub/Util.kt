package com.example.android.eventhub

import android.app.Application
import android.widget.ImageView
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.android.eventhub.database.EventDatabase
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface OnItemClickListener<T> {
    fun onItemClick(item: T)
}

fun getDatabase(application: Application): EventDatabase {
    return EventDatabase.getInstance(application)
}

class LocalDateTimeAdapter {
    @ToJson
    fun toJson(value: LocalDateTime): String {
        return FORMATTER.format(value)
    }

    @FromJson
    fun fromJson(value: String): LocalDateTime {
        return FORMATTER.parse(value, LocalDateTime::from)
    }

    companion object {
        private val FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .into(imgView)
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, file: File?) {
    if (file != null && file.exists()) {
        Glide.with(imgView.context).load(file).into(imgView)
    }
}

class App : Application() {
    companion object {
        private lateinit var INSTANCE: App

        fun getInstance(): App {
            return INSTANCE
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}