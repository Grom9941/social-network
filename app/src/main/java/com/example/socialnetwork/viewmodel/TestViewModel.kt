package com.example.socialnetwork.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.socialnetwork.model.Repository
import com.example.socialnetwork.model.dataclass.User
import kotlinx.coroutines.launch
import retrofit2.Response

class TestViewModel(private val repository: Repository) : ViewModel() {
    val allUsersNetwork: MutableLiveData<Response<List<User>>> = MutableLiveData()
    fun getUsersNetwork() {
        viewModelScope.launch {
            allUsersNetwork.value = repository.getUsersNetwork()
        }
    }
    val allUsersCache = repository.allUsersCache.asLiveData()

    //fun getFriends(id: Int) = repository.getFriendsById(id).asLiveData()
    fun getUserInfo(id: Int) = repository.getUserInfoById(id).asLiveData()

    fun insert(user: User) = viewModelScope.launch { repository.insert(user) }
    fun delete() = viewModelScope.launch { repository.delete() }
}