package com.rf.laundrybooking.booking

import com.rf.laundrybooking.schedule.DefaultBookingSchedule
import com.rf.laundrybooking.schedule.DefaultRoomRepository
import com.rf.laundrybooking.schedule.Room
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

class BookingServiceTest {
    private val schedule = DefaultBookingSchedule(DefaultRoomRepository(2), 7, listOf(5, 4, 3, 3))
    private val repository = InMemoryBookingRepository()
    private val openingHour = LocalDateTime.of(2018, 1, 1, 7, 0)
    private val room1 = Room("1")
    private val room2 = Room("2")
    private val periods = 0..3
    private val user = User("user")

    @Test
    fun listAvailable() {
        val available = bookingServiceAt(openingHour).available(openingHour.toLocalDate())
        assertEquals(4 * 2, available.size)
        assertEquals(Room("1"), available.first().room)
        assertEquals(7, available.first().timePeriod.start.hour)
        assertEquals(12, available.first().timePeriod.end.hour)
    }

    @Test
    fun listAvailableWithPassedPeriod() {
        val duringFirstPeriod = LocalDateTime.of(2018, 1, 1, 15, 0)
        val bookingService = bookingServiceAt(duringFirstPeriod)

        val available = bookingService.available(LocalDate.of(2018, 1, 1))
        assertEquals(3 * 2, available.size)
        assertEquals(Room("1"), available.first().room)
        assertEquals(12, available.first().timePeriod.start.hour)
        assertEquals(16, available.first().timePeriod.end.hour)
    }

    @Test
    fun bookSuccessfully() {
        val bookingService = bookingServiceAt(openingHour)
        val available = bookingService.available(openingHour.toLocalDate())
        assert(available.isNotEmpty())

        val slotToBook = available.first()
        bookingService.book(user, slotToBook.room, slotToBook.day, slotToBook.timePeriod.id)

        val availableAfterBooking = bookingService.available(openingHour.toLocalDate())
        assertEquals(ArrayList(available).apply { remove(slotToBook) }, availableAfterBooking)

        val booked = bookingService.booked()
        assertEquals(Booking("0", user, slotToBook), booked.first())
    }

    @Test(expected = IllegalArgumentException::class)
    fun doubleBookingFails() {
        val bookingService = bookingServiceAt(openingHour)
        val available = bookingService.available(openingHour.toLocalDate())
        assert(available.isNotEmpty())
        val slotToBook = available.first()
        bookingService.book(user, slotToBook.room, slotToBook.day, slotToBook.timePeriod.id)
        bookingService.book(user, slotToBook.room, slotToBook.day, slotToBook.timePeriod.id)
    }

    @Test(expected = IllegalArgumentException::class)
    fun pastBookingFails() {
        val bookingService = bookingServiceAt(openingHour)
        val available = bookingService.available(openingHour.toLocalDate())
        assert(available.isNotEmpty())
        val slotToBook = available.first()
        bookingService.book(user, slotToBook.room, slotToBook.day.minusDays(1), slotToBook.timePeriod.id)
    }

    @Test
    fun canBookDifferentRoomsAtSamePeriod() {
        val bookingService = bookingServiceAt(openingHour)
        val available = bookingService.available(openingHour.toLocalDate())
        assert(available.isNotEmpty())
        val slotToBook = available.first()
        bookingService.book(user, Room("1"), slotToBook.day, slotToBook.timePeriod.id)
        bookingService.book(user, Room("2"), slotToBook.day, slotToBook.timePeriod.id)

        assertEquals(2, bookingService.booked().size)
    }

    @Test
    fun onlyListFutureBookings() {
        book(bookingServiceAt(openingHour), room1, periods.first)

        // Go forward in time a bit
        val booked = bookingServiceAt(openingHour.plusHours(6)).booked()
        assertTrue(booked.isEmpty())
    }

    @Test
    fun cancel() {
        val bookingService = bookingServiceAt(openingHour)
        val booking = book(bookingService, room1, periods.first)
        bookingService.cancel(booking.id)
        assertTrue(bookingService.available(openingHour.toLocalDate()).contains(booking.timeSlot))
    }

    private fun book(bookingService: BookingService, room: Room, period: Int): Booking {
        val available = bookingService.available(openingHour.toLocalDate())
        assert(available.isNotEmpty())

        val slotToBook = available.first()
        bookingService.book(user, room, slotToBook.day, period)

        val availableAfterBooking = bookingService.available(openingHour.toLocalDate())
        assertEquals(ArrayList(available).apply { remove(slotToBook) }, availableAfterBooking)

        val booked = bookingService.booked()
        assertEquals(Booking("0", user, slotToBook), booked.first())
        return booked.first()
    }

    private fun bookingServiceAt(time: LocalDateTime) =
            BookingService(repository, schedule,
                    Clock.fixed(time.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault()))

}
