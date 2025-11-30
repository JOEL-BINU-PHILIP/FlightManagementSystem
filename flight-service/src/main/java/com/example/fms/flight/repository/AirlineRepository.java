package com.example.fms.flight.repository;

import com.example.fms.flight.model.Airline;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AirlineRepository extends MongoRepository<Airline, String> {

    // find all airlines that contain a flight matching criteria
    @Query("{ 'flights': { $elemMatch: { 'from': ?0, 'to': ?1, 'date': { $eq: ?2 } } } }")
    List<Airline> findByFlightFromToDate(String from, String to, LocalDate date);

}
