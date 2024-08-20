package com.example.to_do.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.to_do.R

class UsernameActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_username)

        val usernameText: EditText = findViewById(R.id.username)
        val enterButton: Button = findViewById(R.id.enterButton)

        enterButton.setOnClickListener {
            val username = usernameText.text.toString()
            if (username.isNotEmpty())
            {
                val sharedPreferences = getSharedPreferences("To-Do Strings", Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("USERNAME", username)
                editor.apply()

                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("USERNAME", username)
                startActivity(intent)
                finish()
            }
            else
            {
                Toast.makeText(this, "Please Enter Username!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
