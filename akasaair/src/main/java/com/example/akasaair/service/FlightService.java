package com.example.akasaair.service;

import com.example.akasaair.dao.FlightRepository;
import com.example.akasaair.dao.FlightReservationRequestRepository;
import com.example.akasaair.dao.FlightSeatsRepo;
import com.example.akasaair.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightReservationRequestRepository flightReservationRequestRepository;

    @Autowired
    private FlightSeatsRepo flightSeatsRepo;

    public FlightValues getFlightDetails(FlightDetails request) {
        // fetch flight details from the database using the origin, destination and flightDate from the request
        String dateString = request.getFlightDate();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate flightDate = LocalDate.parse(dateString, formatter);
        Flight flight = flightRepository.findByOriginAndDestinationAndFlightDate(request.getOrigin(),
                request.getDestination(),
                flightDate);
        if (flight == null) {
            return null;
        } else {
            FlightValues flightDetails = new FlightValues();
            flightDetails.setOrigin(flight.getOrigin());
            flightDetails.setDestination(flight.getDestination());
            flightDetails.setFlightDate(flight.getFlightDate());
            flightDetails.setBasicFare(flight.getBasicFare());
            flightDetails.setSeats(flight.getSeats().stream()
                    .filter(seat -> !seat.isBooked())
                    .map(seat -> seat.getSeatNumber())
                    .collect(Collectors.toList()));
            flightDetails.setSeatPrice(flight.getSeats().stream()
                    .filter(seat -> !seat.isBooked())
                    .map(seat -> seat.getSeatCost())
                    .collect(Collectors.toList()));

            return flightDetails;
        }
    }

    public ResponseEntity<?>  bookTicket(FlightReservationRequest flightReservationRequest){
        FlightDetails flightDetails = flightReservationRequest.getFlightDetails();
        Map<String, String> response = new HashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        Flight flight = flightRepository.findByOriginAndDestinationAndFlightDate(flightDetails.getOrigin(), flightDetails.getDestination(),  LocalDate.parse(flightDetails.getFlightDate(), formatter));
        if(flight == null){
            return new ResponseEntity<>("No Details found", HttpStatus.OK);
        }else{
            AtomicInteger totalCost = new AtomicInteger();
            List<String> seatNumbers = flightReservationRequest.getPassengerDetails().stream()
                    .map(passenger -> passenger.getSeat())
                    .collect(Collectors.toList());

            List<FlightSeats> flightSeats = flight.getSeats();

            seatNumbers.stream()
                    .forEach(seatNumber -> flightSeats.stream()
                            .filter(flightSeat -> flightSeat.getSeatNumber().equals(seatNumber))
                            .forEach(flightSeat -> totalCost.updateAndGet(v -> v + 5500 + flightSeat.getSeatCost())));

            String pnrNumber = UUID.randomUUID().toString().substring(0, 6);
            flightReservationRequest.setPnr(pnrNumber);
            flightReservationRequestRepository.save(flightReservationRequest);
            markSeatsAsBooked(seatNumbers);
            response.put("pnr", pnrNumber);
            response.put("totalCost", String.valueOf(totalCost));
            return new ResponseEntity<>(response, HttpStatus.OK);
        }

    }

    @Transactional
    private void markSeatsAsBooked(List<String> seatNumbers) {
        List<FlightSeats> seats = flightSeatsRepo.findBySeatNumberIn(seatNumbers);
        seats.forEach(seat -> seat.setBooked(true));
        flightSeatsRepo.saveAll(seats);
    }

    public FlightReservationRequest getFlightBookingDetails(String pnr) {
        return flightReservationRequestRepository.findById(pnr).get();
    }
}
