package com.example.fms.flight.service;

import com.example.fms.flight.dto.AddAirlineRequest;
import com.example.fms.flight.dto.AddInventoryRequest;
import com.example.fms.flight.dto.FlightInfoDto;
import com.example.fms.flight.dto.SearchRequest;
import com.example.fms.flight.model.Airline;

import java.util.List;

public interface FlightService {

    Airline addAirline(AddAirlineRequest request);

    Airline addInventory(AddInventoryRequest request);

    List<FlightInfoDto> searchFlights(SearchRequest request);
}
