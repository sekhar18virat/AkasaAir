package com.example.akasaair.model;


import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "FlightSeats")
public class FlightSeats {




    @Column
    @Id
    private String seatNumber;
    @Column
    private int seatCost;

    @Column
    private boolean isBooked;

    @ManyToOne
    @JoinColumn(name = "flight_id_fk")
    private Flight flight;
}
