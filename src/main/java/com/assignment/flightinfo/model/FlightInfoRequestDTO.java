package com.assignment.flightinfo.model;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Data
@Builder
@Getter
public class FlightInfoRequestDTO {

  private LocalDate date;
  private String airportId;
  private LocalTime arrivalTime;
  private LocalTime departureTime;
}
