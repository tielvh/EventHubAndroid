package com.example.android.eventhub.network

import com.example.android.eventhub.App
import com.example.android.eventhub.LocalDateTimeAdapter
import com.example.android.eventhub.domain.User
import com.example.android.eventhub.repository.UserRepository
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

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
