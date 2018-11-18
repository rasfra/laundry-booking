package com.rf.laundrybooking.booking

import com.rf.laundrybooking.schedule.BookingSchedule
import com.rf.laundrybooking.schedule.Room
import com.rf.laundrybooking.schedule.TimeSlot
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

class BookingService(private val repository: BookingRepository,
                     private val schedule: BookingSchedule,
                     private val clock: Clock) {

    fun available(to: LocalDate): List<TimeSlot> {
        val from = LocalDateTime.ofInstant(clock.instant(), clock.zone)
        val booked = repository.list().map { it.timeSlot }
        return schedule.getTimeSlotsIn(from, to.atTime(LocalTime.MAX))
                .filter { !booked.contains(it) }
    }

    fun booked(): List<Booking> = repository.list()

    fun book(user: User, room: Room, date: LocalDate, timeSlotId: Int) =
            repository.book(user, room, date, schedule.getPeriod(timeSlotId))

    fun cancel(id: String): Booking {
        val booking = repository.get(id)
        repository.cancel(booking)
        return booking
    }
}