package com.assignment.flightinfo;

import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.assignment.flightinfo.model.FlightInfo;
import com.assignment.flightinfo.web.FlightInfoHandler;

/**
 * Flight information reactive service
 * @author datta
 *
 */

@SpringBootApplication
public class FlightInfoApplication {

  public static void main(String[] args) {
    SpringApplication.run(FlightInfoApplication.class, args);
  }

  @Configuration
  static class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> timerRouterRunction(FlightInfoHandler handler) {

      return RouterFunctions.route()
          .GET(
              "/flight/{date}/{airportId}/{arrivalTime}/{departureTime}",
              accept(MediaType.APPLICATION_JSON),
              request -> ServerResponse.ok().body(handler.getFlightInfo(request), FlightInfo.class))
          
          .build();
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
