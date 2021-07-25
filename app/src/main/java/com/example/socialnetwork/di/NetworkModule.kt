package com.example.socialnetwork.di

import com.example.socialnetwork.model.Repository
import com.example.socialnetwork.model.database.UserDao
import com.example.socialnetwork.model.network.UserRetrofitSource
import com.example.socialnetwork.model.network.Webservice
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_DIR =
        "https://firebasestorage.googleapis.com/v0/b/candidates--questionnaire.appspot.com/o/"

    @Provides
    fun provideHTTPLoggingInterceptor() =
        HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor
    ) = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_DIR)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

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