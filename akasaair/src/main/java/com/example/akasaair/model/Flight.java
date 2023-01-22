package com.example.akasaair.model;

import lombok.Data;
import org.hibernate.annotations.Where;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "Flight")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String origin;
    @Column
    private String destination;
    @Column
    private LocalDate flightDate;
    @OneToMany(mappedBy = "flight")
    @Where(clause = "is_booked = false")
    private List<FlightSeats> seats;
    @Column
    private int  basicFare;

    public Flight(){}

    public Flight(Long id, String origin, String destination, LocalDate flightDate, List<FlightSeats> seats) {
        this.id = id;
        this.origin = origin;
        this.destination = destination;
        this.flightDate = flightDate;
        this.seats = seats;
        this.basicFare = 5500;
    }
}