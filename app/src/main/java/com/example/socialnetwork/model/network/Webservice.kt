package com.example.socialnetwork.model.network

import com.example.socialnetwork.model.dataclass.User
import retrofit2.Response
import retrofit2.http.GET

interface Webservice {
    @GET("users.json?alt=media&token=e3672c23-b1a5-4ca7-bb77-b6580d75810c")
    suspend fun getAllUsers(): Response<List<User>>
}