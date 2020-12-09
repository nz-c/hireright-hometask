package com.hr.hometask.service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class represents a service request.
 * 
 * @author Aleksei Nazarov
 *
 */
public class ServiceRequest {
	private String method;
	private Map<String, String[]> params;
	public ServiceRequest() {
		this.params = new HashMap<>();
	}
	
	public ServiceRequest(String method, Map<String, String[]> params) {
		super();
		this.method = method;
		this.params = params;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public Map<String, String[]> getParams() {
		return params;
	}
	public void setParams(Map<String, String[]> params) {
		this.params = params;
	}

	public Map<String, String> getParamsConverted() {
		return this.params.entrySet().stream().filter(e -> e.getValue().length > 0).
				collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue()[0]));
	}
	
	
}
