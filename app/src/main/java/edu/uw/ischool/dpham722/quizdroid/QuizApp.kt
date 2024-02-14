package edu.uw.ischool.dpham722.quizdroid

import android.app.Application
import android.util.Log

class QuizApp : Application() {
    data class Quiz(val question: String, val answers: Array<String>, val correct: Int)
    data class Topic(val title: String, val shortDesc: String, val longDesc: String, var quizzes: List<Quiz>)

    companion object {
        var repo: TopicRepository = TopicRepo()
    }
    override fun onCreate() {
        super.onCreate()
        repo = TopicRepo()
        Log.d("QuizApp", "QuizApp is running.")
    }
}

interface TopicRepository {
    fun getTopicChoices(): List<QuizApp.Topic>
    fun getTopic(topic: Int): QuizApp.Topic
    fun getQuiz(topic: Int, quiz: Int): QuizApp.Quiz
}
class TopicRepo : TopicRepository {
    private val topics: List<QuizApp.Topic> = listOf(
        QuizApp.Topic(
            "Math",
            "It's Math Time",
            "Do you remember anything from those math classes? Let's see!",
            listOf(
                QuizApp.Quiz("What is 1 + 2?", arrayOf("1", "2", "3", "4"), 2),
                QuizApp.Quiz("What is 10 - 5?", arrayOf("15", "5", "6", "10"), 1),
                QuizApp.Quiz("What is 6 * 6?", arrayOf("66", "48", "24", "36"), 3),
                QuizApp.Quiz("What is 100 / 25?", arrayOf("5", "4", "0", "100"), 1)
            )
        ),
        QuizApp.Topic(
            "Physics",
            "It's Physics Time",
            "Do you remember anything from those physics classes? Let's find out!",
            listOf(
                QuizApp.Quiz(
                    "The first law of thermodynamics is also known by what name?",
                    arrayOf(
                        "conservation of energy",
                        "conservation of mass",
                        "conservation of gravity",
                        "conservation of temperature"
                    ), 0
                ),
                QuizApp.Quiz(
                    "What is the unit of measure for cycles per second?",
                    arrayOf("ounces", "spin", "hertz", "celsius"), 2
                ),
                QuizApp.Quiz(
                    "What is the formula for density?",
                    arrayOf(
                        "d = M * V",
                        "d = M - V",
                        "d = M + V",
                        "d = M / V"
                    ), 3
                )
            )
        ),
        QuizApp.Topic(
            "Marvel Super Heroes",
            "It's Marvel Super Heroes Time",
            "Marvel is know for their superheroes in the MCU film franchise. I wonder if you can ge these questions?",
            listOf(
                QuizApp.Quiz(
                    "Where is Captain America from?",
                    arrayOf("Seattle", "Antarctica", "Brooklyn", "NYC"), 2
                ),
                QuizApp.Quiz(
                    "What is the Green Infinity Stone from Dr Strange?",
                    arrayOf("Time", "Power", "Reality", "Mind"), 0
                ),
                QuizApp.Quiz(
                    "Who is not a Avenger?",
                    arrayOf("Wonder Women", "Thor", "Hulk", "Spider man"), 0
                )
            )
        )
    )
    override fun getTopicChoices(): List<QuizApp.Topic> {
        return topics
    }
    override fun getTopic(topic: Int): QuizApp.Topic {
        return topics[topic]
    }
    override fun getQuiz(topic: Int, quiz: Int): QuizApp.Quiz {
        return topics[topic].quizzes[quiz]
    }
}