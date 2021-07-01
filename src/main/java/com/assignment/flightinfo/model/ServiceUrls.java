package com.assignment.flightinfo.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Component
@ConfigurationProperties(prefix = "downstremservices")
@Getter
@AllArgsConstructor
public class ServiceUrls {

  public Map<String, String> urls = new HashMap<>();
  
}
