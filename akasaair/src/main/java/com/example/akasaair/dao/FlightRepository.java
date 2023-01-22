package com.example.akasaair.dao;


import com.example.akasaair.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;


@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {

    @Query(value = "Select * from Flight where origin = :origin and destination = :destination and flight_date = :flightDate", nativeQuery = true)
    Flight findByOriginAndDestinationAndFlightDate(String origin, String destination, LocalDate flightDate);
}
