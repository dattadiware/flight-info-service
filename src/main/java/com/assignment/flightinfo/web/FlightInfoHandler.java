package com.assignment.flightinfo.web;

import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.assignment.flightinfo.model.FlightInfo;
import com.assignment.flightinfo.service.FlightInfoService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * 
 * @author datta
 *
 */

@Component
@AllArgsConstructor
public class FlightInfoHandler {

  private final FlightInfoService service;

  /**
   * Return Flight information 
   * @param serverRequest
   * @return
   */
  
  public Mono<ServerResponse> getFlightInfo(ServerRequest serverRequest) {
	  
	
    return ok().contentType(MediaType.APPLICATION_JSON)
        
        .body(
            service.getFlightInfo(
                LocalDate.parse(serverRequest.pathVariable("date")),
                serverRequest.pathVariable("airportId"),
                LocalTime.parse(serverRequest.pathVariable("arrivalTime")),
                LocalTime.parse(serverRequest.pathVariable("departureTime"))),
            FlightInfo.class)
        
        .switchIfEmpty(notFound().build())
        .onErrorResume(error -> ServerResponse.badRequest().build());
        
  }
}
