package com.assignment.flightinfo.controller;

import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.assignment.flightinfo.service.FlightInfoService;
import com.assignment.flightinfo.utils.TestUtils;
import com.assignment.flightinfo.web.FlightInfoHandler;
import com.fasterxml.jackson.core.JsonProcessingException;

import reactor.core.publisher.Mono;

@ExtendWith(SpringExtension.class)
@WebFluxTest(controllers = FlightInfoHandler.class)
public class FlightInfoHanlerTest {

  @Autowired private WebTestClient webTestClient;

  @MockBean private FlightInfoService service;

  @Test
  public void given_valid_input_should_return_ok() throws JsonProcessingException {

    when(service.getFlightInfo(
            ArgumentMatchers.any(),
            ArgumentMatchers.any(),
            ArgumentMatchers.any(),
            ArgumentMatchers.any()))
        .thenReturn(Mono.just(TestUtils.mockFlightInfo()));

    webTestClient
        .get()
        .uri(
            uri ->
                uri.path("/flight/{localdate}/{airportId}/{arrival}/{diparture}")
                    .build(LocalDate.now(), "airport", LocalTime.now(), LocalTime.now()))
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus()
        .isOk();
  }
}
