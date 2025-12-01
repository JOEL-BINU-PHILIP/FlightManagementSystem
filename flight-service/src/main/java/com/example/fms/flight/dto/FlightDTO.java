package com.example.fms.flight.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FlightDTO {
    private String id;
    private String flightNumber;
    private String fromPlace;
    private String toPlace;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double price;
    private int totalSeats;
    private int availableSeats;
    private String airlineId;
}
