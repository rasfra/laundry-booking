# To run on localhost:8080
./gradlew bootRun

# Endpoints
GET     /laundrybooking/available?$date - Available times until and including $date
GET     /laundrybooking/booked - All future booked times
PUT     /laundrybooking/book - Book a new time. Json payload with username, roomId, date and timeSlotId aquired from the available endpoint
DELETE  /laundrybooking/cancel/$id - Cancel booking with id