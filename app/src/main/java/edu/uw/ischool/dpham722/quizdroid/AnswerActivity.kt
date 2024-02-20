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

        val userAnswer = intent.getStringExtra("userAnswer")
        val correctquestiontAnswer = intent.getIntExtra("correctAnswer", 0)
        val numCorrect = intent.getIntExtra("numCorrect", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 0)
        val questionNumber = intent.getIntExtra("questionNumber", 0)
        val topic = intent.getIntExtra("quizTopic", 0)
        val questionAnswer = QuizApp.repo.getQuiz(topic, questionNumber).answers[correctquestiontAnswer]

        val youAnsweredTextView = findViewById<TextView>(R.id.userAnswer)
        val correctAnswerTextView = findViewById<TextView>(R.id.correctAnswer)
        val numOutOfNumTextView = findViewById<TextView>(R.id.totalQuestionsCorrect)
        val nextButton = findViewById<Button>(R.id.nextOrFinishButton)

        youAnsweredTextView.text = userAnswer
        correctAnswerTextView.text = questionAnswer
        numOutOfNumTextView.text = getString(R.string.you_have_correct_out_of_total, numCorrect, totalQuestions)

        if ((questionNumber + 1) == totalQuestions) {
            nextButton.text = getString(R.string.finish)
        }

        nextButton.setOnClickListener {
            if (nextButton.text == getString(R.string.finish)) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, QuestionGenerator::class.java)
                intent.putExtra("numCorrect", numCorrect)
                intent.putExtra("questionNumber", questionNumber + 1)
                intent.putExtra("quizTopic", topic)
                startActivity(intent)
            }
        }
    }
}