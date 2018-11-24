package com.exlskills.android

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.exlskills.android.com.exlskills.android.remote.CourseMetaAndUnits
import com.exlskills.android.com.exlskills.android.remote.Graph
import com.exlskills.android.com.exlskills.android.remote.GraphCallback
import kotlinx.android.synthetic.main.activity_course.*

class CourseActivity : AppCompatActivity() {
    private val gqlApi = Graph()
    private lateinit var courseId: String;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)
        courseId = intent.getStringExtra("courseId")

        loadCourse()
    }

    fun loadCourse() {
        val self = this
        mainProgressBar.visibility = View.VISIBLE
        courseLayout.visibility = View.INVISIBLE
        gqlApi.getDetailedCourseByIdWithEMA(courseId, object: GraphCallback<CourseMetaAndUnits> {
            override fun onFailure(msg: String, code: Int) {
                println("onfailure")
            }
            override fun onSuccess(respData: CourseMetaAndUnits) {
                println("onsuccess")
                println(respData)
                self.runOnUiThread(object: Runnable {
                    override fun run() {
                        courseBannerImage.setImageResource(R.drawable.gophergif)
                        courseTitle.text = respData.meta.title
                        courseHeadline.text = respData.meta.headline
                        courseHeadline.text = respData.meta.headline
                        mainProgressBar.visibility = View.INVISIBLE
                        courseLayout.visibility = View.VISIBLE
                    }
                })
            }
        })
    }

}