package com.rf.laundrybooking.web

import com.rf.laundrybooking.booking.Booking
import com.rf.laundrybooking.booking.BookingService
import com.rf.laundrybooking.booking.User
import com.rf.laundrybooking.schedule.Room
import com.rf.laundrybooking.schedule.TimeSlot
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Suppress("unused")
@RestController
@RequestMapping("laundrybooking")
class BookingController(
        private val service: BookingService
) {

    @GetMapping("available")
    fun available(@RequestParam("toDate") toDate: LocalDate): List<TimeSlot> {
        return service.available(toDate)
    }

    @PutMapping("book")
    fun book(@RequestBody request: BookingRequest): Response<Booking> {
        return try {
            val response = service.book(User(request.user), Room(request.roomId), request.date, request.timeSlotId)
            Response(response)
        } catch (e: Exception) {
            Response(e.toString())
        }
    }

    @GetMapping("booked")
    fun booked(): List<Booking> {
        return service.booked()
    }

    @DeleteMapping("cancel/{id}")
    fun cancel(@PathVariable id: String): Response<Booking> {
        return try {
            val response = service.cancel(id)
            Response(response)
        } catch (e: Exception) {
            Response(e.toString())
        }
    }

    class Response<T> {
        var data: T? = null
        var error: String? = null

        constructor(data: T) {
            this.data = data
        }

        constructor(error: String) {
            this.error = error
        }
    }
}