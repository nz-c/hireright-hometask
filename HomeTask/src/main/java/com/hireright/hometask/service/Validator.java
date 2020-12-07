package com.hireright.hometask.service;

/**
 * Service request validation.
 * 
 * @author Aleksei Nazarov
 *
 */
public interface Validator {
	
	/**
	 * Validate the give {@link ServiceRequest}
	 * 
	 * @param request request to validate
	 * @return true if valid, otherwise false
	 */
	public boolean validate(ServiceRequest request);
}
