package com.exlskills.android

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.exlskills.android.com.exlskills.android.remote.CourseLiteMeta
import com.exlskills.android.com.exlskills.android.remote.DigitalDiploma
import com.exlskills.android.com.exlskills.android.remote.Graph
import com.exlskills.android.com.exlskills.android.remote.GraphCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private val gqlApi = Graph()
    private var currentTab: String = UIConstants.BOTTOM_NAV_HOME
    private var coursesAdapter: RecyclerView.Adapter<CourseCardRecyclerAdapter.CourseViewHolder>? = null
    private var homeAdapter: RecyclerView.Adapter<CourseCardRecyclerAdapter.CourseViewHolder>? = null
    private var projectsAdapter: RecyclerView.Adapter<ProjectCardRecyclerAdapter.ProjectViewHolder>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        homeRecyclerView.layoutManager = LinearLayoutManager(this)
        coursesRecyclerView.layoutManager = LinearLayoutManager(this)
        projectsRecyclerView.layoutManager = LinearLayoutManager(this)

        mainBottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.title) {
                UIConstants.BOTTOM_NAV_HOME -> {
                    if (currentTab == UIConstants.BOTTOM_NAV_HOME) {
                        println("Already on home tab, no-op")
                    } else {
                        currentTab = UIConstants.BOTTOM_NAV_HOME
                        loadHome()
                    }
                }
                UIConstants.BOTTOM_NAV_PROJECTS -> {
                    if (currentTab == UIConstants.BOTTOM_NAV_PROJECTS) {
                        println("Already on projects tab, no-op")
                    } else {
                        currentTab = UIConstants.BOTTOM_NAV_PROJECTS
                        loadProjects()
                    }
                }
                UIConstants.BOTTOM_NAV_COURSES -> {
                    if (currentTab == UIConstants.BOTTOM_NAV_COURSES) {
                        println("Already on courses tab, no-op")
                    } else {
                        currentTab = UIConstants.BOTTOM_NAV_COURSES
                        loadCourses()
                    }
                }
            }
            println(item)
            true
        }

        loadHome()
    }

    fun hideLayouts() {
        coursesLayout.visibility = View.INVISIBLE
        projectsLayout.visibility = View.INVISIBLE
        homeLayout.visibility = View.INVISIBLE
    }

    fun loadHome() {
        val self = this
        mainProgressBar.visibility = View.VISIBLE
        hideLayouts()
        gqlApi.getAllCourses("mine", object: GraphCallback<List<CourseLiteMeta>> {
            override fun onFailure(msg: String, code: Int) {
                println("onfailure")
            }
            override fun onSuccess(respData: List<CourseLiteMeta>) {
                println("onsuccess")
                println(respData)
                self.runOnUiThread(object: Runnable {
                    override fun run() {
                        homeAdapter = CourseCardRecyclerAdapter(respData) { courseId ->
                            val intent = Intent(self, CourseActivity::class.java)
                            intent.putExtra("courseId", courseId)
                            startActivity(intent)
                        }
                        homeRecyclerView.adapter = homeAdapter
                        mainProgressBar.visibility = View.INVISIBLE
                        homeLayout.visibility = View.VISIBLE
                    }
                })
            }
        })
    }

    fun loadCourses() {
        val self = this
        mainProgressBar.visibility = View.VISIBLE
        hideLayouts()
        gqlApi.getAllCourses("relevant", object: GraphCallback<List<CourseLiteMeta>> {
            override fun onFailure(msg: String, code: Int) {
                println("onfailure")
            }
            override fun onSuccess(respData: List<CourseLiteMeta>) {
                println("onsuccess")
                println(respData)
                self.runOnUiThread(object: Runnable {
                    override fun run() {
                        coursesAdapter = CourseCardRecyclerAdapter(respData) { courseId ->
                            val intent = Intent(self, CourseActivity::class.java)
                            intent.putExtra("courseId", courseId)
                            startActivity(intent)
                        }
                        coursesRecyclerView.adapter = coursesAdapter
                        mainProgressBar.visibility = View.INVISIBLE
                        coursesLayout.visibility = View.VISIBLE
                    }
                })
            }
        })
    }

    fun loadProjects() {
        val self = this
        mainProgressBar.visibility = View.VISIBLE
        hideLayouts()
        gqlApi.getAllDigitalDiplomas(object: GraphCallback<List<DigitalDiploma>> {
            override fun onFailure(msg: String, code: Int) {
                println("onfailure")
            }
            override fun onSuccess(respData: List<DigitalDiploma>) {
                println("onsuccess")
                println(respData)
                self.runOnUiThread(object: Runnable {
                    override fun run() {
                        projectsAdapter = ProjectCardRecyclerAdapter(respData)
                        projectsRecyclerView.adapter = projectsAdapter
                        mainProgressBar.visibility = View.INVISIBLE
                        projectsLayout.visibility = View.VISIBLE
                    }
                })
            }
        })
    }
}
