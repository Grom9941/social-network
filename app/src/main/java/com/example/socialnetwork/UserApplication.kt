package com.example.socialnetwork

import android.app.Application
import com.example.socialnetwork.model.Repository
import com.example.socialnetwork.model.network.RetrofitInstance
import com.example.socialnetwork.model.database.UserRoomDatabase

class UserApplication : Application() {
    private val database by lazy { UserRoomDatabase.getDatabase(this) }
    private val webservice by lazy { RetrofitInstance.webservice }

    val repository by lazy { Repository(database.userDao(), webservice) }
}