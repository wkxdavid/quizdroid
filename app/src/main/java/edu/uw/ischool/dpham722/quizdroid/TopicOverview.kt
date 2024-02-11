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
            "BriefDescription": "It's math time.",
            "NumberOfQuestions": "4"
        },
        "Physics": {
            "BriefDescription": "Its physics time.",
            "NumberOfQuestions": "3"
        },
        "Marvel Super Heroes": {
            "BriefDescription" : "It's marvel time.?",
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

        val numQuestionsString = quizChoice.getJSONObject(topic).getString("NumberOfQuestions")
        val numQuestions = numQuestionsString.toInt()
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
