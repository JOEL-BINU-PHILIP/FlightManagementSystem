package com.example.fms.booking.service;

import com.example.fms.booking.client.FlightClient;
import com.example.fms.booking.dto.*;
import com.example.fms.booking.model.Booking;
import com.example.fms.booking.repository.BookingRepository;
import com.example.fms.booking.util.PnrGenerator;
import com.example.fms.booking.messaging.EmailProducer;
import com.example.fms.common.dto.EmailEvent;

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

        log.info("Processing booking for email: {}", req.getEmail());

        CircuitBreaker breaker = cbFactory.create("flightCB");

        Boolean seatReserved = breaker.run(
                () -> flightClient.reserveSeats(req),
                throwable -> {
                    log.error("Flight reservation failed: {}", throwable.getMessage());
                    return false;
                }
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

        // Send Booking Confirmation Email via RabbitMQ
        EmailEvent event = new EmailEvent(
                req.getEmail(),
                "Booking Confirmed",
                "Your booking is confirmed!\nPNR: " + pnr,
                "BOOKING_CONFIRMED",
                pnr
        );

        emailProducer.sendEmail(event);
        log.info("Email event published for booking confirmation: {}", pnr);

        BookingResponse res = new BookingResponse();
        res.setPnr(pnr);
        res.setEmail(req.getEmail());
        res.setFlightNo(req.getFlightNo());
        res.setStatus("BOOKED");

        return res;
    }

    @Override
    public CancelResponse cancelBooking(String pnr) {

        log.info("Cancelling booking with PNR: {}", pnr);

        Booking booking = repo.findByPnr(pnr);
        if (booking == null) {
            log.error("Invalid PNR: {}", pnr);
            throw new RuntimeException("Invalid PNR. No booking found.");
        }

        booking.setStatus("CANCELLED");
        repo.save(booking);

        // Send Cancellation Email via RabbitMQ
        EmailEvent event = new EmailEvent(
                booking.getEmail(),
                "Booking Cancelled ",
                "Your booking has been cancelled.\nPNR: " + pnr,
                "BOOKING_CANCELLED",
                pnr
        );

        emailProducer.sendEmail(event);
        log.info("Email event published for cancellation: {}", pnr);

        CancelResponse res = new CancelResponse();
        res.setPnr(pnr);
        res.setStatus("CANCELLED");
        return res;
    }

    @Override
    public Object getTicket(String pnr) {
        log.info("Fetching ticket for PNR: {}", pnr);
        return repo.findByPnr(pnr);
    }

    @Override
    public Object getHistory(String email) {
        log.info("Fetching booking history for email: {}", email);
        return repo.findByEmail(email);
    }
}
