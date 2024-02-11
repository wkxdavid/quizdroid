package edu.uw.ischool.dpham722.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class AnswerActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answer)

        val userAnswer = intent.getStringExtra("userAnswer") ?: ""
        val correctAnswer = intent.getStringExtra("correctAnswer") ?: ""
        val numCorrect = intent.getIntExtra("numCorrect", 0)
        val totalQuestions = intent.getStringExtra("totalQuestions") ?: ""
        val questionNumber = intent.getIntExtra("questionNumber", 0)
        val topic = intent.getStringExtra("topic") ?: ""

        val youAnsweredTextView = findViewById<TextView>(R.id.userAnswer)
        val correctAnswerTextView = findViewById<TextView>(R.id.correctAnswer)
        val numOutOfNumTextView = findViewById<TextView>(R.id.totalQuestionsCorrect)
        val nextButton = findViewById<Button>(R.id.nextOrFinishButton)

        youAnsweredTextView.text = userAnswer
        correctAnswerTextView.text = correctAnswer
        numOutOfNumTextView.text = getString(R.string.questions_correct, numCorrect, totalQuestions)

        nextButton.text = if (questionNumber + 1 == totalQuestions.toInt()) {
            getString(R.string.finish)
        } else {
            getString(R.string.next)
        }

        nextButton.setOnClickListener {
            if (questionNumber + 1 == totalQuestions.toInt()) {
                startActivity(Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            } else {
                Intent(this, QuestionGenerator::class.java).also {
                    it.putExtra("numCorrect", numCorrect)
                    it.putExtra("questionNumber", questionNumber + 1)
                    it.putExtra("quizTopic", topic)
                    startActivity(it)
                }
            }
        }
    }
}
