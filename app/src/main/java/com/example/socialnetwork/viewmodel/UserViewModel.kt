package com.example.socialnetwork.viewmodel

import androidx.lifecycle.*
import com.example.socialnetwork.model.Repository
import com.example.socialnetwork.model.dataclass.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(repository: Repository) : ViewModel() {

    val getAllUsers = repository.getAllUsers()

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