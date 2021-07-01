package com.assignment.flightinfo.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Flight information
 * @author datta
 *
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightInfo {
	
	@Builder.Default
	private List<String> flightNumbers = new ArrayList<>();
	
	
}
