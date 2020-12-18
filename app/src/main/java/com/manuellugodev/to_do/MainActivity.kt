package com.manuellugodev.to_do

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.Navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bAddTaskActivity.setOnClickListener{
            findNavController(this,R.id.nav_host_fragment).navigate(R.id.action_listTaskFragment_to_newTaskFragment)
        }

    }
}