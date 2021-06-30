package com.assignment.flightinfoservice.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.assignment.flightinfoservice.model.FlightInfo;
import com.assignment.flightinfoservice.model.SourceOne;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class FlightInfoService {

  @Qualifier("webClient")
  private final WebClient webClient;

  public Mono<FlightInfo> getFlightInfo(
      LocalDate date, String airportId, LocalTime arrival, LocalTime departure) {

    return Mono.zip(
            getSourceOneFlightInfo(date, airportId, arrival, departure),
            getSourceTwoFlightInfo(date, airportId, arrival, departure),
            getSourceThreeFlightInfo(date, airportId, arrival, departure))
        .map(
            o -> {
              FlightInfo flightInfo = new FlightInfo();

              flightInfo.setFlightNumbers(
                  Arrays.asList(
                      o.getT1().getFlightNumber(),
                      o.getT2().getFlightNumber(),
                      o.getT3().getFlightNumber()));

              System.out.println(flightInfo);

              return flightInfo;
            });
  }

  private Mono<SourceOne> getSourceOneFlightInfo(
      LocalDate date, String airportId, LocalTime arrival, LocalTime departure) {

	 
    return this.webClient
        .get()
        .uri(
            uri ->
                uri.path("/service/one/flight/{localdate}/{airportId}/{arrival}/{diparture}")
                    .build(date, airportId, arrival, departure))
        .retrieve()
        .bodyToMono(SourceOne.class);
  }

  private Mono<SourceOne> getSourceTwoFlightInfo(
      LocalDate date, String airportId, LocalTime arrival, LocalTime departure) {

    return this.webClient
        .get()
        .uri(
            uri ->
                uri.path("/service/two/flight/{localdate}/{airportId}/{arrival}/{diparture}")
                    .build(date, airportId, arrival, departure))
        .retrieve()
        .bodyToMono(SourceOne.class);
  }

  private Mono<SourceOne> getSourceThreeFlightInfo(
      LocalDate date, String airportId, LocalTime arrival, LocalTime departure) {

    return this.webClient
        .get()
        .uri(
            uri ->
                uri.path("/service/three/flight/{localdate}/{airportId}/{arrival}/{diparture}")
                    .build(date, airportId, arrival, departure))
        .retrieve()
        .bodyToMono(SourceOne.class);
  }
}
