package com.example.fms.booking.dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {

    private String flightId;
    private String email;
    private int seatsBooked;
    private List<PassengerDTO> passengers;
}
