package com.example.fms.booking.client;

import com.example.fms.booking.dto.FlightDetailsDto;
import com.example.fms.booking.dto.BookingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "flight-service",
        path = "/api/flight",
        fallback = FlightClientFallback.class
)
public interface FlightClient {

    @PostMapping("/reserve")
    Boolean reserveSeats(@RequestBody BookingRequest req);

    @GetMapping("/details/{flightNo}")
    FlightDetailsDto getFlightDetails(@PathVariable String flightNo);
}
