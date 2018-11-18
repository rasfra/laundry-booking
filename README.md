# To run on localhost:8080
./gradlew bootRun

# Endpoints
> GET     /laundrybooking/available?yyyy-MM-dd

Available times until and including date  
> GET     /laundrybooking/booked

All future booked times
  
> PUT     /laundrybooking/book

Book a new time. Json payload with username, roomId, date and timeSlotId aquired from the available endpoint. Cannot book a time in the past.  
> DELETE  /laundrybooking/cancel/{id}

Cancel booking with id  

# Things scoped out due to time constraints
- Gradle stuff might not be perfect  
- Test coverage somewhat basic
- Persistence by replacing InMemoryBookingRepository with a persistent one.
- Dedicated model for json serialization for prettier json without cluttering domain classes
- Integration tests using the REST API
- Logging