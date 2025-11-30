package com.example.fms.booking.dto;

import lombok.Data;

@Data
public class BookingRequest {

    private String flightNo;
    private String airlineId;
    private String email;
    private int seats;
}
