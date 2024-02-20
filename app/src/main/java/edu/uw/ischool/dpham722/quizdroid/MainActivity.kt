package edu.uw.ischool.dpham722.quizdroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ListView
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import android.view.MenuItem
import android.util.Log
import android.view.Menu

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val quizView = findViewById<ListView>(R.id.quizView)
        val topics = QuizApp.repo.getTopicChoices()

        quizView.adapter = TopicListAdapter(this, topics)
        quizView.setOnItemClickListener { _, _, position, _ ->
            val intent = Intent(this, TopicOverview::class.java)
            intent.putExtra("quizTopic", position)
            startActivity(intent)
        }
    }
        override fun onResume() {
            super.onResume()
            Log.i("OnResume()", "onResume called")

            val urlPref = findViewById<TextView>(R.id.urlPref)
            val minutePref = findViewById<TextView>(R.id.minutePref)

            val urlInput = intent.getStringExtra("url")
            val minuteInput = intent.getIntExtra("downloadMinute", 0)

            if (!urlInput.isNullOrBlank()) {
                urlPref.text = "URL: $urlInput"
                minutePref.text = "Minute Increment: $minuteInput"
            }
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.dropDown -> {
                val intent = Intent(this, Preferences::class.java)
                startActivity(intent)
                true
            } else -> super.onOptionsItemSelected(item)
        }
    }

}

class TopicListAdapter(private val context: Context, private val topics: List<QuizApp.Topic>) : BaseAdapter() {

    private class ViewHolder(view: View) {
        val topicTextView: TextView = view.findViewById(R.id.topicTextView)
        val shortDescTextView: TextView = view.findViewById(R.id.shortDescTextView)
    }

    override fun getCount(): Int = topics.size

    override fun getItem(position: Int): Any = topics[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val holder: ViewHolder
        val view: View

        if (convertView == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.activity_quizapp, parent, false)
            holder = ViewHolder(view)
            view.tag = holder
        } else {
            view = convertView
            holder = view.tag as ViewHolder
        }

        val topic = getItem(position) as QuizApp.Topic
        holder.topicTextView.text = topic.title
        holder.shortDescTextView.text = topic.desc

        return view
    }
}
