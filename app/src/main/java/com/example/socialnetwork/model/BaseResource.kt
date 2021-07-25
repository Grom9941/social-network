package com.example.socialnetwork.model

import android.util.Log
import retrofit2.Response

abstract class BaseResource {

    protected suspend fun <T> getResult(call: suspend () -> Response<T>): Resource<T> {
        try {
            val response = call()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) return Resource.success(body)
            }
            return error(" ${response.code()} ${response.message()}")
        } catch (e: Exception) {
            return error(e.message ?: e.toString())
        }
    }

    private fun <T> error(message: String): Resource<T> {
        Log.e(BASE_RESOURCE_TAG, message)
        return Resource.error("Network isn't available")
    }

    companion object {
        private const val BASE_RESOURCE_TAG = "com.example.socialnetwork.model_BaseResource"
    }
}