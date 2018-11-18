package com.rf.laundrybooking.schedule

interface RoomRepository {
    fun rooms(): List<Room>
}

class DefaultRoomRepository(size: Int) : RoomRepository {
    private val rooms = (1..size).map { Room(it.toString()) }
    override fun rooms() = rooms
}