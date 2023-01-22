package com.example.akasaair.model;

import lombok.Data;

import javax.persistence.Embeddable;

@Data
@Embeddable
public class PassengerDetails {
    private String firstName;
    private String lastName;
    private String seat;
}
