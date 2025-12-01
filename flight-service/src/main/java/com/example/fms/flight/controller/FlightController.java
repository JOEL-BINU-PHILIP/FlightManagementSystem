package com.example.fms.flight.controller;

import com.example.fms.flight.dto.*;
import com.example.fms.flight.service.FlightService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/internal/flights")   // ONLY API GATEWAY CALLS THIS
@RequiredArgsConstructor
public class FlightController {

    private final FlightService service;

    @PostMapping("/airlines")
    public String addAirline(@RequestBody AddAirlineRequest req) {
        service.addAirline(req);
        return "Airline added successfully";
    }

    @PostMapping("/inventory")
    public String addInventory(@RequestBody AddInventoryRequest req) {
        service.addInventory(req);
        return "Flight Inventory added successfully";
    }

    @PostMapping("/search")
    public SearchFlightResponse search(@RequestBody SearchFlightRequest req) {
        return service.searchFlights(req);
    }

    @GetMapping("/{flightId}")
    public FlightDTO getFlight(@PathVariable String flightId) {
        return service.getFlightById(flightId);
    }
}
