package com.example.fms.flight.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Document(collection = "airlines")
public class Airline {

    @Id
    private String id;

    private String airlineName;
    private String description;

    private List<EmbeddedFlight> flights = new ArrayList<>();

    public void addFlight(EmbeddedFlight flight) {
        flights.add(flight);
    }
}
