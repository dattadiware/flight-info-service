package com.assignment.flightinfo.utils;

import java.util.Arrays;

import com.assignment.flightinfo.model.FlightInfo;

public class TestUtils {

  public static FlightInfo mockFlightInfo() {

    return FlightInfo.builder()
        .flightNumbers(Arrays.asList("mockflightnumberone", "mockflightnumbertwo"))
        .build();
  }
}
