package edu.uw.ischool.dpham722.quizdroid

import android.app.Application
import android.util.Log
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

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
    private val jsonFile: String =  context.resources.openRawResource(R.raw.questions).bufferedReader().use { it.readText() }
    private val jsonData: List<QuizApp.Topic> = Gson().fromJson(jsonFile, object : TypeToken<List<QuizApp.Topic>>() {}.type)

    override fun getTopicChoices(): List<QuizApp.Topic> {
        return jsonData
    }
    override fun getTopic(topic: Int): QuizApp.Topic {
        return jsonData[topic]
    }
    override fun getQuiz(topic: Int, quiz: Int): QuizApp.Quiz {
        return jsonData[topic].questions[quiz]
    }
}