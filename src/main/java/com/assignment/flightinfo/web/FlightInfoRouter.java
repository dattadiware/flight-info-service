package com.assignment.flightinfo.web;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

/**
 * 
 * @author datta
 *
 */

@Configuration
public class FlightInfoRouter {
	
  /**
   * Router
   * @param handler
   * @return
   */

  @Bean
  public RouterFunction<ServerResponse> router(FlightInfoHandler handler) {

    return RouterFunctions.route(
        GET("/flight/{date}/{airportId}/{arrivalTime}/{departureTime}")
            .and(accept(APPLICATION_JSON)),
        handler::getFlightInfo);
  }
}
