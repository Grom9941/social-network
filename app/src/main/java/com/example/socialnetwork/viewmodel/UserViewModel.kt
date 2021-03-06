package com.example.socialnetwork.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialnetwork.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    var getAllUsers = repository.getAllUsers()

    val getUsers = repository.getAllUsers()

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
        getAllUsers = repository.getAllUsers()
    }

    fun update() = viewModelScope.launch { getAllUsers = repository.getAllUsers() }

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