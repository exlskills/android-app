package com.exlskills.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.Group
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.exlskills.android.com.exlskills.android.remote.Course
import com.exlskills.android.com.exlskills.android.remote.Graph
import com.exlskills.android.com.exlskills.android.remote.GraphCallback

class MainActivity : AppCompatActivity() {
    private val gqlApi = Graph()
    private lateinit var textField: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var courseListGroup: Group

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Setup view elements
        this.textField = findViewById(R.id.foobartextview)
        this.progressBar = findViewById(R.id.foobarprogview)
        this.courseListGroup = findViewById(R.id.courseListGroup)
        val self = this

        gqlApi.getAllCourses("relevant", object: GraphCallback<List<Course>> {
            override fun onFailure(msg: String, code: Int) {
                println("onfailure")
            }
            override fun onSuccess(respData: List<Course>) {
                println("onsuccess")
                println(respData)
                self.runOnUiThread(object: Runnable {
                    override fun run() {
                        progressBar.visibility = View.GONE
                        courseListGroup.visibility = View.VISIBLE
                    }
                })
            }
        })
        changeText()
    }

    fun changeText() {
        textField.setText("Foobar");
    }
}
