package com.example.fms.flight.controller;

import com.example.fms.flight.dto.AddAirlineRequest;
import com.example.fms.flight.dto.AddInventoryRequest;
import com.example.fms.flight.dto.FlightInfoDto;
import com.example.fms.flight.dto.SearchRequest;
import com.example.fms.flight.model.Airline;
import com.example.fms.flight.service.FlightService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/airline")
    public Airline addAirline(@RequestBody AddAirlineRequest request) {
        return flightService.addAirline(request);
    }

    @PostMapping("/inventory")
    public Airline addInventory(@RequestBody AddInventoryRequest request) {
        return flightService.addInventory(request);
    }

    @PostMapping("/search")
    public List<FlightInfoDto> searchFlights(@RequestBody SearchRequest request) {
        return flightService.searchFlights(request);
    }
}
