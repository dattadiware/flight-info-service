package com.assignment.flightinfo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.verify.VerificationTimes;
import org.springframework.web.reactive.function.client.WebClient;

import com.assignment.flightinfo.model.FlightInfoRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.netty.handler.codec.http.HttpMethod;
import reactor.test.StepVerifier;

@ExtendWith(MockServerExtension.class)
public class FlightInfoServiceTest {

  private ClientAndServer mockServer;

  private FlightInfoService service;

  @BeforeEach
  public void setupMockServer() {
    mockServer = ClientAndServer.startClientAndServer(2001);
    service =
        new FlightInfoService(
            WebClient.builder().baseUrl("http://localhost:" + mockServer.getPort()).build());
  }

  @Test
  public void testFlightInfoService() throws JsonProcessingException {

    HttpRequest expectedFirstRequest =
        HttpRequest.request()
            .withMethod(HttpMethod.GET.name())
            .withPath("/service/one/flight/2021-11-01/DUB/10%3A20%3A30/11%3A20%3A30");

    HttpRequest expectedTwoRequest =
        HttpRequest.request()
            .withMethod(HttpMethod.GET.name())
            .withPath("/service/two/flight/2021-11-01/DUB/10%3A20%3A30/11%3A20%3A30");

    HttpRequest expectedThreeRequest =
        HttpRequest.request()
            .withMethod(HttpMethod.GET.name())
            .withPath("/service/three/flight/2021-11-01/DUB/10%3A20%3A30/11%3A20%3A30");

    this.mockServer
        .when(expectedFirstRequest)
        .respond(
            HttpResponse.response()
                .withBody("{\"flightNumber\": \"f1\"}")
                .withContentType(MediaType.APPLICATION_JSON)
                .withDelay(TimeUnit.MILLISECONDS, 800));

    this.mockServer
        .when(expectedTwoRequest)
        .respond(
            HttpResponse.response()
                .withBody("{\"flightNumber\": \"f1\"}")
                .withContentType(MediaType.APPLICATION_JSON)
                .withDelay(TimeUnit.MILLISECONDS, 600));

    this.mockServer
        .when(expectedThreeRequest)
        .respond(
            HttpResponse.response()
                .withBody("{\"flightNumber\": \"f3\"}")
                .withContentType(MediaType.APPLICATION_JSON)
                .withDelay(TimeUnit.MILLISECONDS, 500));

    FlightInfoRequest requestData =
        FlightInfoRequest.builder()
            .date(LocalDate.of(2021, 11, 01))
            .airportId("DUB")
            .arrivalTime(LocalTime.of(10, 20, 30))
            .departureTime(LocalTime.of(11, 20, 30))
            .build();

    StepVerifier.create(this.service.getFlightInfo(requestData))
        .expectNextMatches(mergedCallsDTO -> 3 == mergedCallsDTO.getFlightNumbers().size())
        .verifyComplete();

    this.mockServer.verify(expectedFirstRequest, VerificationTimes.once());
    this.mockServer.verify(expectedTwoRequest, VerificationTimes.once());
  }

  @AfterEach
  public void tearDownServer() {
    mockServer.stop();
  }
}
