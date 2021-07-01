package com.assignment.flightinfo;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.datetime.standard.DateTimeFormatterRegistrar;
import org.springframework.format.support.FormattingConversionService;

/**
 * Flight information reactive service
 *
 * @author datta
 */
@SpringBootApplication
@EnableConfigurationProperties
public class FlightInfoApplication {

  public static void main(String[] args) {
    SpringApplication.run(FlightInfoApplication.class, args);
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
