package com.example.fms.flight.dto;

import lombok.Data;

@Data
public class AddAirlineRequest {
    private String airlineName;
    private String description;
}
