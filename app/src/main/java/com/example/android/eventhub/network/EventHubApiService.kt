package com.example.android.eventhub.network

import com.example.android.eventhub.App
import com.example.android.eventhub.LocalDateTimeAdapter
import com.example.android.eventhub.repository.UserRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.*

private const val BASE_URL = "https://eventhubweb4.azurewebsites.net/api/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .add(LocalDateTimeAdapter())
    .build()

private val retrofit = Retrofit.Builder()
    .client(OkHttpClient().newBuilder().addInterceptor(AuthenticationInterceptor()).build())
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface EventHubApiService {
    @GET("Events")
    fun getAllEventsAsync(): Deferred<List<NetworkEvent>>

    @GET("Comments")
    fun getCommentsAsync(@Query("eventId") eventId: Int): Deferred<List<NetworkComment>>

    @POST("Users/Authenticate")
    fun authenticateAsync(@Body credentials: NetworkCredentials): Deferred<NetworkUser>

    @Multipart
    @POST("Events")
    fun postEventAsync(
        @Part name: MultipartBody.Part,
        @Part place: MultipartBody.Part,
        @Part dateTime: MultipartBody.Part,
        @Part description: MultipartBody.Part,
        @Part image: MultipartBody.Part
    ): Deferred<NetworkEvent>

    @Multipart
    @POST("Comments")
    fun postCommentAsync(
        @Part eventId: MultipartBody.Part,
        @Part content: MultipartBody.Part
    ): Call<Void>
}

object EventApi {
    val retrofitService: EventHubApiService by lazy {
        retrofit.create(EventHubApiService::class.java)
    }
}

class AuthenticationInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val repository = UserRepository(App.getInstance())
        var request = chain.request()
        if (repository.isLoggedIn()) {
            request = request
                .newBuilder()
                .addHeader(
                    "Authorization",
                    "Bearer ${repository.getUser().token}"
                )
                .build()
        }
        return chain.proceed(request)
    }
}
