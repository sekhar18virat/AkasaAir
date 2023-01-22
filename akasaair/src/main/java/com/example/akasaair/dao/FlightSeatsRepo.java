package com.example.akasaair.dao;

import com.example.akasaair.model.FlightSeats;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightSeatsRepo extends JpaRepository<FlightSeats, String> {

    List<FlightSeats> findBySeatNumberIn(List<String> seatNumbers);
}
