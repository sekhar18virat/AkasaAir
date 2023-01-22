package com.example.akasaair.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.Embeddable;


@Data
@Embeddable
public class FlightDetails {
    private String origin;
    private String destination;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private String flightDate;

}