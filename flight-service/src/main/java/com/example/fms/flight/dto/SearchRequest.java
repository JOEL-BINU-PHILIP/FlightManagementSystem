package com.example.fms.flight.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class SearchRequest {
    private String from;
    private String to;
    private LocalDate date;
}
