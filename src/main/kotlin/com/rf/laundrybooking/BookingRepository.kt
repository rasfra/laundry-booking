package com.rf.laundrybooking

import java.time.LocalDate

interface BookingRepository {
    fun get(timeSlot: TimeSlot): Booking
    fun list(): List<Booking>
    fun book(user: User, room: Room, day: LocalDate, period: SlotPeriod): Booking
    fun cancel(booking: Booking): Boolean

}