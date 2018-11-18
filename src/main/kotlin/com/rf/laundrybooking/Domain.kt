package com.rf.laundrybooking

import java.time.LocalDate
import java.time.LocalTime

data class Room(val id: Int)

/**
 * A discrete time slot in a specific room that can be booked
 */
data class TimeSlot(val room: Room, val day: LocalDate, val slotPeriod: SlotPeriod)

/**
 * A bookable time period for any given day. The SlotPeriod for different days are interchangeable.
 */
data class SlotPeriod(val id: Int, val start: LocalTime, val end: LocalTime)

data class User(val name: String)

data class Booking(val user: User, val timeSlot: TimeSlot)