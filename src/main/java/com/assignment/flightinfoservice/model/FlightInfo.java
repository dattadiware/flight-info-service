package com.assignment.flightinfoservice.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class FlightInfo {
	
	private List<String> flightNumbers = new ArrayList<>();
	
	
}
