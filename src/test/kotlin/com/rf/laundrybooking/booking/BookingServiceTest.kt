package com.rf.laundrybooking.booking

import com.rf.laundrybooking.schedule.DefaultBookingSchedule
import com.rf.laundrybooking.schedule.DefaultRoomRepository
import com.rf.laundrybooking.schedule.Room
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.*

class BookingServiceTest {
    private val schedule = DefaultBookingSchedule(DefaultRoomRepository(2), 7, listOf(5, 4, 3, 3))
    val earlyMorning = LocalDateTime.of(2018, 1, 1, 6, 0)
    private val bookingService = BookingService(InMemoryBookingRepository(), schedule, clockAt(earlyMorning))
    val user = User("user")

    @Test
    fun listAvailable() {
        val available = bookingService.available(earlyMorning.toLocalDate())
        assertEquals(4 * 2, available.size)
        assertEquals(Room("1"), available.first().room)
        assertEquals(7, available.first().timePeriod.start.hour)
        assertEquals(12, available.first().timePeriod.end.hour)
    }

    @Test
    fun listAvailableAtMidOfDay() {
        val bookingService = BookingService(InMemoryBookingRepository(), schedule,
                clockAt(LocalDateTime.of(2018, 1, 1, 15, 0)))

        val available = bookingService.available(LocalDate.of(2018, 1, 1))
        assertEquals(3 * 2, available.size)
        assertEquals(Room("1"), available.first().room)
        assertEquals(12, available.first().timePeriod.start.hour)
        assertEquals(16, available.first().timePeriod.end.hour)
    }

    @Test
    fun bookSuccessfully() {
        val available = bookingService.available(earlyMorning.toLocalDate())
        assert(available.isNotEmpty())

        val slotToBook = available.first()
        bookingService.book(user, slotToBook.room, slotToBook.day, slotToBook.timePeriod.id)

        val availableAfterBooking = bookingService.available(earlyMorning.toLocalDate())
        assertEquals(ArrayList(available).apply { remove(slotToBook) }, availableAfterBooking)

        val booked = bookingService.booked()
        assertEquals(Booking("0", user, slotToBook), booked.first())
    }

    @Test(expected = IllegalArgumentException::class)
    fun doubleBookingFails() {
        val available = bookingService.available(earlyMorning.toLocalDate())
        assert(available.isNotEmpty())
        val slotToBook = available.first()
        bookingService.book(user, slotToBook.room, slotToBook.day, slotToBook.timePeriod.id)
        bookingService.book(user, slotToBook.room, slotToBook.day, slotToBook.timePeriod.id)
    }

    @Test
    fun canBookDifferentRoomsAtSameTime() {
        val available = bookingService.available(earlyMorning.toLocalDate())
        assert(available.isNotEmpty())
        val slotToBook = available.first()
        bookingService.book(user, Room("1"), slotToBook.day, slotToBook.timePeriod.id)
        bookingService.book(user, Room("2"), slotToBook.day, slotToBook.timePeriod.id)

        assertEquals(2, bookingService.booked().size)
    }

    @Test
    fun listBooked() {
        bookSuccessfully()
        val booked = bookingService.booked()
        assertEquals(1, booked.size)
    }

    @Test
    fun cancel() {
        bookSuccessfully()
        val booking = bookingService.booked().first()
        bookingService.cancel(booking.id)
        assertTrue(bookingService.available(earlyMorning.toLocalDate()).contains(booking.timeSlot))
    }

    private fun clockAt(at: LocalDateTime) = Clock.fixed(ZonedDateTime.of(at, ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())

}
