package com.example.socialnetwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.socialnetwork.view.UserFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        createTransaction()
    }

    private fun createTransaction() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_container, UserFragment())
                .commit()
        }
    }
}