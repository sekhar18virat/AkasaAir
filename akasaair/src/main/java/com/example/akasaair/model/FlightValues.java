package com.example.akasaair.model;

import lombok.Data;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Data

public class FlightValues {

    private String origin;

    private String destination;

    private LocalDate flightDate;

    private List<String> seats;


    private List<Integer> seatPrice;


    private int  basicFare;

}
