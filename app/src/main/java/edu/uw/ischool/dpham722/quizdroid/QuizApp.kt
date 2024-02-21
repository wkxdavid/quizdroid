package edu.uw.ischool.dpham722.quizdroid

import android.app.Application
import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.FileReader

class QuizApp : Application() {
    data class Quiz(val text: String, val answers: Array<String>, val answer: Int)
    data class Topic(val title: String, val desc: String, var questions: List<Quiz>)

    companion object {
        lateinit var repo: TopicRepository
    }

    override fun onCreate() {
        super.onCreate()
        repo = TopicRepo(this)
        Log.d("QuizApp", "QuizApp is running.")
    }
}

interface TopicRepository {
    fun getTopicChoices(): List<QuizApp.Topic>
    fun getTopic(topic: Int): QuizApp.Topic
    fun getQuiz(topic: Int, quiz: Int): QuizApp.Quiz
}

class TopicRepo(private val context: Context) : TopicRepository {
    private var jsonData: List<QuizApp.Topic> = emptyList()

    init {
        loadQuizData()
    }

    private fun loadQuizData() {
        val internalFile = File(context.filesDir, "questions.json")
        if (internalFile.exists() && internalFile.canRead()) {
            try {
                FileReader(internalFile).use { reader ->
                    jsonData = Gson().fromJson(reader, object : TypeToken<List<QuizApp.Topic>>() {}.type)
                    return
                }
            } catch (e: Exception) {
                Log.e("TopicRepo", "Failed to load quiz data from internal storage: ${e.message}")
            }
        } else {
            Log.d("TopicRepo", "questions.json does not exist in internal storage. Loading fallback data.")
        }
        try {
            val inputStream = context.resources.openRawResource(R.raw.questions)
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            jsonData = Gson().fromJson(jsonString, object : TypeToken<List<QuizApp.Topic>>() {}.type)
        } catch (e: Exception) {
            Log.e("TopicRepo", "Failed to load quiz data: ${e.message}")
        }
    }


    override fun getTopicChoices(): List<QuizApp.Topic> = jsonData

    override fun getTopic(topic: Int): QuizApp.Topic = jsonData[topic]

    override fun getQuiz(topic: Int, quiz: Int): QuizApp.Quiz = jsonData[topic].questions[quiz]
}
