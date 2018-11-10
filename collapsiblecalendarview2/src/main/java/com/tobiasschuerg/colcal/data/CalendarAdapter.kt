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
import org.threeten.bp.Month.DECEMBER
import org.threeten.bp.Month.JANUARY
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

    private val mItemList = ArrayList<LocalDate>()
    private val mViewList = ArrayList<View>()
    private val mEventList = ArrayList<Event>()

    val count: Int
        get() = mItemList.size

    init {
        refresh()
    }

    fun getItem(position: Int): LocalDate {
        return mItemList[position]
    }

    fun getView(position: Int): View {
        return mViewList[position]
    }

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
        mItemList.clear()
        mViewList.clear()

        // set calendar
        val year = calendar.year
        val month = calendar.monthValue

        calendar = LocalDate.of(year, month, 1)

        val lastDayOfMonth = calendar.lengthOfMonth()
        val firstDayOfWeek = calendar.dayOfWeek

        // generate day list
        var offset = 0 - (firstDayOfWeek.value - mFirstDayOfWeek.value)
        if (offset > 0) offset += -7
        val length = Math.ceil(((lastDayOfMonth - offset).toFloat() / 7).toDouble()).toInt() * 7
        for (i in offset until length + offset) {
            val numYear: Int
            val numMonth: Int
            val numDay: Int

            when {
                i <= 0             -> { // prev month
                    if (month == JANUARY.value) {
                        numYear = year - 1
                        numMonth = DECEMBER.value
                    } else {
                        numYear = year
                        numMonth = month - 1
                    }
                    val tempCal = LocalDate.of(numYear, numMonth, 1)
                    numDay = tempCal.lengthOfMonth() + i
                }
                i > lastDayOfMonth -> { // next month
                    if (month == DECEMBER.value) {
                        numYear = year + 1
                        numMonth = JANUARY.value
                    } else {
                        numYear = year
                        numMonth = month + 1
                    }
                    numDay = i - lastDayOfMonth
                }
                else               -> {
                    numYear = year
                    numMonth = month
                    numDay = i
                }
            }

            val day = LocalDate.of(numYear, numMonth, numDay)
            val view: View = if (mEventDotSize == UICalendar.EVENT_DOT_SMALL) {
                mInflater.inflate(R.layout.day_layout_small, null)
            } else {
                mInflater.inflate(R.layout.day_layout, null)
            }

            val txtDay = view.findViewById<TextView>(R.id.txt_day)
            val imgEventTag = view.findViewById<ImageView>(R.id.img_event_tag)

            txtDay.text = day.dayOfMonth.toString()
            if (day.month != calendar.month) {
                txtDay.alpha = 0.3f
            }

            for (j in mEventList.indices) {
                val (date, color) = mEventList[j]
                if (day == date) {
                    imgEventTag.visibility = View.VISIBLE
                    imgEventTag.setColorFilter(color, PorterDuff.Mode.SRC_ATOP)
                }
            }

            mItemList.add(day)
            mViewList.add(view)
        }
    }
}
