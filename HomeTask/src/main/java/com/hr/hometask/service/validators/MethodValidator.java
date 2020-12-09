package com.hr.hometask.service.validators;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hr.hometask.service.ServiceRequest;
import com.hr.hometask.service.Validator;
import com.hr.hometask.util.Log;

/**
 * Validator is used to check whether service request 
 * has allowed query method, e.g. GET or POST.
 * 
 * @author Aleksei Nazarov
 *
 */
public class MethodValidator implements Validator {
	private boolean allAllowed;
	private List<String> allowedMethods;
	
	public MethodValidator() {
		this.allowedMethods = new ArrayList<>();
		this.allAllowed = true;
	}

	public MethodValidator(String ... allowedMethods) {
		this.allowedMethods = Arrays.asList(allowedMethods);
		this.allAllowed = this.allowedMethods.contains("*");
	}

	public List<String> getAllowedMethods() {
		return allowedMethods;
	}

	public void setAllowedMethods(List<String> allowedMethods) {
		this.allowedMethods = allowedMethods;
		this.allAllowed = this.allowedMethods.contains("*");
	}

	@Override
	public boolean validate(ServiceRequest request) {
		boolean ret = this.allAllowed 
				|| this.allowedMethods.contains(request.getMethod());
		
		if (!ret) Log.TO.info("Invalid request method: " + request.getMethod());
		
		return ret;
	}

}
