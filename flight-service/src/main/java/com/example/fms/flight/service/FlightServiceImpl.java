package com.example.fms.flight.service;

import com.example.fms.flight.dto.AddAirlineRequest;
import com.example.fms.flight.dto.AddInventoryRequest;
import com.example.fms.flight.dto.FlightInfoDto;
import com.example.fms.flight.dto.SearchRequest;
import com.example.fms.flight.model.Airline;
import com.example.fms.flight.repository.AirlineRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor   // Lombok creates constructor for final fields
@Slf4j                      // Lombok logging
public class FlightServiceImpl implements FlightService {

    private final AirlineRepository airlineRepository;

    @Override
    public Airline addAirline(AddAirlineRequest request) {
        Airline airline = new Airline();
        airline.setAirlineName(request.getAirlineName());
        airline.setDescription(request.getDescription());

        airline = airlineRepository.save(airline);

        log.info("Airline added: {}", airline.getId());
        return airline;
    }

    @Override
    public Airline addInventory(AddInventoryRequest request) {
        Airline airline = airlineRepository.findById(request.getAirlineId())
                .orElseThrow(() -> new RuntimeException("Airline not found"));

        EmbeddedFlight flight = new EmbeddedFlight(
                request.getFlightNo(),
                request.getFrom(),
                request.getTo(),
                request.getDate(),
                request.getPrice(),
                request.getSeats(),
                request.getSeats()
        );

        airline.getFlights().add(flight);
        airline = airlineRepository.save(airline);

        log.info("Added inventory to airline {}", airline.getId());
        return airline;
    }

    @Override
    public List<FlightInfoDto> searchFlights(SearchRequest request) {
        List<Airline> airlines = airlineRepository.findAll();
        List<FlightInfoDto> results = new ArrayList<>();

        airlines.forEach(airline -> airline.getFlights().forEach(flight -> {
            if (flight.getFrom().equalsIgnoreCase(request.getFrom()) &&
                    flight.getTo().equalsIgnoreCase(request.getTo()) &&
                    flight.getDate().equals(request.getDate())) {

                FlightInfoDto dto = new FlightInfoDto();
                dto.setAirlineId(airline.getId());
                dto.setAirlineName(airline.getAirlineName());
                dto.setFlightNo(flight.getFlightNo());
                dto.setFrom(flight.getFrom());
                dto.setTo(flight.getTo());
                dto.setDate(flight.getDate());
                dto.setPrice(flight.getPrice());
                dto.setAvailableSeats(flight.getAvailable());

                results.add(dto);
            }
        }));

        log.info("Search results: {} flights found", results.size());
        return results;
    }
}
