package com.example.socialnetwork

import android.app.Application
import android.util.Log
import com.example.socialnetwork.model.Repository
import com.example.socialnetwork.model.database.UserRoomDatabase
import com.example.socialnetwork.model.network.RetrofitInstance
import kotlin.properties.Delegates

class UserApplication : Application() {
    private val database by lazy { UserRoomDatabase.getDatabase(this) }
    private val webservice by lazy { RetrofitInstance.webservice }

    val repository by lazy { Repository(database.userDao(), webservice) }

    object Variables {
        var isNetworkConnected: Boolean by Delegates.observable(false) { _, _, newValue ->
            Log.i("userViewModel Network connectivity", "$newValue")
        }
    }
}