package com.assignment.flightinfo.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@ConfigurationProperties(prefix = "downstremservices")
@Getter
public class ServiceUrls {

  public final static Map<String, String> URLS = new HashMap<>();
  
}
