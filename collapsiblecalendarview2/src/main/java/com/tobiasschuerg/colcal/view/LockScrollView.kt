package com.tobiasschuerg.colcal.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ScrollView

class LockScrollView(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ScrollView(context, attrs, defStyleAttr) {

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        return false
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return false
    }

}
