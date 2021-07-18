package com.example.socialnetwork.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.socialnetwork.model.Repository
import java.lang.IllegalArgumentException

class ViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TestViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TestViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class provided")
    }
}