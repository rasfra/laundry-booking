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
        val booked = repository.list().map { it.timeSlot }
        return schedule.getTimeSlotsIn(now(), to.atTime(LocalTime.MAX))
                .filter { !booked.contains(it) }
    }

    fun booked(): List<Booking> = repository.list().filter { isNowOrFuture(LocalDateTime.of(it.timeSlot.day, it.timeSlot.timePeriod.end)) }

    fun book(user: User, room: Room, date: LocalDate, timeSlotId: Int): Booking {
        val period = schedule.getPeriod(timeSlotId)
        require(isNowOrFuture(LocalDateTime.of(date, period.end))) { "Cannot book in the past" }
        return repository.book(user, room, date, period)
    }

    fun cancel(id: String): Booking {
        val booking = repository.get(id)
        repository.cancel(booking)
        return booking
    }

    private fun now() = LocalDateTime.ofInstant(clock.instant(), clock.zone)

    private fun isNowOrFuture(date: LocalDateTime) = date.isEqual(now()) || date.isAfter(now())
}