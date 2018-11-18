package com.rf.laundrybooking.booking

import com.rf.laundrybooking.schedule.TimeSlot

data class Booking(val id: String, val user: User, val timeSlot: TimeSlot) {
    override fun toString() = "Booking $id, $timeSlot by $user"
}