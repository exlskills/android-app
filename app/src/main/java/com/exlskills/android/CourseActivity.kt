package com.exlskills.android

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.graphics.Palette
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.exlskills.android.remote.CourseMetaAndUnits
import com.exlskills.android.remote.Graph
import com.exlskills.android.remote.GraphCallback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_course.*


import java.util.ArrayList


class CourseActivity : AppCompatActivity() {
    private val gqlApi = Graph()
    private lateinit var courseId: String
    private var course: CourseMetaAndUnits? = null
    private lateinit var collapsingToolbarLayout: CollapsingToolbarLayout
    private lateinit var infoFrag: InfoFragment
    private lateinit var learnFrag: LearningFragment
    private lateinit var helpFrag: LiveHelpFragment
    private val TAG = CourseActivity::class.java.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        courseId = intent.getStringExtra(UIConstants.COURSE_INTENT_KEY_COURSE_ID)
        val courseTitle = intent.getStringExtra(UIConstants.COURSE_INTENT_KEY_COURSE_TITLE)

        setSupportActionBar(findViewById<View>(R.id.htab_toolbar) as Toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        if (courseTitle != null) {
            supportActionBar!!.title = courseTitle
        } else {
            supportActionBar!!.title = "Loading ..."
        }

        val viewPager = findViewById<View>(R.id.htab_viewpager) as ViewPager
        setupViewPager(viewPager)


        val tabLayout = findViewById<View>(R.id.htab_tabs) as TabLayout
        tabLayout.setupWithViewPager(viewPager)

        collapsingToolbarLayout = findViewById<View>(R.id.htab_collapse_toolbar) as CollapsingToolbarLayout
        // Setup the defaults before we have loaded the course image
        collapsingToolbarLayout.setContentScrimColor(
            ContextCompat.getColor(this, R.color.colorPrimary)
        )
        collapsingToolbarLayout.setStatusBarScrimColor(
            ContextCompat.getColor(this, R.color.colorPrimaryDark)
        )

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

                viewPager.currentItem = tab.position
                Log.d(TAG, "onTabSelected: pos: " + tab.position)

                when (tab.position) {
                    0 -> {
                    }
                }// TODO: 31/03/17
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

        loadCourse()
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        infoFrag = InfoFragment(this)
        adapter.addFrag(
            infoFrag, "Info"
        )
        learnFrag = LearningFragment(this)
        adapter.addFrag(
            learnFrag, "Learning"
        )
        helpFrag = LiveHelpFragment(this)
        adapter.addFrag(
            helpFrag, "Live Help"
        )
        viewPager.adapter = adapter
    }

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
//        return true
//    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        when (item.getItemId()) {
//            android.R.id.home -> {
//                finish()
//                return true
//            }
//            R.id.action_settings -> return true
//        }
//        return super.onOptionsItemSelected(item)
//    }

    private class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): Fragment {
            return mFragmentList.get(position)
        }

        override fun getCount(): Int {
            return mFragmentList.size
        }

        fun addFrag(fragment: Fragment, title: String) {
            mFragmentList.add(fragment)
            mFragmentTitleList.add(title)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return mFragmentTitleList.get(position)
        }
    }

    class LearningFragment : Fragment {
        private var activity: CourseActivity? = null

        @SuppressLint("ValidFragment")
        constructor(a: CourseActivity) {
            activity = a
        }

        constructor()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.course_learn_fragment, container, false)
            // Anything for now? TODO show loading?
            return view
        }

        fun courseLoaded() {
            if (view == null) {
                // Edge case, but have to leave because for whatever reason the view is null!
                println("Warning course loaded but view is null, not rendering")
                return
            }

            val linLayout = view!!.findViewById(R.id.unitsLayout) as LinearLayout

            val c = activity!!.course!!

            for (unit in c.units) {
                val uHeading = TextView(activity!!)
                uHeading.text = unit.title
                linLayout.addView(uHeading)
                val sectionsRecycler = RecyclerView(activity!!)
                val linearLayoutManager = LinearLayoutManager(getActivity().baseContext)
                sectionsRecycler.layoutManager = linearLayoutManager
                sectionsRecycler.setHasFixedSize(true)

                val adapter = UnitSectionsAdapter(context, unit.sections_list) { section ->
                    println("Section clicked: " + section.toString())
                }
                sectionsRecycler.adapter = adapter

                linLayout.addView(sectionsRecycler)

                sectionsRecycler.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                sectionsRecycler.requestLayout()

                ViewCompat.setNestedScrollingEnabled(sectionsRecycler, false)
            }
        }
    }

    class LiveHelpFragment : Fragment {
        private lateinit var activity: CourseActivity

        @SuppressLint("ValidFragment")
        constructor(a: CourseActivity) {
            activity = a
        }

        constructor()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.dummy_fragment, container, false)

            val frameLayout = view.findViewById(R.id.dummyfrag_bg) as FrameLayout

            val recyclerView = view.findViewById(R.id.dummyfrag_scrollableview) as RecyclerView

            val linearLayoutManager = LinearLayoutManager(getActivity().baseContext)
            recyclerView.layoutManager = linearLayoutManager
            recyclerView.setHasFixedSize(true)

            val adapter = DessertAdapter(getContext())
            recyclerView.adapter = adapter

            return view
        }
    }

    class InfoFragment : Fragment {
        private lateinit var activity: CourseActivity

        @SuppressLint("ValidFragment")
        constructor(a: CourseActivity) {
            activity = a
        }

        constructor()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            val view = inflater.inflate(R.layout.course_info_fragment, container, false)

            val frameLayout = view.findViewById(R.id.courseInfoFragBackground) as FrameLayout
            // TODO

            return view
        }
    }

    fun loadCourseImage(url: String) {
        Picasso.get().load(url).into(object : com.squareup.picasso.Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                Palette.from(bitmap).generate(object : Palette.PaletteAsyncListener {
                    override fun onGenerated(palette: Palette) {
                        htab_header.setImageBitmap(bitmap)
                        val vibrantColor = palette.getVibrantColor(resources.getColor(R.color.colorPrimary))
                        val vibrantDarkColor = palette.getDarkVibrantColor(resources.getColor(R.color.colorPrimaryDark))
                        collapsingToolbarLayout.setContentScrimColor(vibrantColor)
                        collapsingToolbarLayout.setStatusBarScrimColor(vibrantDarkColor)
                    }
                })
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {}
        })
    }

    fun loadCourse() {
        val self = this
        mainProgressBar.visibility = View.VISIBLE
        htab_viewpager.visibility = View.INVISIBLE
        gqlApi.getDetailedCourseByIdWithEMA(courseId, object: GraphCallback<CourseMetaAndUnits> {
            override fun onFailure(msg: String, code: Int) {
                println("onfailure")
            }
            override fun onSuccess(respData: CourseMetaAndUnits) {
                println("onsuccess")
                println(respData)
                self.runOnUiThread(object: Runnable {
                    override fun run() {
                        course = respData
                        title = respData.meta.title
                        supportActionBar!!.title = respData.meta.title
//                        courseBannerImage.setImageResource(R.drawable.gophergif)
//                        courseTitle.text = respData.meta.title
//                        courseHeadline.text = respData.meta.headline
//                        courseHeadline.text = respData.meta.headline
                        loadCourseImage(respData.meta.logo_url)
                        learnFrag.courseLoaded()
                        mainProgressBar.visibility = View.INVISIBLE
                        htab_viewpager.visibility = View.VISIBLE
                    }
                })
            }
        })
    }

}