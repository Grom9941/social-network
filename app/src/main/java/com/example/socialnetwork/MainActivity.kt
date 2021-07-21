
package com.example.socialnetwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.socialnetwork.view.UserFragment

class MainActivity : AppCompatActivity() {

    private val USER_FRAGMENT_TAG = "BACK_STACK_ROOT_TAG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (supportFragmentManager.backStackEntryCount == 0) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.nav_host_fragment_container, UserFragment())
                .addToBackStack(USER_FRAGMENT_TAG)
                .commit()
        }
    }
}