package com.assignment.flightinfo.exception;

import org.slf4j.Logger;
import org.springframework.web.reactive.function.client.ClientResponse;

public class ApiError {

 
public static void logTraceResponse(Logger log, ClientResponse response) {
    if (log.isTraceEnabled()) {
      log.trace("Response status: {}", response.statusCode());
      log.trace("Response headers: {}", response.headers().asHttpHeaders());
      response
          .bodyToMono(String.class)
          .subscribe(body -> log.trace("Response body: {}", body));
    }
  }
}
