package com.example.socialnetwork.model

import androidx.annotation.WorkerThread
import com.example.socialnetwork.model.dataclass.User
import com.example.socialnetwork.model.network.Webservice
import com.example.socialnetwork.model.database.UserDao

class Repository(private val userDao: UserDao, private val webservice: Webservice) {

    val allUsersCache = userDao.getAllUsers()

    @WorkerThread
    suspend fun getUsersNetwork() = webservice.getAllUsers()

    fun getUserInfoById(id: Int) = userDao.getUserInfoById(id)

    @WorkerThread
    suspend fun insert(user: User) = userDao.insert(user)

    @WorkerThread
    suspend fun delete() = userDao.deleteAll()
}