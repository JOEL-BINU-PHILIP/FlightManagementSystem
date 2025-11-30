package com.example.fms.booking.dto;

import lombok.Data;

@Data
public class FlightDetailsDto {
    private String flightNo;
    private String from;
    private String to;
    private String date;
    private double price;
    private int availableSeats;
}
