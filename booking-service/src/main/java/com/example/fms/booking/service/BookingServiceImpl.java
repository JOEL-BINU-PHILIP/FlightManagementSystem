package com.example.fms.booking.service;

import com.example.fms.booking.client.FlightClient;
import com.example.fms.booking.dto.*;
import com.example.fms.booking.model.Booking;
import com.example.fms.booking.repository.BookingRepository;
import com.example.fms.booking.util.PnrGenerator;
import com.example.fms.booking.messaging.EmailProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingServiceImpl implements BookingService {

    private final FlightClient flightClient;
    private final BookingRepository repo;
    private final EmailProducer emailProducer;
    private final CircuitBreakerFactory cbFactory;

    @Override
    public BookingResponse bookFlight(BookingRequest req) {

        CircuitBreaker breaker = cbFactory.create("flightCB");

        Boolean seatReserved = breaker.run(
                () -> flightClient.reserveSeats(req),
                throwable -> false
        );

        if (!seatReserved) {
            throw new RuntimeException("Flight service unavailable / seat reservation failed");
        }

        String pnr = PnrGenerator.generate();

        Booking booking = new Booking();
        booking.setPnr(pnr);
        booking.setFlightNo(req.getFlightNo());
        booking.setAirlineId(req.getAirlineId());
        booking.setEmail(req.getEmail());
        booking.setSeats(req.getSeats());
        booking.setStatus("BOOKED");

        repo.save(booking);

        emailProducer.sendEmailMessage(req.getEmail(), "Booking confirmed. PNR: " + pnr);

        BookingResponse res = new BookingResponse();
        res.setPnr(pnr);
        res.setEmail(req.getEmail());
        res.setFlightNo(req.getFlightNo());
        res.setStatus("BOOKED");

        return res;
    }

    @Override
    public CancelResponse cancelBooking(String pnr) {

        Booking booking = repo.findByPnr(pnr);
        if (booking == null) throw new RuntimeException("Invalid PNR");

        booking.setStatus("CANCELLED");
        repo.save(booking);

        emailProducer.sendEmailMessage(booking.getEmail(), "Booking cancelled: " + pnr);

        CancelResponse res = new CancelResponse();
        res.setPnr(pnr);
        res.setStatus("CANCELLED");
        return res;
    }

    @Override
    public Object getTicket(String pnr) {
        return repo.findByPnr(pnr);
    }

    @Override
    public Object getHistory(String email) {
        return repo.findByEmail(email);
    }
}
