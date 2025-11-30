package com.example.fms.flight.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class FlightInfoDto {
    private String airlineId;
    private String airlineName;
    private String flightNo;
    private String from;
    private String to;
    private LocalDate date;
    private double price;
    private int availableSeats;
}
