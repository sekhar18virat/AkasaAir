package com.example.akasaair.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table
public class FlightReservationRequest {

    @Column
    @Id
    private String pnr;
    @Column
    private FlightDetails flightDetails;
    @Column
    @ElementCollection
    private List<PassengerDetails> passengerDetails;

}
