package com.tobiasschuerg.colcal.data

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tobiasschuerg.colcal.R
import com.tobiasschuerg.colcal.widget.UICalendar
import org.threeten.bp.DayOfWeek
import org.threeten.bp.LocalDate
import java.util.*

/**
 * Created by shrikanthravi on 06/03/18.
 */

class CalendarAdapter(context: Context) {
    private var mFirstDayOfWeek = DayOfWeek.MONDAY
    var calendar: LocalDate = LocalDate.now()
        private set
    private val mInflater: LayoutInflater by lazy { LayoutInflater.from(context) }
    private var mEventDotSize = UICalendar.EVENT_DOT_BIG

    private val dayList: MutableList<LocalDate> = mutableListOf()
    private val mViewList: MutableList<View> = mutableListOf()
    private val mEventList = ArrayList<Event>()

    fun count(): Int = dayList.size

    init {
        refresh()
    }

    fun getItem(position: Int): LocalDate = dayList[position]

    fun getView(position: Int): View = mViewList[position]

    fun nextMonth() {
        calendar = calendar.plusMonths(1)
    }

    fun previousMonth() {
        calendar = calendar.minusMonths(1)
    }

    fun setDate(date: LocalDate) {
        calendar = date
    }

    fun setFirstDayOfWeek(firstDayOfWeek: DayOfWeek) {
        mFirstDayOfWeek = firstDayOfWeek
    }

    fun setEventDotSize(eventDotSize: Int) {
        mEventDotSize = eventDotSize
    }

    fun addEvent(event: Event) {
        mEventList.add(event)
    }

    fun refresh() {
        // clear data
        dayList.clear()
        mViewList.clear()

        calendar = calendar.withDayOfMonth(1)

        val lastDayOfMonth = calendar.lengthOfMonth()
        val firstDayOfWeek = calendar.dayOfWeek

        // generate day list
        var offset: Long = 0L - (firstDayOfWeek.value - mFirstDayOfWeek.value)
        if (offset > 0) offset += -7

        var date = calendar.plusDays(offset - 1)

        val length = Math.ceil(((lastDayOfMonth - offset).toFloat() / 7).toDouble()).toInt() * 7
        for (i in offset until length + offset) {
            date = date.plusDays(1)
            dayList.add(date)
        }

        mViewList.addAll(dayList.map { createView(it) })
    }

    private fun createView(date: LocalDate): View {
        val view: View = if (mEventDotSize == UICalendar.EVENT_DOT_SMALL) {
            mInflater.inflate(R.layout.day_layout_small, null)
        } else {
            mInflater.inflate(R.layout.day_layout, null)
        }

        val txtDay = view.findViewById<TextView>(R.id.txt_day)
        val imgEventTag = view.findViewById<ImageView>(R.id.img_event_tag)

        txtDay.text = date.dayOfMonth.toString()
        if (date.month != calendar.month) {
            txtDay.alpha = 0.3f
        }

        mEventList.find { it.date == date }?.let {
            imgEventTag.visibility = View.VISIBLE
            imgEventTag.setColorFilter(it.color, PorterDuff.Mode.SRC_ATOP)
        }

        return view
    }
}
