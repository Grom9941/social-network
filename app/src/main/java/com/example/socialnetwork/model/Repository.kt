package com.example.socialnetwork.model

import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.example.socialnetwork.model.database.UserDao
import com.example.socialnetwork.model.network.UserRetrofitSource
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class Repository @Inject constructor(
    private val userDao: UserDao,
    private val userRetrofitSource: UserRetrofitSource
) {

    fun getAllUsers() = getUsersData(
        databaseQuery = { userDao.getAllUsers().asLiveData() },
        networkCall = { userRetrofitSource.getAllUsers() },
        saveCallResult = { userDao.insertAll(it) }
    )

    @WorkerThread
    suspend fun deleteAll() {
        userDao.deleteAll()
    }

    private fun <T, A> getUsersData(
        databaseQuery: () -> LiveData<T>,
        networkCall: suspend () -> Resource<A>,
        saveCallResult: suspend (A) -> Unit
    ): LiveData<Resource<T>> =
        liveData(Dispatchers.IO) {
            emit(Resource.loading())
            val source = databaseQuery.invoke().map { Resource.success(it) }
            Log.i(REPOSITORY_TAG, "database")
            emitSource(source)

            val responseStatus = networkCall.invoke()
            if (responseStatus.status == Resource.Status.SUCCESS) {
                Log.i(REPOSITORY_TAG, "network")
                saveCallResult(responseStatus.data!!)

            } else if (responseStatus.status == Resource.Status.ERROR) {
                emit(Resource.error(responseStatus.message!!))
                emitSource(source   )
            }
        }

    companion object {
        private const val REPOSITORY_TAG = "com.example.socialnetwork.model_Repository"
    }
}