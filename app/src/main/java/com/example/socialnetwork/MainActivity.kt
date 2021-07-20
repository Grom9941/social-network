
package com.example.socialnetwork

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.socialnetwork.view.UserFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val TAG = "BACK_STACK_ROOT_TAG"
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.nav_host_fragment_container, UserFragment())
            .addToBackStack(TAG)
            .commit()
    }
}