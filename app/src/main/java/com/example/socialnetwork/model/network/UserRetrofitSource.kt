package com.example.socialnetwork.model.network

import com.example.socialnetwork.model.BaseResource
import javax.inject.Inject

class UserRetrofitSource @Inject constructor(private val webservice: Webservice) : BaseResource() {
    suspend fun getAllUsers() = getResult { webservice.getAllUsers() }
}