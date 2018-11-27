package com.exlskills.android

import android.content.Context
import android.view.MotionEvent
import android.support.v4.view.ViewPager
import android.util.AttributeSet

// Ref (mostly): https://stackoverflow.com/a/39094886/4858295
class CustomViewPagerEndSwipe(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    private var mStartDragX: Float = 0.toFloat()
    private var mListener: OnSwipeOutListener? = null
    private val swipeThreshold: Float = 30F

    fun setOnSwipeOutListener(listener: OnSwipeOutListener) {
        mListener = listener
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val x = ev.x
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> mStartDragX = x
            MotionEvent.ACTION_MOVE -> if (mListener != null && mStartDragX + swipeThreshold < x && currentItem == 0) {
                println("LT start: $mStartDragX cur: $x")
                mListener!!.onSwipeOutAtStart()
            } else if (mListener != null && mStartDragX > x + swipeThreshold && currentItem == adapter.count - 1) {
                println("GT start: $mStartDragX cur: $x")
                mListener!!.onSwipeOutAtEnd()
            } else {
                println("EL start: $mStartDragX cur: $x")
            }
        }
        return super.onInterceptTouchEvent(ev)
    }

    interface OnSwipeOutListener {
        fun onSwipeOutAtStart()
        fun onSwipeOutAtEnd()
    }
}