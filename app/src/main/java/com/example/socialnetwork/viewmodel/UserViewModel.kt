package com.example.socialnetwork.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.socialnetwork.model.Repository
import com.example.socialnetwork.model.dataclass.User
import kotlinx.coroutines.launch

class UserViewModel(private val repository: Repository) : ViewModel() {

    val usersData: MutableLiveData<List<User>> = MutableLiveData()
    val userInfo: MutableLiveData<User> = MutableLiveData()
    private var allUsersCached = false

    fun getUsersData() {
        if (allUsersCached) {
            getUsersCached()
            Log.v("UserViewModel", "roomRequest")
        } else {
            getUsersNetwork()
            Log.v("UserViewModel", "networkRequest")
        }
    }

    fun getUsersData(id: Int) {
        if (id >= 0) {
            getUserInfo(id)
            Log.v("UserViewModel", "networkRequest")
        }
    }

    private fun getUsersNetwork() {
        viewModelScope.launch {
            val listUsers = repository.getUsersNetwork().body()
            usersData.value = listUsers
            listUsers?.let { insert(it) }
            allUsersCached = true
        }
    }

    private fun getUsersCached() {
        usersData.value = repository.allUsersCache.asLiveData().value
    }

    private fun getUserInfo(id: Int) {
        userInfo.value = repository.getUserInfoById(id).asLiveData().value
    }

    fun insert(user: User) = viewModelScope.launch { repository.insert(user) }
    fun insert(userList: List<User>) = viewModelScope.launch {
        userList.forEach { repository.insert(it) }
    }
    fun delete() = viewModelScope.launch { repository.delete() }


    private val sharedData: MutableLiveData<Int> = MutableLiveData()

    fun setData(id: Int) {
        sharedData.value = id
    }

    fun getData() = sharedData
}