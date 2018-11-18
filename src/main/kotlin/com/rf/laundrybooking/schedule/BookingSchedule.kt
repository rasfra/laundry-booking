package com.rf.laundrybooking.schedule

import java.time.LocalDateTime

interface BookingSchedule {
    /**
     * Generates all possible time slots in all rooms between two days (inclusive) sorted by day, time and room
     */
    fun getTimeSlotsIn(from: LocalDateTime, to: LocalDateTime): List<TimeSlot>

    fun getPeriod(id: Int): TimePeriod
}

