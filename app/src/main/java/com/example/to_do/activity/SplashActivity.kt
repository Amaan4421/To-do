package com.example.to_do.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do.R
import com.example.to_do.model.Task

class SplashActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPreferences = getSharedPreferences("To-Do Strings", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", null)

        Handler(Looper.getMainLooper()).postDelayed({
            if (username != null) {
                val taskList = getTasks()
                navigateToHomeActivity(username, taskList)
            } else {
                navigateToUsernameActivity()
            }
        }, 3000)
    }

    private fun getTasks(): ArrayList<Task> {
        val taskList = ArrayList<Task>()
        val taskSet = sharedPreferences.getStringSet("TASK_LIST", emptySet())
        taskSet?.forEach { taskString ->
            val taskParts = taskString.split(",")
            if (taskParts.size == 5) {
                val task = Task(
                    taskParts[0].toLong(),
                    taskParts[1],
                    taskParts[2],
                    taskParts[3],
                    taskParts[4]
                )
                taskList.add(task)
            }
        }
        return taskList
    }

    private fun navigateToHomeActivity(username: String, taskList: ArrayList<Task>) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("USERNAME", username)
        intent.putExtra("TASK_LIST", taskList)
        startActivity(intent)
        finish()
    }

    private fun navigateToUsernameActivity() {
        val intent = Intent(this, UsernameActivity::class.java)
        startActivity(intent)
        finish()
    }
}
