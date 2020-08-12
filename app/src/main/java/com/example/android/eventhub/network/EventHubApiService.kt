package com.example.android.eventhub.network

import com.example.android.eventhub.LocalDateTimeAdapter
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://eventhubweb4.azurewebsites.net/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(LocalDateTimeAdapter())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface EventHubApiService {
    @GET("Events")
    fun getAllEventsAsync(): Deferred<List<NetworkEvent>>

    @GET("Comments")
    fun getCommentsAsync(@Query("eventId") eventId: Int): Deferred<List<NetworkComment>>
}

object EventApi {
    val retrofitService: EventHubApiService by lazy {
        retrofit.create(EventHubApiService::class.java)
    }
}