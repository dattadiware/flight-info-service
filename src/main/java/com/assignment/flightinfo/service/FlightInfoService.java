package com.assignment.flightinfo.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.assignment.flightinfo.exception.ApiError;
import com.assignment.flightinfo.model.FlightInfo;
import com.assignment.flightinfo.model.SourceFive;
import com.assignment.flightinfo.model.SourceFour;
import com.assignment.flightinfo.model.SourceOne;
import com.assignment.flightinfo.model.SourceThree;
import com.assignment.flightinfo.model.SourceTwo;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Collect information from fiver sources and return Mono
 *
 * @author datta
 */
@Service
@AllArgsConstructor
@Slf4j
public class FlightInfoService {

  @Qualifier("webClient")
  private final WebClient webClient;

  /**
   * Returns Flight information for given Date airport id arrival time and departure time
   *
   * @param date
   * @param airportId
   * @param arrival
   * @param departure
   * @return
   */
  @Cacheable
  public Mono<FlightInfo> getFlightInfo(
      LocalDate date, String airportId, LocalTime arrival, LocalTime departure) {

    return Mono.zip(
            getSourceOneFlightInfo(date, airportId, arrival, departure),
            getSourceTwoFlightInfo(date, airportId, arrival, departure),
            getSourceThreeFlightInfo(date, airportId, arrival, departure),
            getSourceFourFlightInfo(date, airportId, arrival, departure),
            getSourceFiveFlightInfo(date, airportId, arrival, departure))
        .map(
            o -> {
              FlightInfo flightInfo = new FlightInfo();

              flightInfo.setFlightNumbers(
                  Arrays.asList(
                      o.getT1().getFlightNumber(),
                      o.getT2().getFlightNumber(),
                      o.getT3().getFlightNumber(),
                      o.getT4().getFlightNumber(),
                      o.getT5().getFlightNumber()));

              return flightInfo;
            })
        .onErrorContinue(null);
  }

  /**
   * Get flight information from source one
   *
   * @param date
   * @param airportId
   * @param arrival
   * @param departure
   * @return
   */
  public Mono<SourceOne> getSourceOneFlightInfo(
      LocalDate date, String airportId, LocalTime arrival, LocalTime departure) {

    return this.webClient
        .get()
        .uri(
            uri ->
                uri.path("/service/one/flight/{localdate}/{airportId}/{arrival}/{diparture}")
                    .build(date, airportId, arrival, departure))
        .retrieve()
        .onStatus(
            HttpStatus::isError,
            response -> {
              ApiError.logTraceResponse(log, response);
              return Mono.error(
                  new IllegalStateException(
                      String.format("Failed! %s %s %s %s", date, airportId, arrival, departure)));
            })
        .bodyToMono(SourceOne.class);
  }

  /**
   * Get flight information from source two
   *
   * @param date
   * @param airportIdO
   * @param arrival
   * @param departure
   * @return
   */
  private Mono<SourceTwo> getSourceTwoFlightInfo(
      LocalDate date, String airportId, LocalTime arrival, LocalTime departure) {

    return this.webClient
        .get()
        .uri(
            uri ->
                uri.path("/service/two/flight/{localdate}/{airportId}/{arrival}/{diparture}")
                    .build(date, airportId, arrival, departure))
        .retrieve()
        .bodyToMono(SourceTwo.class);
  }

  /**
   * Get flight information from source three
   *
   * @param date
   * @param airportId
   * @param arrival
   * @param departure
   * @return
   */
  private Mono<SourceThree> getSourceThreeFlightInfo(
      LocalDate date, String airportId, LocalTime arrival, LocalTime departure) {

    return this.webClient
        .get()
        .uri(
            uri ->
                uri.path("/service/three/flight/{localdate}/{airportId}/{arrival}/{diparture}")
                    .build(date, airportId, arrival, departure))
        .retrieve()
        .bodyToMono(SourceThree.class);
  }

  /**
   * Get flight information from source four
   *
   * @param date
   * @param airportId
   * @param arrival
   * @param departure
   * @return
   */
  private Mono<SourceFour> getSourceFourFlightInfo(
      LocalDate date, String airportId, LocalTime arrival, LocalTime departure) {

    return this.webClient
        .get()
        .uri(
            uri ->
                uri.path("/service/three/flight/{localdate}/{airportId}/{arrival}/{diparture}")
                    .build(date, airportId, arrival, departure))
        .retrieve()
        .bodyToMono(SourceFour.class);
  }

  /**
   * Get flight information from source five
   *
   * @param date
   * @param airportId
   * @param arrival
   * @param departure
   * @return
   */
  private Mono<SourceFive> getSourceFiveFlightInfo(
      LocalDate date, String airportId, LocalTime arrival, LocalTime departure) {

    return this.webClient
        .get()
        .uri(
            uri ->
                uri.path("/service/three/flight/{localdate}/{airportId}/{arrival}/{diparture}")
                    .build(date, airportId, arrival, departure))
        .retrieve()
        .bodyToMono(SourceFive.class);
  }
}
