package com.exlskills.android

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.exlskills.android.remote.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_course_card.*
import java.util.ArrayList

class CourseCardActivity : AppCompatActivity() {
    private val gqlApi = Graph()
    private lateinit var course: CourseMetaAndUnits
    private lateinit var curUnit: CourseUnit
    private lateinit var curSection: UnitSection
    private lateinit var curCardMeta: SectionCardLiteMeta

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_card)

        course = Gson().fromJson<CourseMetaAndUnits>(intent.getStringExtra(UIConstants.COURSE_CARD_INTENT_KEY_COURSE), CourseMetaAndUnits::class.java)
        curUnit = course.units.find { u -> u.id == intent.getStringExtra(UIConstants.COURSE_CARD_INTENT_KEY_UNIT_ID) }!!
        curSection = curUnit.sections_list.find { s -> s.id == intent.getStringExtra(UIConstants.COURSE_CARD_INTENT_KEY_SECTION_ID) }!!

        setupViewPager(courseCardsPager)
        tabDots.setupWithViewPager(courseCardsPager, true)

        setSupportActionBar(htab_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = curSection.title
    }

    class CardFragment : Fragment {
        private var activity: CourseCardActivity? = null

        @SuppressLint("ValidFragment")
        constructor(a: CourseCardActivity, cardMeta: SectionCardLiteMeta) {
            activity = a
        }

        constructor()

        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View = inflater.inflate(R.layout.course_card_fragment, container, false)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        for (card in curSection.cards_list) {
            adapter.addFrag(
                CardFragment(this, card), card.title
            )
        }
        viewPager.adapter = adapter
    }

    private class ViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val mFragmentList = ArrayList<android.support.v4.app.Fragment>()
        private val mFragmentTitleList = ArrayList<String>()

        override fun getItem(position: Int): android.support.v4.app.Fragment {
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
            // Note, since we are using dotnav, we don't show the titles
            return ""
            // return mFragmentTitleList.get(position)
        }
    }

    fun setNextCard() {
        // TODO
    }

}
