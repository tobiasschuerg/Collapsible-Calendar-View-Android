package com.tobiasschuerg.colcal.data

import org.threeten.bp.LocalDate

data class Event(
        val date: LocalDate,
        val color: Int? = null
)
