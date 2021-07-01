package com.assignment.flightinfo.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
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
import com.assignment.flightinfo.model.ServiceUrls;
import com.fasterxml.jackson.core.JsonProcessingException;

import io.netty.handler.codec.http.HttpMethod;
import reactor.test.StepVerifier;

@ExtendWith(MockServerExtension.class)
public class FlightInfoServiceTest {

  private ClientAndServer mockServer;

  private FlightInfoService service;

  private Map<String, String> urlsMap;

  @BeforeEach
  public void setupMockServer() throws UnsupportedEncodingException {

    urlsMap = new HashMap<>();
    urlsMap.put("serviceOne", URLEncoder.encode("/service/one/2021-11-01/DUB/10:20:20/11:20:30", "UTF-8"));
    urlsMap.put("serviceTwo", URLEncoder.encode("/service/two/2021-11-01/DUB/10:20:20/11:20:30","UTF-8"));
    urlsMap.put("serviceThree", URLEncoder.encode("/service/three/2021-11-01/DUB/10:20:20/11:20:30","UTF-8"));
    urlsMap.put("serviceFour", URLEncoder.encode("/service/four/2021-11-01/DUB/10:20:20/11:20:30","UTF-8"));
    urlsMap.put("serviceFive", URLEncoder.encode("/service/five/2021-11-01/DUB/10:20:20/11:20:30","UTF-8"));

    ServiceUrls urls = new ServiceUrls(urlsMap);

    mockServer = ClientAndServer.startClientAndServer(2001);
    service =
        new FlightInfoService(
            WebClient.builder().baseUrl("http://localhost:" + mockServer.getPort()).build(), urls);
  }

  @Test
  public void testFlightInfoService() throws JsonProcessingException {

    HttpRequest expectedFirstRequest =
        HttpRequest.request().withMethod(HttpMethod.GET.name()).withPath(urlsMap.get("serviceOne"));

    HttpRequest expectedTwoRequest =
        HttpRequest.request().withMethod(HttpMethod.GET.name()).withPath(urlsMap.get("serviceTwo"));

    HttpRequest expectedThreeRequest =
        HttpRequest.request()
            .withMethod(HttpMethod.GET.name())
            .withPath(urlsMap.get("serviceThree"));

    HttpRequest expectedFourRequest =
        HttpRequest.request()
            .withMethod(HttpMethod.GET.name())
            .withPath(urlsMap.get("serviceFour"));

    HttpRequest expectedFiveRequest =
        HttpRequest.request()
            .withMethod(HttpMethod.GET.name())
            .withPath(urlsMap.get("serviceFive"));

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
                .withBody("{\"flightNumber\": \"f2\"}")
                .withContentType(MediaType.APPLICATION_JSON)
                .withDelay(TimeUnit.MILLISECONDS, 600));

    this.mockServer
        .when(expectedThreeRequest)
        .respond(
            HttpResponse.response()
                .withBody("{\"flightNumber\": \"f3\"}")
                .withContentType(MediaType.APPLICATION_JSON)
                .withDelay(TimeUnit.MILLISECONDS, 500));

    this.mockServer
        .when(expectedFourRequest)
        .respond(
            HttpResponse.response()
                .withBody("{\"flightNumber\": \"f4\"}")
                .withContentType(MediaType.APPLICATION_JSON)
                .withDelay(TimeUnit.MILLISECONDS, 500));

    this.mockServer
        .when(expectedFiveRequest)
        .respond(
            HttpResponse.response()
                .withBody("{\"flightNumber\": \"f5\"}")
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
        .expectNextMatches(mergedCallsDTO -> 5 == mergedCallsDTO.getFlightNumbers().size())
        .verifyComplete();

    this.mockServer.verify(expectedFirstRequest, VerificationTimes.once());
    this.mockServer.verify(expectedTwoRequest, VerificationTimes.once());
  }

  @AfterEach
  public void tearDownServer() {
    mockServer.stop();
  }
}
