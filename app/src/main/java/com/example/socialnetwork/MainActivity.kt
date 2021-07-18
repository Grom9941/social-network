
package com.example.socialnetwork

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.socialnetwork.viewmodel.TestViewModel
import com.example.socialnetwork.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private val testViewModel: TestViewModel by viewModels {
        ViewModelFactory((application as UserApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.textView)

        testViewModel.allUsersNetwork.observe(this, {
                user -> textView.text = user.toString()
        })

        findViewById<Button>(R.id.button).setOnClickListener{
            testViewModel.getUsersNetwork()
        }
    }
}