package edu.uw.ischool.dpham722.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class Preferences : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences)

        val toolbar = findViewById<Toolbar>(R.id.prefActivityBar)
        val intervalEditText = findViewById<EditText>(R.id.minuteInput)
        val submitButton = findViewById<Button>(R.id.submitButton)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        intervalEditText.setText(sharedPreferences.getInt("downloadMinute", 5).toString())

        submitButton.setOnClickListener {
            try {
                val newInterval = intervalEditText.text.toString().toInt()

                editor.putInt("downloadMinute", newInterval)
                editor.apply()

                Toast.makeText(this, "Interval preference saved successfully.", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, MainActivity::class.java))
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please enter a valid number for the interval.", Toast.LENGTH_SHORT).show()
            }
        }
    }
}