package com.example.socialnetwork.model.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    private const val BASE_DIR =
        "https://firebasestorage.googleapis.com/v0/b/candidates--questionnaire.appspot.com/o/"

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()
    }
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_DIR)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    internal val webservice by lazy { retrofit.create(Webservice::class.java) }
}