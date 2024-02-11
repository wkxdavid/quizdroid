package edu.uw.ischool.dpham722.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject

class QuestionGenerator : AppCompatActivity() {

    private val quizQuestions: JSONObject = JSONObject("""{
        "Math": {
            "NumberOfQuestions": "4",
            "QuestionsList": [
                { "Question": "What is 1 + 2?",
                  "Answer": "3",
                  "AnswerOptions": ["1", "2", "3", "4"] },
                { "Question": "What is 15-5?",
                  "Answer": "10",
                  "AnswerOptions": ["15", "5", "6", "10"] },
                { "Question": "What is 6 * 6?",
                  "Answer": "36",
                  "AnswerOptions": ["66", "48", "24", "36"] },
                { "Question": "What is 100 / 25?",
                  "Answer": "4",
                  "AnswerOptions": ["4", "-1", "0", "1000"] }
            ]
        },
        "Physics": {
            "NumberOfQuestions": "3",   
            "QuestionsList": [
                { "Question": "The first law of thermodynamics is also known by what name?",
                  "Answer": "conservation of energy",
                  "AnswerOptions": [
                        "conservation of energy", 
                        "conservation of mass", 
                        "conservation of gravity",
                        "conservation of temperature" ] },
                { "Question": "What is the unit of measure for cycles per second?",
                  "Answer": "hertz",
                  "AnswerOptions": [
                        "ounces", 
                        "hertz", 
                        "spin",
                        "celsius" ] },
                { "Question": "What is the formula for density?",
                  "Answer": "d = M / V",
                  "AnswerOptions": [
                        "d = M * V", 
                        "d = M / V", 
                        "d = M + V",
                        "d = M - V" ] }
            ]
        },
        "Marvel Super Heroes": {
            "NumberOfQuestions": "3",
            "QuestionsList": [
                { "Question": "Where is Captain America from?",
                  "Answer": "Brooklyn",
                  "AnswerOptions": [
                        "Seattle", 
                        "Brooklyn", 
                        "Bellevue",
                        "NYC" ] },
                { "Question": "What is the Green Infinity Stone?",
                  "Answer": "6",
                  "AnswerOptions": [ "Time", "Power", "Reality", "Mind" ] },
                { "Question": "Who is not a avenger?",
                  "Answer": "Wonder Women",
                  "AnswerOptions": [
                        "Wonder Women", 
                        "Thor", 
                        "Hulk",
                        "Iron Man" ] }
            ]
        }
    }""")

    private var userAnswer: String = ""
    private var userCorrect: Int = 0
    private var isCorrect: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

        val topic = intent.getStringExtra("quizTopic") ?: "DefaultTopic"
        val questionNum = intent.getIntExtra("questionNumber", 0)
        userCorrect = intent.getIntExtra("numCorrect", 0)

        val numberOfQuestion = findViewById<TextView>(R.id.questionNum)
        numberOfQuestion.text = getString(R.string.question_number, questionNum + 1)

        val questionList = quizQuestions.getJSONObject(topic).getJSONArray("QuestionsList")
        val quizQuestion = findViewById<TextView>(R.id.quizQuestion)
        val question = questionList.getJSONObject(questionNum).getString("Question")
        quizQuestion.text = question

        val options = questionList.getJSONObject(questionNum).getJSONArray("AnswerOptions")
        findViewById<RadioButton>(R.id.oneOption).text = options.getString(0)
        findViewById<RadioButton>(R.id.twoOption).text = options.getString(1)
        findViewById<RadioButton>(R.id.threeOption).text = options.getString(2)
        findViewById<RadioButton>(R.id.fourOption).text = options.getString(3)

        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.text = getString(R.string.submit)

        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            userAnswer = when (checkedId) {
                R.id.oneOption -> findViewById<RadioButton>(checkedId).text.toString()
                R.id.twoOption -> findViewById<RadioButton>(checkedId).text.toString()
                R.id.threeOption -> findViewById<RadioButton>(checkedId).text.toString()
                R.id.fourOption -> findViewById<RadioButton>(checkedId).text.toString()
                else -> ""
            }
            submitButton.isEnabled = userAnswer.isNotEmpty()
        }

        submitButton.setOnClickListener {
            val correctAnswer = questionList.getJSONObject(questionNum).getString("Answer")
            val totalQuestions = quizQuestions.getJSONObject(topic).getString("NumberOfQuestions")
            if (userAnswer == correctAnswer) {
                isCorrect = true
                userCorrect += 1
            }
            val intent = Intent(this, AnswerActivity::class.java).apply {
                putExtra("userAnswer", userAnswer)
                putExtra("correctAnswer", correctAnswer)
                putExtra("numCorrect", userCorrect)
                putExtra("totalQuestions", totalQuestions)
                putExtra("questionNumber", questionNum)
                putExtra("topic", topic)
            }
            startActivity(intent)
        }
    }
}
