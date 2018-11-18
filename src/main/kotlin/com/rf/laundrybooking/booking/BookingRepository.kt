package com.rf.laundrybooking.booking

import com.rf.laundrybooking.schedule.Room
import com.rf.laundrybooking.schedule.TimePeriod
import java.time.LocalDate

interface BookingRepository {
    fun get(id: String): Booking
    fun list(): List<Booking>
    fun book(user: User, room: Room, day: LocalDate, period: TimePeriod): Booking
    fun cancel(booking: Booking): Boolean
}