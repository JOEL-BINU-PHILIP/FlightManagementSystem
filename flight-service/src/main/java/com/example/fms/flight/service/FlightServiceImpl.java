package com.example.fms.flight.service;

import com.example.fms.flight.dto.*;
import com.example.fms.flight.model.Airline;
import com.example.fms.flight.model.Flight;
import com.example.fms.flight.repository.AirlineRepository;
import com.example.fms.flight.repository.FlightRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final AirlineRepository airlineRepository;
    private final FlightRepository flightRepository;

    @Override
    public void addAirline(AddAirlineRequest req) {
        Airline airline = new Airline();
        airline.setName(req.getName());
        airline.setLogoUrl(req.getLogoUrl());
        airlineRepository.save(airline);
    }

    @Override
    public void addInventory(AddInventoryRequest req) {

        Airline airline = airlineRepository.findById(req.getAirlineId())
                .orElseThrow(() -> new RuntimeException("Airline not found"));

        Flight flight = new Flight();
        flight.setAirlineId(req.getAirlineId());
        flight.setFlightNumber(req.getFlightNumber());
        flight.setFromPlace(req.getFromPlace());
        flight.setToPlace(req.getToPlace());
        flight.setDepartureTime(req.getDepartureTime());
        flight.setArrivalTime(req.getArrivalTime());
        flight.setPrice(req.getPrice());
        flight.setTotalSeats(req.getTotalSeats());
        flight.setAvailableSeats(req.getTotalSeats());

        flightRepository.save(flight);
    }
    @Override
    public FlightDTO getFlightById(String flightId) {
        var flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setFromPlace(flight.getFromPlace());
        dto.setToPlace(flight.getToPlace());
        dto.setDepartureTime(flight.getDepartureTime());
        dto.setArrivalTime(flight.getArrivalTime());
        dto.setPrice(flight.getPrice());
        dto.setAvailableSeats(flight.getAvailableSeats());

        return dto;
    }

    @Override
    public SearchFlightResponse searchFlights(SearchFlightRequest req) {

        LocalDate date = LocalDate.parse(req.getDate());

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end   = date.atTime(LocalTime.MAX);

        List<Flight> flights = flightRepository
                .findByFromPlaceAndToPlaceAndDepartureTimeBetween(
                        req.getFromPlace(), req.getToPlace(), start, end
                );

        List<FlightInfoDto> dtos = flights.stream().map(f -> {
            Airline airline = airlineRepository.findById(f.getAirlineId()).orElse(null);
            return FlightInfoDto.builder()
                    .id(f.getId())
                    .flightNumber(f.getFlightNumber())
                    .fromPlace(f.getFromPlace())
                    .toPlace(f.getToPlace())
                    .departureTime(f.getDepartureTime().toString())
                    .arrivalTime(f.getArrivalTime().toString())
                    .availableSeats(f.getAvailableSeats())
                    .price(f.getPrice())
                    .airlineName(airline != null ? airline.getName() : "Unknown")
                    .build();
        }).toList();

        return SearchFlightResponse.builder().flights(dtos).build();
    }
}
