package org.universAAL.ucc.configuration.model;

/**
 * 
 * A class to handle cardinalities like 0..1,  0..*, 1..1
 * 
 * @author Sebastian.Schoebinge
 *
 */

public class Cardinality {
	
	
	String bottom;
	String top;
	
	String cardinality;
	
	/**
	 * Splits the input string by ".." into top and bottom limit.
	 * @param cardinality
	 */
	public Cardinality(String cardinality) {
		this.cardinality = cardinality;
		if(cardinality != null && cardinality.contains("..")){
			String[] result = cardinality.split("\\.\\.");
			if(result.length > 1){
				bottom = result[0];
				top = result[1];
			}
		}
	}
	
	/**
	 * Checks if the cardinality has the bottom limit 0 or not.
	 * @return true if the bottom limit doesnt fits "0".
	 */
	public boolean isRequired(){
		if(bottom != null)
			return !bottom.equals("0");
		return false;
	}
	
	/**
	 * 
	 * @return true if the top limit isn't 1 or 0. 
	 */
	public boolean allowMultiselection() {
		if(top != null)
		{
			return !top.equals("1") && !top.equals("0");
		}
		return false;
	}
	
	@Override
	public String toString() {
		return cardinality;
	}
}
