package com.example.android.eventhub.dagger

import com.example.android.eventhub.ui.account.AccountViewModel
import com.example.android.eventhub.ui.eventcreation.EventCreationViewModel
import com.example.android.eventhub.ui.eventdetails.EventDetailsViewModel
import com.example.android.eventhub.ui.events.EventsViewModel
import com.example.android.eventhub.ui.login.LoginViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(target: EventsViewModel)

    fun inject(target: AccountViewModel)

    fun inject(target: EventCreationViewModel)

    fun inject(target: EventDetailsViewModel)

    fun inject(target: LoginViewModel)
}