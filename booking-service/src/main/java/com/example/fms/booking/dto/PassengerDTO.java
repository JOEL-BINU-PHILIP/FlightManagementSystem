package com.example.fms.booking.dto;

import lombok.Data;

@Data
public class PassengerDTO {
    private String name;
    private String gender;
    private int age;
    private String seatNumber;
    private String meal;
}
