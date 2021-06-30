package com.assignment.flightinfo.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * Flight information
 * @author datta
 *
 */

@Data
public class FlightInfo {
	
	
	private List<String> flightNumbers = new ArrayList<>();
	
	
}
