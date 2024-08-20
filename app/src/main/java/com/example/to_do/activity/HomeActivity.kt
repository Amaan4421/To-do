package com.example.to_do.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_do.R
import com.example.to_do.adapter.TaskAdapter
import com.example.to_do.model.Task
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar


class HomeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var listOfTask: RecyclerView
    private lateinit var taskAdapter: TaskAdapter
    private val taskList = mutableListOf<Task>()
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val welcomeText: TextView = findViewById(R.id.tvName)
        sharedPreferences = getSharedPreferences("To-Do Strings", Context.MODE_PRIVATE)
        val username = sharedPreferences.getString("USERNAME", "") ?: ""
        welcomeText.text = "Welcome, $username!"

        val btnAdd: ImageView = findViewById(R.id.imgAdd)
        val btnAdd2: Button = findViewById(R.id.btnAdd)

        listOfTask = findViewById(R.id.listOfTasks)
        listOfTask.layoutManager = LinearLayoutManager(this)
        taskAdapter = TaskAdapter(taskList)
        listOfTask.adapter = taskAdapter

        btnAdd.setOnClickListener {
            openActivity(AddTaskActivity::class.java)
        }

        btnAdd2.setOnClickListener {
            openActivity(AddTaskActivity::class.java)
        }


        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        drawerLayout = findViewById(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        val navigationView: NavigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_add_task -> {
                    openActivity(AddTaskActivity::class.java)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                R.id.nav_user_manual -> {
                    openActivity(UserManualActivity::class.java)
                    drawerLayout.closeDrawer(GravityCompat.START)
                    true
                }
                else -> false
            }
        }
        loadTasks()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (toggle.onOptionsItemSelected(item)) {
            true
        } else super.onOptionsItemSelected(item)
    }


    private fun openActivity(cls: Class<*>) {
        val intent = Intent(this, cls)
        startActivity(intent)
    }

    private fun loadTasks() {
        taskList.clear()
        val savedTasks = showTasks()
        taskList.addAll(savedTasks)
        taskAdapter.notifyDataSetChanged()
    }

    private fun showTasks(): ArrayList<Task> {
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



    private fun saveTasks() {
        val editor = sharedPreferences.edit()
        val taskSet = taskList.map { task ->
            "${task.id},${task.taskName},${task.dueDate},${task.time},${task.description}"
        }.toSet()
        editor.putStringSet("TASK_LIST", taskSet)
        editor.apply()
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
            data?.getParcelableExtra<Task>("TASK")?.let { task ->
                taskList.add(task)
                taskAdapter.notifyDataSetChanged()
                saveTasks()
            }
        }
    }



    companion object {
        const val ADD_TASK_REQUEST_CODE = 1
    }



    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}
