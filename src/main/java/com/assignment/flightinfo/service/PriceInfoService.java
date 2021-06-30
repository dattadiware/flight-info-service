package com.assignment.flightinfo.service;

import java.time.LocalDate;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.assignment.flightinfo.model.PriceInfo;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PriceInfoService {

  public Mono<PriceInfo> getPriceInfo(@NonNull final LocalDate date, @NonNull final String flightNumber) {
	  
	  
	  
	  return null ;
	  
	  
	  
  }
}
