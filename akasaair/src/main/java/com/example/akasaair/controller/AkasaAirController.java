package com.example.akasaair.controller;

import com.example.akasaair.model.FlightDetails;
import com.example.akasaair.model.FlightReservationRequest;
import com.example.akasaair.model.FlightValues;
import com.example.akasaair.service.FlightService;
import com.example.akasaair.service.JwtTokenProvider;
import com.example.akasaair.utilities.JwtTokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AkasaAirController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private FlightService flightService;

    @GetMapping("/")
    public String defaultMethod() {
        return "Hi";
    }

    @GetMapping("/api/auth")
    public ResponseEntity<JwtTokenResponse> generateToken() {
        String jwt = jwtTokenProvider.createToken("username");
        return ResponseEntity.ok(new JwtTokenResponse(jwt));
    }

    @GetMapping("/validateToken")
    public ResponseEntity<String> validateToken() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZSIsImlhdCI6MTY3NDMwMjEwMCwiZXhwIjoxNjc0MzM4MDkwfQ.hH82yqvUHdGb087lw2hxhWE_xXBkZN2UgHLxiICZIsY";
        if (jwtTokenProvider.validateToken(token)) {
            return ResponseEntity.ok("Valid Token");
        }
        return ResponseEntity.ok("InValid Token");
    }

    @PostMapping("/api/flight/details")
    public ResponseEntity<?> getFlightDetails(@RequestHeader("Authorization") String token, @RequestBody FlightDetails request) {
        if (jwtTokenProvider.validateToken(token)) {
            FlightValues flightDetails = flightService.getFlightDetails(request);
            if (flightDetails == null) {
                return new ResponseEntity<>("No details Found", HttpStatus.OK);
            }
            return new ResponseEntity<>(flightDetails, HttpStatus.OK);
        }
        return ResponseEntity.ok("InValid Token");
    }

    @PostMapping("/api/flight/book")
    public ResponseEntity<?> reserveFlight(@RequestHeader("Authorization") String token, @RequestBody FlightReservationRequest flightReservationRequest) {
        if (jwtTokenProvider.validateToken(token)) {
            return flightService.bookTicket(flightReservationRequest);
        }
        return ResponseEntity.ok("InValid Token");
    }


    @GetMapping("/api/flight/retrieve")
    public ResponseEntity<?> findFlightBookingDetails(@RequestHeader("Authorization") String token, @RequestParam("pnr") String pnr){
        if (jwtTokenProvider.validateToken(token)) {
            FlightReservationRequest flightReservationRequest = flightService.getFlightBookingDetails(pnr);
            if(flightReservationRequest == null){
                return new ResponseEntity<>("Invalid PNR", HttpStatus.OK);
            }
            return new ResponseEntity<>(flightReservationRequest, HttpStatus.OK);
        }
        return ResponseEntity.ok("InValid Token");
    }
}
