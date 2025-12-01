package com.example.fms.flight.service;

import com.example.fms.flight.dto.*;

public interface FlightService {

    void addAirline(AddAirlineRequest request);

    void addInventory(AddInventoryRequest request);

    SearchFlightResponse searchFlights(SearchFlightRequest request);

    FlightDTO getFlightById(String flightId);
}
