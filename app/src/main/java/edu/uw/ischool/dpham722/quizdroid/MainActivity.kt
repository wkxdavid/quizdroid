package edu.uw.ischool.dpham722.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val quizView = findViewById<ListView>(R.id.quizView)
        val names = arrayOf("Math", "Physics", "Marvel Super Heroes")

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(
            this, android.R.layout.simple_list_item_1, names
        )

        quizView.adapter = arrayAdapter
        quizView.setOnItemClickListener{ _, _, position, _ ->
            val selectedItem = names[position]
            val intent = Intent(this, TopicOverview::class.java)
            intent.putExtra("quizTopic", selectedItem)
            startActivity(intent)
        }

    }
}