package com.example.fms.booking.dto;

import lombok.Data;

@Data
public class BookingResponse {
    private String pnr;
    private String flightNo;
    private String email;
    private String status;
}
