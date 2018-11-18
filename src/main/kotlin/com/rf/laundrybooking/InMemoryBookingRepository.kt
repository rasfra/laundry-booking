package com.rf.laundrybooking

import java.time.LocalDate

class InMemoryBookingRepository : BookingRepository {
    private val bookings = ArrayList<Booking>()

    override fun get(timeSlot: TimeSlot): Booking {
        val booking = bookings.find { it.timeSlot == timeSlot }
        if (booking != null) {
            return booking
        } else {
            throw IllegalArgumentException("No booking exists for $timeSlot")
        }
    }

    override fun list(): List<Booking> = bookings

    override fun book(user: User, room: Room, day: LocalDate, period: SlotPeriod): Booking {
        val timeSlot = TimeSlot(room, day, period)
        if (bookings.any { it.timeSlot == timeSlot }) {
            throw IllegalArgumentException("$timeSlot already booked!")
        } else {
            val booking = Booking(user, timeSlot)
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

