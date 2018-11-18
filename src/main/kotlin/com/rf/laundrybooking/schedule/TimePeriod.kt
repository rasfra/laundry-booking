package com.rf.laundrybooking.schedule

import java.time.LocalTime

/**
 * A bookable time period for any given day. The period for different days are interchangeable.
 */
data class TimePeriod(val id: Int, val start: LocalTime, val end: LocalTime) {
    override fun toString() = "$start-$end"
}