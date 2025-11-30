package com.example.fms.booking.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("bookings")
public class Booking {

    @Id
    private String id;

    private String pnr;
    private String airlineId;
    private String flightNo;
    private String email;
    private int seats;
    private String status; // BOOKED / CANCELLED
}
