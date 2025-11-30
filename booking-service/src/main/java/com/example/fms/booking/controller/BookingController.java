package com.example.fms.booking.controller;

import com.example.fms.booking.dto.BookingRequest;
import com.example.fms.booking.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/flight")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService service;

    @PostMapping("/booking")
    public Object book(@RequestBody BookingRequest req) {
        return service.bookFlight(req);
    }

    @GetMapping("/ticket/{pnr}")
    public Object ticket(@PathVariable String pnr) {
        return service.getTicket(pnr);
    }

    @GetMapping("/booking/history/{email}")
    public Object history(@PathVariable String email) {
        return service.getHistory(email);
    }

    @DeleteMapping("/booking/cancel/{pnr}")
    public Object cancel(@PathVariable String pnr) {
        return service.cancelBooking(pnr);
    }
}
