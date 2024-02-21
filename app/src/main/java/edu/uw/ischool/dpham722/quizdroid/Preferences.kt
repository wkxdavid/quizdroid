package edu.uw.ischool.dpham722.quizdroid

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.app.AlertDialog
import android.content.Context
import android.net.ConnectivityManager
import android.provider.Settings
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit
import android.net.NetworkCapabilities


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

        val urlInput = findViewById<EditText>(R.id.urlInput)

        submitButton.setOnClickListener {
            try {
                val newInterval = intervalEditText.text.toString().toInt()
                val downloadUrl = urlInput.text.toString()

                editor.putInt("downloadMinute", newInterval)
                editor.putString("url", downloadUrl)
                editor.apply()

                if (isAirplaneModeOn()) {
                    Toast.makeText(this, "Airplane Mode is on. Please turn off Airplane Mode to use this feature.", Toast.LENGTH_LONG).show()

                    AlertDialog.Builder(this)
                        .setTitle("Airplane Mode On")
                        .setMessage("Do you want to turn Airplane Mode off?")
                        .setPositiveButton("Turn Off") { _, _ ->
                            startActivity(Intent(Settings.ACTION_AIRPLANE_MODE_SETTINGS))
                        }
                        .show()
                } else if (!isNetworkOnline()) {
                    Toast.makeText(this, "You are currently not connected to the internet. Please try again.", Toast.LENGTH_LONG).show()
                } else {
                    scheduleActivityDownload(newInterval)
                    Toast.makeText(this, "Interval preference saved successfully.", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, MainActivity::class.java))
                }
            } catch (e: NumberFormatException) {
                Toast.makeText(this, "Please enter a valid number for the interval.", Toast.LENGTH_SHORT).show()
            }
        }
    }


    fun scheduleActivityDownload(interval: Int) {
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val downloadRequest = PeriodicWorkRequestBuilder<ActivityDownload>(
            interval.toLong(), TimeUnit.MINUTES

        ).setConstraints(constraints).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "downloadWork",
            ExistingPeriodicWorkPolicy.REPLACE,
            downloadRequest
        )
    }

    private fun isNetworkOnline(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            else -> false
        }
    }
    private fun isAirplaneModeOn(): Boolean {
        return Settings.Global.getInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
    }
}