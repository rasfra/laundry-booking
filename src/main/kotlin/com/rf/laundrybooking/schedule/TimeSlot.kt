package com.rf.laundrybooking.schedule

import java.time.LocalDate

/**
 * A discrete time slot in a specific room that can be booked
 */
data class TimeSlot(val room: Room, val day: LocalDate, val timePeriod: TimePeriod) {
    override fun toString() = "TimeSlot $day $timePeriod in room $room"
}