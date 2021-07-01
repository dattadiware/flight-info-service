package com.assignment.flightinfo.service;

import static com.assignment.flightinfo.model.ServiceUrls.URLS;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.assignment.flightinfo.exception.ApiError;
import com.assignment.flightinfo.model.FlightInfo;
import com.assignment.flightinfo.model.FlightInfoRequest;
import com.assignment.flightinfo.model.GetFlightInfoFromDownStream;
import com.assignment.flightinfo.model.ServiceUrls;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;;

/**
 * Collect information from fiver sources and return Mono
 *
 * @author datta
 */
@Service
@Slf4j
@AllArgsConstructor
@ConfigurationProperties(prefix = "downstremservices")
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
  public Mono<FlightInfo> getFlightInfo(FlightInfoRequest requestData) {

    return Mono.zip(
            getFlightInfoFromDownStream(URLS.get("serviceOne"), requestData),
            getFlightInfoFromDownStream(URLS.get("serviceTwo"), requestData),
            getFlightInfoFromDownStream(URLS.get("serviceThree"), requestData),
            getFlightInfoFromDownStream(URLS.get("serviceFour"), requestData),
            getFlightInfoFromDownStream(URLS.get("serviceFive"), requestData))
        .map(
            o -> {
              return FlightInfo.builder()
                  .flightNumbers(
                      Arrays.asList(
                          o.getT1().getFlightNumber(),
                          o.getT2().getFlightNumber(),
                          o.getT3().getFlightNumber(),
                          o.getT4().getFlightNumber(),
                          o.getT5().getFlightNumber()))
                  .build();
            })
        .log()
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
  public Mono<GetFlightInfoFromDownStream> getFlightInfoFromDownStream(
      String downStreamServiceUri, FlightInfoRequest requestData) {

    return this.webClient
        .get()
        .uri(
            uri ->
                uri.path(downStreamServiceUri)
                    .build(
                        requestData.getDate(),
                        requestData.getAirportId(),
                        requestData.getArrivalTime(),
                        requestData.getDepartureTime()))
        .retrieve()
        .onStatus(
            HttpStatus::isError,
            response -> {
              ApiError.logTraceResponse(log, response);
              return Mono.error(
                  new IllegalStateException(String.format("Failed! %s", requestData)));
            })
        .bodyToMono(GetFlightInfoFromDownStream.class);
  }
}
