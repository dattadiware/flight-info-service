package com.assignment.flightinfo.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FlightInfoRequest {

  private LocalDate date;
  private String airportId;
  private LocalTime arrivalTime;
  private LocalTime departureTime;
}
