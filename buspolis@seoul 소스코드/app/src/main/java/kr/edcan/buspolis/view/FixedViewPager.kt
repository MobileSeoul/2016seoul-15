package kr.edcan.buspolis.view

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by LNTCS on 2016-10-24.
 */
class FixedViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    var swipable: Boolean = false

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (this.swipable) {
            return super.onTouchEvent(event)
        }
        return false
    }

    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        if (this.swipable) {
            return super.onInterceptTouchEvent(event)
        }
        return false
    }

    fun setPagingEnabled() {
        this.swipable = true
    }

    fun setPagingDisabled() {
        this.swipable = false
    }

}