package com.example.fms.flight.controller;

import com.example.fms.flight.dto.*;
import com.example.fms.flight.service.FlightService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flight")
public class FlightController {

    private final FlightService flightService;

    // ADMIN → Add Airline
    @PostMapping("/addAirline")
    public ResponseEntity<String> addAirline(@RequestBody AddAirlineRequest req) {
        flightService.addAirline(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Airline added successfully");
    }


    // ADMIN → Add Flight Inventory
    @PostMapping("/airline/inventory/add")
    public ResponseEntity<String> addInventory(@RequestBody AddInventoryRequest req) {
        flightService.addInventory(req);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Flight inventory added successfully");
    }


    // USER → Search Flights
    @PostMapping("/search")
    public ResponseEntity<SearchFlightResponse> searchFlights(@RequestBody SearchFlightRequest req) {
        return ResponseEntity.ok(flightService.searchFlights(req));
    }


    // USER → Get Flight Details
    @GetMapping("/{flightId}")
    public ResponseEntity<FlightDTO> getFlightById(@PathVariable String flightId) {
        return ResponseEntity.ok(flightService.getFlightById(flightId));
    }
}
