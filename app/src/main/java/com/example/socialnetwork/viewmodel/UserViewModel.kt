package com.example.socialnetwork.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.socialnetwork.UserApplication
import com.example.socialnetwork.model.Repository
import com.example.socialnetwork.model.dataclass.User
import kotlinx.coroutines.launch

class UserViewModel(private val repository: Repository) : ViewModel() {

    var usersData: MutableLiveData<List<User>> = MutableLiveData()
    var allUsersCached = false

    fun getUsersData() {
        //TODO: fix when leave app
        if (allUsersCached) {
            getUsersCached()
            Log.v("UserViewModel", "roomRequest-$allUsersCached")
        } else {
            getUsersNetwork()
            Log.v("UserViewModel", "networkRequest-$allUsersCached")
        }
    }

    private fun getUsersNetwork() {
        viewModelScope.launch {
            if (UserApplication.Variables.isNetworkConnected) {
                val listUsers = repository.getUsersNetwork().body()
                usersData.value = listUsers
                listUsers?.let { insert(it) }
                allUsersCached = true
            }
        }
    }

    private fun getUsersCached() {
        usersData = repository.allUsersCache.asLiveData() as MutableLiveData<List<User>>
    }

    fun insert(user: User) = viewModelScope.launch { repository.insert(user) }
    fun insert(userList: List<User>) = viewModelScope.launch {
        Log.v("UserViewModel", "insertData-$allUsersCached")
        userList.forEach { repository.insert(it) }
    }

    fun delete() = viewModelScope.launch {
        repository.delete()
        allUsersCached = false
        Log.v("UserViewModel", "deleteData-$allUsersCached")
        getUsersData()
    }

    private val sharedData: MutableLiveData<MutableList<Int>> = MutableLiveData()

    fun setData(id: Int, position: Int = 0) {
        sharedData.value = if (sharedData.value == null) mutableListOf(id) else {
            val listOfData = sharedData.value as MutableList<Int>
            listOfData.add(position, id)
            listOfData
        }
    }

    fun getData() = sharedData


}