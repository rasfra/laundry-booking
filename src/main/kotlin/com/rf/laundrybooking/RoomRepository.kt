package com.rf.laundrybooking

interface RoomRepository {
    fun rooms(): List<Room>
}

class DefaultRoomRepository(size: Int) : RoomRepository {
    private val rooms = (0 until size).map { Room(it) }
    override fun rooms() = rooms
}