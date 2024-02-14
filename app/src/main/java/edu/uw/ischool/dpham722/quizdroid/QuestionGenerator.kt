    package edu.uw.ischool.dpham722.quizdroid

    import android.content.Intent
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.widget.Button
    import android.widget.RadioButton
    import android.widget.RadioGroup
    import android.widget.TextView

    class QuestionGenerator : AppCompatActivity() {

        private var userAnswer: String = ""
        private var userCorrect: Int = 0
        private var isCorrect: Boolean = false

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_questions)

            val topicIndex = intent.getIntExtra("quizTopic", 0)
            val topicChoice = QuizApp.repo.getTopic(topicIndex)

            var questionNum = intent.getIntExtra("questionNumber", 0)
            val numberOfQuestion = findViewById<TextView>(R.id.questionNum)
            val quizQuestion = findViewById<TextView>(R.id.quizQuestion)
            val question = QuizApp.repo.getQuiz(topicIndex, questionNum).question

            userCorrect = intent.getIntExtra("numCorrect", 0)

            numberOfQuestion.text = "Question ${questionNum + 1}"

            quizQuestion.text = question

            val options = QuizApp.repo.getQuiz(topicIndex, questionNum).answers
            findViewById<RadioButton>(R.id.oneOption).text = options[0]
            findViewById<RadioButton>(R.id.twoOption).text = options[1]
            findViewById<RadioButton>(R.id.threeOption).text = options[2]
            findViewById<RadioButton>(R.id.fourOption).text = options[3]

            val submitButton = findViewById<Button>(R.id.submitButton)

            val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
            radioGroup.setOnCheckedChangeListener { _, checkedId ->
                userAnswer = when (checkedId) {
                    R.id.oneOption -> options[0]
                    R.id.twoOption -> options[1]
                    R.id.threeOption -> options[2]
                    R.id.fourOption -> options[3]
                    else -> ""
                }
                submitButton.isEnabled = userAnswer.isNotEmpty()
            }

            submitButton.setOnClickListener {
                val correctAnswerIndex = QuizApp.repo.getQuiz(topicIndex, questionNum).correct
                val correctAnswer = options[correctAnswerIndex]
                if (userAnswer == correctAnswer) {
                    isCorrect = true
                    userCorrect++
                }
                val totalQuestions = topicChoice.quizzes.size
                val intent = Intent(this, AnswerActivity::class.java).apply {
                    putExtra("userAnswer", userAnswer)
                    putExtra("correctAnswer", correctAnswerIndex)
                    putExtra("numCorrect", userCorrect)
                    putExtra("totalQuestions", totalQuestions)
                    putExtra("questionNumber", questionNum)
                    putExtra("quizTopic", topicIndex)
                }
                startActivity(intent)
            }
        }
    }