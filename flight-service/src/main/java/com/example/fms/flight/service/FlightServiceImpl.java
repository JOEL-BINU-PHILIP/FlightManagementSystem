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
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FlightServiceImpl implements FlightService {

    private final AirlineRepository airlineRepository;
    private final FlightRepository flightRepository;

    // Shared formatter for converting String → LocalDateTime
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Override
    public void addAirline(AddAirlineRequest req) {
        Airline airline = new Airline();
        airline.setName(req.getName());
        airline.setLogoUrl(req.getLogoUrl());
        airlineRepository.save(airline);
    }

    @Override
    public void addInventory(AddInventoryRequest req) {

        airlineRepository.findById(req.getAirlineId())
                .orElseThrow(() -> new RuntimeException("Airline not found"));

        Flight flight = new Flight();
        flight.setAirlineId(req.getAirlineId());
        flight.setFlightNumber(req.getFlightNumber());
        flight.setFromPlace(req.getFromPlace());
        flight.setToPlace(req.getToPlace());

        // IMPORTANT — convert String → LocalDateTime
        flight.setDepartureTime(LocalDateTime.parse(req.getDepartureTime(), DATE_TIME_FORMATTER));
        flight.setArrivalTime(LocalDateTime.parse(req.getArrivalTime(), DATE_TIME_FORMATTER));

        flight.setPrice(req.getPrice());
        flight.setTotalSeats(req.getTotalSeats());
        flight.setAvailableSeats(req.getTotalSeats()); // all seats available

        flightRepository.save(flight);
    }

    @Override
    public FlightDTO getFlightDetails(String flightId) {

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        FlightDTO dto = new FlightDTO();
        dto.setId(flight.getId());
        dto.setFlightNumber(flight.getFlightNumber());
        dto.setFromPlace(flight.getFromPlace());
        dto.setToPlace(flight.getToPlace());
        dto.setDepartureTime(flight.getDepartureTime().toString());
        dto.setArrivalTime(flight.getArrivalTime().toString());
        dto.setPrice(flight.getPrice());
        dto.setAvailableSeats(flight.getAvailableSeats());

        return dto;
    }

    @Override
    public SearchFlightResponse searchFlights(SearchFlightRequest req) {

        LocalDate date = LocalDate.parse(req.getDate());
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Flight> flights = flightRepository.findByFromPlaceAndToPlaceAndDepartureTimeBetween(
                req.getFromPlace(),
                req.getToPlace(),
                start,
                end
        );

        List<FlightInfoDTO> flightInfoList = flights.stream()
                .map(flight -> {
                    Airline airline = airlineRepository
                            .findById(flight.getAirlineId()).orElse(null);

                    return FlightInfoDTO.builder()
                            .id(flight.getId())
                            .flightNumber(flight.getFlightNumber())
                            .airlineName(airline != null ? airline.getName() : "Unknown")
                            .fromPlace(flight.getFromPlace())
                            .toPlace(flight.getToPlace())
                            .departureTime(flight.getDepartureTime().toString())
                            .arrivalTime(flight.getArrivalTime().toString())
                            .price(flight.getPrice())
                            .availableSeats(flight.getAvailableSeats())
                            .build();
                })
                .toList();

        return SearchFlightResponse.builder()
                .flights(flightInfoList)
                .build();
    }
    @Override
    public boolean reserveSeats(String flightId, int seats) {

        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found"));

        if (flight.getAvailableSeats() < seats) {
            return false;
        }

        flight.setAvailableSeats(flight.getAvailableSeats() - seats);
        flightRepository.save(flight);

        return true;
    }

}

