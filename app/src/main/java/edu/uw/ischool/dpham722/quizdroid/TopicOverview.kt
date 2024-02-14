package edu.uw.ischool.dpham722.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class TopicOverview : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)

        val topicIndex = intent.getIntExtra("quizTopic", 0)
        val topicChoice = QuizApp.repo.getTopic(topicIndex)

        val topicOverview = findViewById<TextView>(R.id.quizTopic)
        val quizOverview = findViewById<TextView>(R.id.quizOverview)
        val numberOverview = findViewById<TextView>(R.id.numQuestions)

        topicOverview.text = "${topicChoice.title} Quiz Overview"
        quizOverview.text = topicChoice.longDesc
        numberOverview.text = "Number of questions: ${topicChoice.quizzes.size}"

        val beginButton = findViewById<Button>(R.id.beginButton)
        beginButton.setOnClickListener {
            val intent = Intent(this, QuestionGenerator::class.java)
            intent.putExtra("quizTopic", topicIndex)
            startActivity(intent)
        }
    }
}
