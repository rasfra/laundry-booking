package com.rf.laundrybooking

import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Suppress("unused")
@RestController("laundrybooking")
class BookingController(
        private val service: BookingService
) {

    @GetMapping("/available")
    fun available(@RequestParam toDate: LocalDate): List<TimeSlot> {
        return service.available(toDate)
    }

    @PostMapping("/book")
    fun book(@RequestBody request: BookingRequest): Response<Booking> {
        return try {
            val response = service.book(User(request.user), Room(request.roomId), request.date, request.timeSlotId)
            Response(response)
        } catch (e: Exception) {
            Response(e.toString())
        }
    }

    @GetMapping("/booked")
    fun booked(): List<Booking> {
        return service.booked()
    }

    @PostMapping("/cancel")
    fun cancel(@RequestBody request: CancelRequest): Response<Booking> {
        return try {
            val response = service.cancel(Room(request.roomId), request.date, request.timeSlotId)
            Response(response)
        } catch (e: Exception) {
            Response(e.toString())
        }
    }

    class BookingRequest(val user: String, val roomId: Int, val date: LocalDate, val timeSlotId: Int)
    class CancelRequest(val roomId: Int, val date: LocalDate, val timeSlotId: Int)

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