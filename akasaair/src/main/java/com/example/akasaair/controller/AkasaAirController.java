package com.example.akasaair.controller;

import com.example.akasaair.model.FlightDetails;
import com.example.akasaair.model.FlightReservationRequest;
import com.example.akasaair.model.FlightValues;
import com.example.akasaair.service.FlightService;
import com.example.akasaair.service.JwtTokenProvider;
import com.example.akasaair.utilities.JwtTokenResponse;
import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class AkasaAirController {

    @Autowired
    private final RateLimiter rateLimiter;
    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private FlightService flightService;

    @Autowired
    public AkasaAirController(RateLimiter rateLimiter, JwtTokenProvider jwtTokenProvider, FlightService flightService) {
        this.rateLimiter = rateLimiter;
        this.jwtTokenProvider = jwtTokenProvider;
        this.flightService = flightService;
    }


    @GetMapping("/api/auth")
    public ResponseEntity<?> generateToken() {
        if (rateLimiter.tryAcquire()) {
            String jwt = jwtTokenProvider.createToken("username");
            return ResponseEntity.ok(new JwtTokenResponse(jwt));
        }
        return new ResponseEntity<>("Number of requests exceeded", HttpStatus.OK);
    }


    @PostMapping("/api/flight/details")
    public ResponseEntity<?> getFlightDetails(@RequestHeader("Authorization") String token, @RequestBody FlightDetails request) {
        if (rateLimiter.tryAcquire()) {
            if (jwtTokenProvider.validateToken(token)) {
                FlightValues flightDetails = flightService.getFlightDetails(request);
                if (flightDetails == null) {
                    return new ResponseEntity<>("No details Found", HttpStatus.OK);
                }
                return new ResponseEntity<>(flightDetails, HttpStatus.OK);
            }
            return ResponseEntity.ok("InValid Token");
        } else {
            return new ResponseEntity<>("Number of requests exceeded", HttpStatus.OK);
        }
    }

    @PostMapping("/api/flight/book")
    public ResponseEntity<?> reserveFlight(@RequestHeader("Authorization") String token, @RequestBody FlightReservationRequest flightReservationRequest) {
        if (rateLimiter.tryAcquire()) {
            if (jwtTokenProvider.validateToken(token)) {
                return flightService.bookTicket(flightReservationRequest);
            }
            return ResponseEntity.ok("InValid Token");
        } else {
            return new ResponseEntity<>("Number of requests exceeded", HttpStatus.OK);
        }

    }


    @GetMapping("/api/flight/retrieve")
    public ResponseEntity<?> findFlightBookingDetails(@RequestHeader("Authorization") String token, @RequestParam("pnr") String pnr) {
        if (rateLimiter.tryAcquire()) {
            if (jwtTokenProvider.validateToken(token)) {
                FlightReservationRequest flightReservationRequest = flightService.getFlightBookingDetails(pnr);
                if (flightReservationRequest == null) {
                    return new ResponseEntity<>("Invalid PNR", HttpStatus.OK);
                }
                return new ResponseEntity<>(flightReservationRequest, HttpStatus.OK);
            }
            return ResponseEntity.ok("InValid Token");
        } else {
            return new ResponseEntity<>("Number of requests exceeded", HttpStatus.OK);
        }
    }
}
