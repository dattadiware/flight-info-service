package com.assignment.flightinfo.service;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.assignment.flightinfo.model.FlightInfo;
import com.assignment.flightinfo.model.FlightInfoRequest;
import com.assignment.flightinfo.model.GetFlightInfoFromDownStream;
import com.assignment.flightinfo.model.ServiceUrls;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;;

/**
 * Collect information from fiver sources and return Mono
 *
 * @author datta
 */
@Service
@AllArgsConstructor
@ConfigurationProperties(prefix = "downstremservices")
public class FlightInfoService {

  @Qualifier("webClient")
  private final WebClient webClient;
  
 private ServiceUrls urls ;

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

	 System.out.println("*******************" + urls.getUrls().values());
	  
    return Mono.zip(
            getFlightInfoFromDownStream(urls.getUrls().get("serviceOne"), requestData),
            getFlightInfoFromDownStream(urls.getUrls().get("serviceTwo"), requestData),
            getFlightInfoFromDownStream(urls.getUrls().get("serviceThree"), requestData),
            getFlightInfoFromDownStream(urls.getUrls().get("serviceFour"), requestData),
            getFlightInfoFromDownStream(urls.getUrls().get("serviceFive"), requestData))
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
	  System.out.println(" ******* downStreamServiceUri *** "+ downStreamServiceUri );
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
        .bodyToMono(GetFlightInfoFromDownStream.class);
  }
}
