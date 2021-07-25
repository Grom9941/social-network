package com.example.socialnetwork.di

import android.content.Context
import com.example.socialnetwork.model.database.UserRoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideUserDao(database: UserRoomDatabase) = database.userDao()

    @Singleton
    @Provides
    fun provideUserRoomDatabase(@ApplicationContext appContext: Context) =
        UserRoomDatabase.getDatabase(appContext)
}