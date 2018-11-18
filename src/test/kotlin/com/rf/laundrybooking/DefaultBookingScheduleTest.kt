package com.rf.laundrybooking

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class DefaultBookingScheduleTest {
    private val schedule = DefaultBookingSchedule(DefaultRoomRepository(2), 12, listOf(4, 4)) // 12-16, 16-20

    @Test
    fun generatesSlotPeriods() {
        assertEquals(2, schedule.slotPeriods.size)
        assertEquals(12, schedule.slotPeriods[0].start.hour)
        assertEquals(16, schedule.slotPeriods[0].end.hour)
        assertEquals(16, schedule.slotPeriods[1].start.hour)
        assertEquals(20, schedule.slotPeriods[1].end.hour)
    }

    @Test
    fun generatesSlotsInChronologicalOrder() {
        val firstDay = LocalDate.of(2018, 1, 1)
        val secondDay = LocalDate.of(2018, 1, 2)
        val result = schedule.getTimeSlotsIn(
                firstDay.atTime(13, 0), // Still bookable since the time has not ended
                secondDay.atTime(15, 0))

        assertEquals(6, result.size)
        val firstPeriod = schedule.slotPeriods[0]
        val secondPeriod = schedule.slotPeriods[1]
        val firstRoom = Room(0)
        val secondRoom = Room(1)

        var i = 0
        assertEquals(TimeSlot(firstRoom, firstDay, firstPeriod), result[i++])
        assertEquals(TimeSlot(secondRoom, firstDay, firstPeriod), result[i++])
        assertEquals(TimeSlot(firstRoom, firstDay, secondPeriod), result[i++])
        assertEquals(TimeSlot(secondRoom, firstDay, secondPeriod), result[i++])
        assertEquals(TimeSlot(firstRoom, secondDay, firstPeriod), result[i++])
        assertEquals(TimeSlot(secondRoom, secondDay, firstPeriod), result[i++])
    }
}