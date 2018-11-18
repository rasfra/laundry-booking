package com.rf.laundrybooking.web

import java.time.LocalDate

class BookingRequest(val user: String, val roomId: String, val date: LocalDate, val timeSlotId: Int)