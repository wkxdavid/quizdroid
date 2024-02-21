package edu.uw.ischool.dpham722.quizdroid

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ActivityDownload(appContext: Context, workerParams: WorkerParameters) : Worker(appContext, workerParams) {
    override fun doWork(): Result {
        val sharedPrefs = applicationContext.getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val url = sharedPrefs.getString("url", "defaultUrl")

        if (!isNetworkOnline()) {
            Log.e("ActivityDownload", "No internet connection.")
            return Result.retry()
        }

        if (url.isNullOrEmpty() || url == "defaultURL") {
            Log.e("ActivityDownload", "URL is not configured.")
            return Result.failure()
        }

        try {
            val client = OkHttpClient()
            val request = Request.Builder().url(url).build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                val inputStream = response.body?.byteStream()
                val outputFile = File(applicationContext.filesDir, "questions.json.tmp")
                FileOutputStream(outputFile).use { outputStream ->
                    inputStream?.copyTo(outputStream)
                }
                val finalFile = File(applicationContext.filesDir, "questions.json")
                if (finalFile.exists()) finalFile.delete()
                outputFile.renameTo(finalFile)
            }

            Log.d("ActivityDownload", "File downloaded and saved successfully.")
            return Result.success()
        } catch (e: Exception) {
            Log.e("ActivityDownload", "Failed to download file: ${e.message}")
            restoreBackup()
            return Result.failure()
        }
    }

    private fun isNetworkOnline(): Boolean {
        val connectivityManager = applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return capabilities.hasCapability(android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun restoreBackup() {
        val backupFile = File(applicationContext.filesDir, "custom_questions.json")
        val finalFile = File(applicationContext.filesDir, "questions.json")
        if (backupFile.exists()) {
            if (finalFile.exists()) finalFile.delete()
            backupFile.renameTo(finalFile)
            Log.d("ActivityDownload", "Backup restored.")
        }
    }
}
