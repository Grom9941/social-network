package com.example.socialnetwork.di

import android.content.Context
import com.example.socialnetwork.model.Repository
import com.example.socialnetwork.model.database.UserDao
import com.example.socialnetwork.model.database.UserRoomDatabase
import com.example.socialnetwork.model.network.UserRetrofitSource
import com.example.socialnetwork.model.network.Webservice
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    private const val BASE_DIR =
        "https://firebasestorage.googleapis.com/v0/b/candidates--questionnaire.appspot.com/o/"

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_DIR)
        .client(
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        )
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    fun provideUserDao(database: UserRoomDatabase) = database.userDao()

    @Singleton
    @Provides
    fun provideUserRoomDatabase(@ApplicationContext appContext: Context) =
        UserRoomDatabase.getDatabase(appContext)

    @Provides
    fun provideWebservice(retrofit: Retrofit): Webservice = retrofit.create(Webservice::class.java)

    @Singleton
    @Provides
    fun provideUserRetrofitSource(webservice: Webservice) = UserRetrofitSource(webservice)

    @Singleton
    @Provides
    fun provideRepository(userDao: UserDao, userRetrofitSource: UserRetrofitSource) =
        Repository(userDao, userRetrofitSource)
}