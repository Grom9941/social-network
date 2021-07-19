
package com.example.socialnetwork

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import com.example.socialnetwork.databinding.ActivityMainBinding
import com.example.socialnetwork.viewmodel.TestViewModel
import com.example.socialnetwork.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private val testViewModel: TestViewModel by viewModels {
        ViewModelFactory((application as UserApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        testViewModel.allUsersNetwork.observe(this, {
                userNetwork -> userNetwork.body().toString()
        })
        testViewModel.getUsersNetwork()
    }
}