package com.example.fms.booking.client;

import com.example.fms.booking.dto.BookingRequest;
import org.springframework.stereotype.Component;

@Component
public class FlightClientFallback implements FlightClient {

    @Override
    public Boolean reserveSeats(BookingRequest req) {
        return false; // flight-service down
    }

    @Override
    public FlightDetailsDto getFlightDetails(String flightNo) {
        return null;
    }
}
