package com.rf.laundrybooking.booking

import com.rf.laundrybooking.schedule.Room
import com.rf.laundrybooking.schedule.TimePeriod
import com.rf.laundrybooking.schedule.TimeSlot
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicInteger

class InMemoryBookingRepository : BookingRepository {
    private val idGen = AtomicInteger(0)
    private val bookings = ArrayList<Booking>()

    override fun get(id: String): Booking {
        val booking = bookings.find { it.id == id }
        if (booking != null) {
            return booking
        } else {
            throw IllegalArgumentException("No booking exists with id $id")
        }
    }

    override fun list(): List<Booking> = bookings

    override fun book(user: User, room: Room, day: LocalDate, period: TimePeriod): Booking {
        val timeSlot = TimeSlot(room, day, period)
        if (bookings.any { it.timeSlot == timeSlot }) {
            throw IllegalArgumentException("$timeSlot already booked!")
        } else {
            val booking = Booking(idGen.getAndIncrement().toString(), user, timeSlot)
            bookings.add(booking)
            return booking
        }
    }

    override fun cancel(booking: Booking): Boolean {
        val found = bookings.find { it == booking }
        return if (found != null) {
            bookings.remove(found)
            true
        } else {
            false
        }
    }
}

