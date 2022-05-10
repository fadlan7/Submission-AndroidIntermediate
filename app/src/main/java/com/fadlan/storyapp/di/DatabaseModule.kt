package com.fadlan.storyapp.di

import android.content.Context
import androidx.room.Room
import com.fadlan.storyapp.data.local.RemoteKeysDao
import com.fadlan.storyapp.data.local.StoriesDao
import com.fadlan.storyapp.data.local.StoriesDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Provides
    fun provideStoryDao(storiesDatabase: StoriesDatabase): StoriesDao = storiesDatabase.storiesDao()

    @Provides
    fun provideRemoteKeysDao(storiesDatabase: StoriesDatabase): RemoteKeysDao =
        storiesDatabase.remoteKeysDao()

    @Provides
    @Singleton
    fun provideStoryDatabase(@ApplicationContext context: Context): StoriesDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            StoriesDatabase::class.java,
            "story_database"
        ).build()
    }
}