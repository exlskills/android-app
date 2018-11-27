package com.exlskills.android

import android.annotation.SuppressLint
import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.NavUtils
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import com.exlskills.android.ids.URLIds.Companion.toUrlId
import com.exlskills.android.remote.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
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

        if (intent.getStringExtra(UIConstants.COURSE_CARD_INTENT_KEY_CARD_ID) != null) {
            curCardMeta = curSection.cards_list.find { c -> c.id == intent.getStringExtra(UIConstants.COURSE_CARD_INTENT_KEY_CARD_ID) }!!
        } else {
            setInitialCard()
        }

        setupViewPager(courseCardsPager)
        tabDots.setupWithViewPager(courseCardsPager, true)

        setSupportActionBar(htab_toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title = curSection.title

        val curCardIdx = curSection.cards_list.indexOfFirst { c -> c.id == curCardMeta.id }
        if (curCardIdx != 0) {
            courseCardsPager.setCurrentItem(curCardIdx, true)
        }
    }

    private fun setInitialCard() {
        var setCard = false
        for (c in curSection.cards_list) {
            if (c.ema.roundToInt() != 100) {
                curCardMeta = c
                setCard = true
                break
            }
        }
        if (!setCard) {
            // If we haven't set anything yet, then just go the last one in the series
            curCardMeta = curSection.cards_list[curSection.cards_list.size-1]
        }
    }

    private fun getNavigationParams(): CardNavigationParams {
        var nav = CardNavigationParams()
        val curUnitIdx = course.units.indexOfFirst { u -> u.id == curUnit.id }
        val curSectIdx = curUnit.sections_list.indexOfFirst { s -> s.id == curSection.id }
        val curCardIdx = curSection.cards_list.indexOfFirst { c -> c.id == curCardMeta.id }
        // Setup the card navigation
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
                nav.prevSection = curUnit.sections_list[curSectIdx-1]
                nav.prevCard = nav.prevSection!!.cards_list[nav.prevSection!!.cards_list.size-1]
            } else {
                if (curUnitIdx-1 > -1) {
                    // Use the last card of the last section of the prev unit
                    nav.prevUnit = course.units[curUnitIdx-1]
                    nav.prevSection = nav.prevUnit!!.sections_list[nav.prevUnit!!.sections_list.size-1]
                    nav.prevCard = nav.prevSection!!.cards_list[nav.prevSection!!.cards_list.size-1]
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
        private var cardMeta: SectionCardLiteMeta? = null

        @SuppressLint("ValidFragment")
        constructor(a: CourseCardActivity, cm: SectionCardLiteMeta) {
            activity = a
            cardMeta = cm
        }

        constructor()

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
            val view = inflater.inflate(R.layout.course_card_fragment, container, false)
            val wv = view.findViewById<WebView>(R.id.courseCardWebView)
            wv.settings.domStorageEnabled = true
            wv.settings.javaScriptEnabled = true
            wv.loadUrl("$cardWebViewBaseUrl/learn-en/courses/${toUrlId(activity!!.course.meta.title, activity!!.course.meta.id)}/${toUrlId(activity!!.curUnit.title, activity!!.curUnit.id)}/${toUrlId(activity!!.curSection.title, activity!!.curSection.id)}/${toUrlId(cardMeta!!.title, cardMeta!!.id)}")
            return view
        }
    }

    private fun setupViewPager(viewPager: CustomViewPagerEndSwipe) {
        val adapter = ViewPagerAdapter(supportFragmentManager)
        for (card in curSection.cards_list) {
            adapter.addFrag(
                CardFragment(this, card), card.title
            )
        }
        viewPager.adapter = adapter
        viewPager.setOnSwipeOutListener(object: CustomViewPagerEndSwipe.OnSwipeOutListener {
            override fun onSwipeOutAtEnd() {
                navigateNext()
            }

            override fun onSwipeOutAtStart() {
                navigatePrev()
            }
        })

        viewPager.addOnPageChangeListener(object: ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                curCardMeta = curSection.cards_list[position]
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) { }

            override fun onPageScrollStateChanged(state: Int) { }
        })
    }

    fun navigateNext() {
        val nextNav = getNavigationParams()
        if (nextNav.nextCard == null || nextNav.nextSection == null || nextNav.nextUnit == null) {
            // This will take us back out to the course page if there is a back history
            NavUtils.navigateUpFromSameTask(this)
        } else {
            val intent = Intent(this, CourseCardActivity::class.java)
            intent.putExtra(UIConstants.COURSE_CARD_INTENT_KEY_COURSE, GsonBuilder().create().toJson(course))
            intent.putExtra(UIConstants.COURSE_CARD_INTENT_KEY_UNIT_ID, nextNav.nextUnit!!.id)
            intent.putExtra(UIConstants.COURSE_CARD_INTENT_KEY_SECTION_ID, nextNav.nextSection!!.id)
            intent.putExtra(UIConstants.COURSE_CARD_INTENT_KEY_CARD_ID, nextNav.nextCard!!.id)
            startActivity(intent)
        }
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

    fun navigatePrev() {
        val prevNav = getNavigationParams()
        if (prevNav.prevCard == null || prevNav.prevSection == null || prevNav.prevUnit == null) {
            // This will take us back out to the course page if there is a back history
            NavUtils.navigateUpFromSameTask(this)
        } else {
            val intent = Intent(this, CourseCardActivity::class.java)
            intent.putExtra(UIConstants.COURSE_CARD_INTENT_KEY_COURSE, GsonBuilder().create().toJson(course))
            intent.putExtra(UIConstants.COURSE_CARD_INTENT_KEY_UNIT_ID, prevNav.prevUnit!!.id)
            intent.putExtra(UIConstants.COURSE_CARD_INTENT_KEY_SECTION_ID, prevNav.prevSection!!.id)
            intent.putExtra(UIConstants.COURSE_CARD_INTENT_KEY_CARD_ID, prevNav.prevCard!!.id)
            startActivity(intent)
        }
        overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
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
