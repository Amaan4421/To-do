package com.example.to_do.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do.R
import com.example.to_do.model.Task
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddTaskActivity: AppCompatActivity() {

    private lateinit var edtTaskName: EditText
    private lateinit var edtDate: EditText
    private lateinit var edtTime: EditText
    private lateinit var edtDescription: EditText
    private lateinit var sharedPreferences: SharedPreferences

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_task)

        val backBtn: ImageView = findViewById(R.id.back)
        edtTaskName = findViewById(R.id.etTaskName)
        edtDate = findViewById(R.id.etDueDate)
        edtTime = findViewById(R.id.etTime)
        edtDescription = findViewById(R.id.etDescription)
        val btnAdd: Button = findViewById(R.id.btnAddTask)

        sharedPreferences = getSharedPreferences("To-Do Strings", Context.MODE_PRIVATE)

        edtDate.setOnClickListener {
            showDatePicker()
        }

        edtTime.setOnClickListener {
            showTimePicker()
        }

        backBtn.setOnClickListener{
            onBackPressed()
        }

        btnAdd.setOnClickListener{
            addTaskIntoDatabase()
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                edtDate.setText(dateFormat.format(selectedDate.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                val selectedTime = Calendar.getInstance()
                selectedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                selectedTime.set(Calendar.MINUTE, minute)
                val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
                edtTime.setText(timeFormat.format(selectedTime.time))
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }

    private fun addTaskIntoDatabase() {
        val taskName = edtTaskName.text.toString().trim()
        val dueDate = edtDate.text.toString().trim()
        val time = edtTime.text.toString().trim()
        val description = edtDescription.text.toString().trim()

        if (taskName.isEmpty() || dueDate.isEmpty() || time.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields first!!!", Toast.LENGTH_SHORT).show()
        } else {
            val task = Task(
                System.currentTimeMillis(),
                taskName,
                dueDate,
                time,
                description
            )

            saveTaskInMemory(task)
            Toast.makeText(this, "Your task added successfully!!!", Toast.LENGTH_SHORT).show()

            val intent = Intent()
            intent.putExtra("TASK", task)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun saveTaskInMemory(task: Task) {
        val editor = sharedPreferences.edit()
        val taskSet = sharedPreferences.getStringSet("TASK_LIST", mutableSetOf()) ?: mutableSetOf()

        val taskString = "${task.id},${task.taskName},${task.dueDate},${task.time},${task.description}"
        taskSet.add(taskString)

        editor.putStringSet("TASK_LIST", taskSet)
        editor.apply()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}
