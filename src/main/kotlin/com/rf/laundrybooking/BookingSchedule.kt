package com.rf.laundrybooking

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.temporal.ChronoUnit

interface BookingSchedule {
    /**
     * Generates all possible time slots in all rooms between two days (inclusive) sorted by day, time and room
     */
    fun getTimeSlotsIn(from: LocalDateTime, to: LocalDateTime): List<TimeSlot>

    fun getPeriod(id: Int): SlotPeriod
}

class DefaultBookingSchedule : BookingSchedule {
    val slotPeriods: List<SlotPeriod>
    private val roomRepository: RoomRepository

    constructor(roomRepository: RoomRepository, start: Int, durationHrs: List<Int>) {
        this.roomRepository = roomRepository
        require(durationHrs.isNotEmpty()) { "Duration hours may not be empty" }
        require(start + durationHrs.sum() <= 24) { "Sum of hours exceeds a day" }
        val list = ArrayList<SlotPeriod>()
        var hour = start
        // Generate SlotPeriods according to the schema
        for ((id, duration) in durationHrs.withIndex()) {
            list.add(SlotPeriod(id, LocalTime.of(hour, 0), LocalTime.of(hour + duration, 0)))
            hour += duration
        }
        slotPeriods = list
    }


    override fun getTimeSlotsIn(from: LocalDateTime, to: LocalDateTime): List<TimeSlot> {
        val days = (0..ChronoUnit.DAYS.between(from.toLocalDate(), to.toLocalDate()))
                .map { from.toLocalDate().plusDays(it) }
        val rooms = roomRepository.rooms()

        return days.flatMap { day ->
            slotPeriods.filter { periodInRange(day, it, from, to) }
                    .flatMap { period ->
                        rooms.map { TimeSlot(it, day, period) }
                    }
        }
    }

    private fun periodInRange(day: LocalDate, period: SlotPeriod, from: LocalDateTime, to: LocalDateTime): Boolean {
        return LocalDateTime.of(day, period.end).isAfter(from) && // Get rid if times that ends before $from
                LocalDateTime.of(day, period.start).isBefore(to) // Get rid of times that starts after $to
    }

    override fun getPeriod(id: Int): SlotPeriod {
        require(id < slotPeriods.size) { "Invalid slot period id" }
        return slotPeriods[id]
    }
}