package com.example.akasaair.dao;

import com.example.akasaair.model.FlightReservationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightReservationRequestRepository extends JpaRepository<FlightReservationRequest, String> {


}
