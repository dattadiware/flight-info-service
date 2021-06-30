package com.assignment.flightinfoservice;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.assignment.flightinfoservice.model.FlightInfo;
import com.assignment.flightinfoservice.service.FlightInfoService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class FlightInfoServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(FlightInfoServiceApplication.class, args);
  }

  @Configuration
  static class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> timerRouterRunction(FlightInfoHandler handler) {

      return RouterFunctions.route()
          .GET(
              "/flight/{date}/{airportId}/{arrivalTime}/{departureTime}",
              accept(MediaType.TEXT_PLAIN),
              request -> ServerResponse.ok().body(handler.getFlightInfo(request), FlightInfo.class))
          
          .build();
    }
  }

  @Component
  @AllArgsConstructor
  static class FlightInfoHandler {

    private final FlightInfoService service;

    public Mono<ServerResponse> getFlightInfo(ServerRequest serverRequest) {

      return ok().contentType(MediaType.APPLICATION_JSON)
          .body(
              service.getFlightInfo(
                  LocalDate.parse(serverRequest.pathVariable("date")),
                  serverRequest.pathVariable("airportId"),
                  LocalTime.parse(serverRequest.pathVariable("arrivalTime")),
                  LocalTime.parse(serverRequest.pathVariable("departureTime"))),
              FlightInfo.class);
    }
  }

  @Configuration(proxyBeanMethods = false)
  class DateFormatConfiguration {

    private final DateTimeFormatterRegistrar registrar = new DateTimeFormatterRegistrar();

    public DateFormatConfiguration() {
      registrar.setDateFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      registrar.setTimeFormatter(DateTimeFormatter.ofPattern("HH:mm:ss"));
      registrar.setDateTimeFormatter(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
    }

    @Autowired
    public void configure(final FormattingConversionService conversionService) {
      registrar.registerFormatters(conversionService);
    }
  }
}
