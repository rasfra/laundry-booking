package com.rf.laundrybooking

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import java.time.Clock

@Configuration
@EnableWebMvc
class ApplicationConfig {

    //@Bean
    //fun bookingController(bookingService: BookingService) = BookingController(bookingService)

    @Bean
    fun roomRepository() = DefaultRoomRepository(2)

    @Bean
    fun bookingRepository() = InMemoryBookingRepository()

    @Bean
    fun bookingSchedule(roomRepository: RoomRepository): BookingSchedule =
            DefaultBookingSchedule(roomRepository, 7, listOf(5, 4, 3, 3))

    @Bean
    fun bookingService(repository: BookingRepository, schedule: BookingSchedule) = BookingService(repository, schedule, Clock.systemDefaultZone())
}