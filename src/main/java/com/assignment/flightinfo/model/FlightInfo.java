package com.assignment.flightinfo.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * Flight information
 * @author datta
 *
 */

@Data
@Builder
public class FlightInfo {
	
	@Builder.Default
	private List<String> flightNumbers = new ArrayList<>();
	
	
}
