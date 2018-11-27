package com.exlskills.android

import android.annotation.SuppressLint
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.exlskills.android.ids.URLIds.Companion.toUrlId
import com.exlskills.android.remote.*
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_course_card.*
import java.util.ArrayList
import kotlin.math.roundToInt

class CourseCardActivity : AppCompatActivity() {
    private val gqlApi = Graph()
    private lateinit var course: CourseMetaAndUnits
    private lateinit var curUnit: CourseUnit
    private lateinit var curSection: UnitSection
    private lateinit var curCardMeta: SectionCardLiteMeta

    companion object {
        val cardWebViewBaseUrl = "https://exlskills.com/mobile-v1"
    }

    data class CardNavigationParams (
        var nextUnit: CourseUnit?,
        var nextSection: UnitSection?,
        var nextCard: SectionCardLiteMeta?,
        var prevUnit: CourseUnit?,
        var prevSection: UnitSection?,
        var prevCard: SectionCardLiteMeta?
    ) {
        constructor() : this(null, null, null, null, null, null)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_card)

        course = Gson().fromJson<CourseMetaAndUnits>(intent.getStringExtra(UIConstants.COURSE_CARD_INTENT_KEY_COURSE), CourseMetaAndUnits::class.java)
        curUnit = course.units.find { u -> u.id == intent.getStringExtra(UIConstants.COURSE_CARD_INTENT_KEY_UNIT_ID) }!!
        curSection = curUnit.sections_list.find { s -> s.id == intent.getStringExtra(UIConstants.COURSE_CARD_INTENT_KEY_SECTION_ID) }!!

        setInitialCard()

        setupViewPager(courseCardsPager)
        tabDots.setupWithViewPager(courseCardsPager, true)

        setSupportActionBar(htab_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = curSection.title
    }

    fun setInitialCard() {
        var setCard = false
        for (c in curSection.cards_list) {
            if (c.ema.roundToInt() != 100) {
                curCardMeta = c
                setCard = true
            }
        }
        if (setCard) {
            // If we haven't set anything yet, then just go the last one in the series
            curCardMeta = curSection.cards_list[curSection.cards_list.size-1]
        }
    }

    // TODO test this function to get the navigation details
    fun getNextPosition(): CardNavigationParams {
        var nav = CardNavigationParams()
        val curUnitIdx = course.units.indexOf(curUnit)
        val curSectIdx = curUnit.sections_list.indexOf(curSection)
        val curCardIdx = curSection.cards_list.indexOf(curCardMeta)
        // Setup the card navigation
        // TODO
        nav.nextUnit = curUnit
        nav.prevUnit = curUnit
        nav.nextSection = curSection
        nav.prevSection = curSection
        if (curCardIdx+1 < curSection.cards_list.size) {
            nav.nextCard = curSection.cards_list[curCardIdx+1]
        } else {
            if (curSectIdx+1 < curUnit.sections_list.size) {
                // Use the first card of the next section and set next section
                nav.nextSection = curUnit.sections_list[curSectIdx+1]
                nav.nextCard = nav.nextSection!!.cards_list[0]
            } else {
                if (curUnitIdx+1 < course.units.size) {
                    // Use the first card of the first section of the next unit
                    nav.nextUnit = course.units[curUnitIdx+1]
                    nav.nextSection = nav.nextUnit!!.sections_list[0]
                    nav.nextCard = nav.nextSection!!.cards_list[0]
                } else {
                    // END OF COURSE
                    nav.nextUnit = null;
                    nav.nextSection = null;
                    nav.nextCard = null;
                }
            }
        }
        if (curCardIdx-1 > -1) {
            nav.prevCard = curSection.cards_list[curCardIdx-1]
        } else {
            if (curSectIdx-1 > -1) {
                // Use the last card of the prev section and set prev section
                nav.prevSection = curUnit.sections_list[curSectIdx-1];
                nav.prevCard = nav.prevSection!!.cards_list[nav.prevSection!!.cards_list.size-1];
            } else {
                if (curUnitIdx-1 > -1) {
                    // Use the last card of the last section of the prev unit
                    nav.prevUnit = course.units[curUnitIdx-1];
                    nav.prevSection = nav.prevUnit!!.sections_list[nav.prevUnit!!.sections_list.size-1];
                    nav.prevCard = nav.prevSection!!.cards_list[nav.prevSection!!.cards_list.size-1];
                } else {
                    // FIRST CARD OF COURSE
                    nav.prevUnit = null
                    nav.prevSection = null
                    nav.prevCard = null
                }
            }
        }
        return nav
    }

    class CardFragment : Fragment {
        private var activity: CourseCardActivity? = null

        @SuppressLint("ValidFragment")
        constructor(a: CourseCardActivity, cardMeta: SectionCardLiteMeta) {
            activity = a
        }

        constructor()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            val view = inflater.inflate(R.layout.course_card_fragment, container, false)
            val wv = view.findViewById<WebView>(R.id.courseCardWebView)
            wv.settings.domStorageEnabled = true
            wv.settings.javaScriptEnabled = true
            wv.loadUrl("$cardWebViewBaseUrl/learn-en/courses/${toUrlId(activity!!.course.meta.title, activity!!.course.meta.id)}/${toUrlId(activity!!.curUnit.title, activity!!.curUnit.id)}/${toUrlId(activity!!.curSection.title, activity!!.curSection.id)}/${toUrlId(activity!!.curCardMeta.title, activity!!.curCardMeta.id)}")
            return view
        }
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
            // TODO how to manage the colors for completion, etc?
            return ""
            // return mFragmentTitleList.get(position)
        }
    }
}
