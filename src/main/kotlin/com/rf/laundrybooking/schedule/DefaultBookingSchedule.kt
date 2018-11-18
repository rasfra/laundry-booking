package com.rf.laundrybooking.schedule

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

class DefaultBookingSchedule : BookingSchedule {
    val timePeriods: List<TimePeriod>
    private val roomRepository: RoomRepository

    constructor(roomRepository: RoomRepository, start: Int, durationHrs: List<Int>) {
        this.roomRepository = roomRepository
        require(durationHrs.isNotEmpty()) { "Duration hours may not be empty" }
        require(start + durationHrs.sum() <= 24) { "Sum of hours exceeds a day" }
        val list = ArrayList<TimePeriod>()
        var hour = start
        // Generate TimePeriods according to the schema
        for ((id, duration) in durationHrs.withIndex()) {
            list.add(TimePeriod(id, LocalTime.of(hour, 0), LocalTime.of(hour + duration, 0)))
            hour += duration
        }
        timePeriods = list
    }


    override fun getTimeSlotsIn(from: LocalDateTime, to: LocalDateTime): List<TimeSlot> {
        val days = (0..ChronoUnit.DAYS.between(from.toLocalDate(), to.toLocalDate()))
                .map { from.toLocalDate().plusDays(it) }
        val rooms = roomRepository.rooms()

        return days.flatMap { day ->
            timePeriods.filter { periodInRange(day, it, from, to) }
                    .flatMap { period ->
                        rooms.map { TimeSlot(it, day, period) }
                    }
        }
    }

    private fun periodInRange(day: LocalDate, period: TimePeriod, from: LocalDateTime, to: LocalDateTime): Boolean {
        return LocalDateTime.of(day, period.end).isAfter(from) && // Get rid if times that ends before $from
                LocalDateTime.of(day, period.start).isBefore(to) // Get rid of times that starts after $to
    }

    override fun getPeriod(id: Int): TimePeriod {
        require(id < timePeriods.size) { "Invalid slot period id" }
        return timePeriods[id]
    }
}