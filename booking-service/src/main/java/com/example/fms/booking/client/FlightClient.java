package com.example.fms.booking.client;

import com.example.fms.booking.dto.BookingRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "flight-service",
        path = "/api/flight",
        fallback = FlightClientFallback.class
)
public interface FlightClient {

    @PostMapping("/reserve")
    Boolean reserveSeats(@RequestBody BookingRequest req);

    @GetMapping("/details/{flightNo}")
    FlightDetailsDto getFlightDetails(@PathVariable("flightNo") String flightNo);
}
