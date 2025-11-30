package com.example.fms.flight.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmbeddedFlight {
    private String flightNo;
    private String from;
    private String to;
    private LocalDate date;
    private double price;
    private int seats;
    private int available;
}
