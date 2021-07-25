package com.example.socialnetwork.model

import com.example.socialnetwork.model.database.UserDao
import com.example.socialnetwork.model.network.UserRetrofitSource
import javax.inject.Inject

class Repository @Inject constructor(private val userDao: UserDao, private val userRetrofitSource: UserRetrofitSource) {

    fun getAllUsers() =  getUsersData(
        databaseQuery = { userDao.getAllUsers() },
        networkCall = { userRetrofitSource.getAllUsers() },
        saveCallResult = { userDao.insertAll(it) }
        )
}