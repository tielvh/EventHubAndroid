package com.example.android.eventhub.dagger

import com.example.android.eventhub.App
import com.example.android.eventhub.getDatabase
import com.example.android.eventhub.repository.CommentRepository
import com.example.android.eventhub.repository.EventRepository
import com.example.android.eventhub.repository.UserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Provides
    @Singleton
    fun provideEventRepository(): EventRepository = EventRepository(getDatabase(App.getInstance()))

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = UserRepository(App.getInstance())

    @Provides
    @Singleton
    fun provideCommentRepository(): CommentRepository =
        CommentRepository(getDatabase(App.getInstance()))
}