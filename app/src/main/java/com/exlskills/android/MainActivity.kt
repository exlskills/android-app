package com.exlskills.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.Group
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.exlskills.android.com.exlskills.android.remote.CourseLiteMeta
import com.exlskills.android.com.exlskills.android.remote.Graph
import com.exlskills.android.com.exlskills.android.remote.GraphCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val gqlApi = Graph()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private var adapter: RecyclerView.Adapter<CardRecyclerAdapter.ViewHolder>? = null
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
        layoutManager = LinearLayoutManager(this)
        recycler_view.layoutManager = layoutManager

        adapter = CardRecyclerAdapter()
        recycler_view.adapter = adapter

        loadData()
    }

    fun loadData() {
        val self = this
        gqlApi.getAllCourses("mine", object: GraphCallback<List<CourseLiteMeta>> {
            override fun onFailure(msg: String, code: Int) {
                println("onfailure")
            }
            override fun onSuccess(respData: List<CourseLiteMeta>) {
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
    }
}
