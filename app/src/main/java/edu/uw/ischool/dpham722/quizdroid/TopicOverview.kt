package edu.uw.ischool.dpham722.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import org.json.JSONObject

class TopicOverview : AppCompatActivity() {
    private val quizChoice: JSONObject = JSONObject("""{
        "Math": {
            "BriefDescription": "Test your knowledge on addition, subtraction, multiplication, and division with this mathematical quiz.",
            "NumberOfQuestions": "4"
        },
        "Physics": {
            "BriefDescription": "Test your knowledge on physics and see your understanding of motion and behavior through space and time.",
            "NumberOfQuestions": "3"
        },
        "Marvel Super Heroes": {
            "BriefDescription" : "Marvel is known for their American superheroes in the MCU film franchise. Do you know your Marvel Super Heroes?",
            "NumberOfQuestions": "3"
        }
    }""")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_topic)

        val topic = intent.getStringExtra("quizTopic") ?: ""
        val topicOverview = findViewById<TextView>(R.id.quizTopic)
        topicOverview.text = getString(R.string.quiz_overview, topic)

        val briefDescr = quizChoice.getJSONObject(topic).getString("BriefDescription")
        val quizOverview = findViewById<TextView>(R.id.quizOverview)
        quizOverview.text = briefDescr

        val numQuestions = quizChoice.getJSONObject(topic).getString("NumberOfQuestions")
        val numOverview = findViewById<TextView>(R.id.numQuestions)
        numOverview.text = getString(R.string.number_of_questions, numQuestions)

        val beginButton = findViewById<Button>(R.id.beginButton)
        beginButton.text = getString(R.string.begin_quiz)
        beginButton.setOnClickListener {
            val intent = Intent(this, QuestionGenerator::class.java)
            intent.putExtra("quizTopic", topic)
            startActivity(intent)
        }
    }
}
