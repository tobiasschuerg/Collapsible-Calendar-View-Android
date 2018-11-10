package com.tobiasschuerg.colcal.widget

import android.view.View

interface CalendarListener {

    // triggered when a day is selected programmatically or clicked by user.
    fun onDaySelect()

    // triggered only when the views of day on calendar are clicked by user.
    fun onItemClick(v: View)

    // triggered when the data of calendar are updated by changing month or adding events.
    fun onDataUpdate()

    // triggered when the month are changed.
    fun onMonthChange()

    // triggered when the week position are changed.
    fun onWeekChange(position: Int)
}
