package com.hr.hometask.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.hr.hometask.service.Service;
import com.hr.hometask.service.ServiceRegistry;
import com.hr.hometask.service.ServiceRequest;

public class WeatherTest {
	
	@Test
	public void testOkRequest() {
		Service service = getService();
		
		// create and test request
		Map<String, String[]> params = null;
		ServiceRequest request = null;
		StringWriter writer = null;
		
		params = new HashMap<>();
		params.put("cityCode", new String[] {"TLN"});
		request = new ServiceRequest("GET", params);
		writer = new StringWriter();
				
		try {
			assertEquals(Service.STATUS.OK , service.processAndWrite(request, writer));
			assertEquals("{\"data\":\"Cloudy 4\u00B0C, Percipitation 30%, Wind 5m/s NW\"}", writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testParamValueCase() {
		Service service = getService();
		
		// create and test request
		Map<String, String[]> params = null;
		ServiceRequest request = null;
		StringWriter writer = null;
		
		params = new HashMap<>();
		params.put("cityCode", new String[] {"tln"});
		request = new ServiceRequest("GET", params);
		writer = new StringWriter();
				
		try {
			assertEquals(Service.STATUS.OK , service.processAndWrite(request, writer));
			assertEquals("{\"data\":\"Cloudy 4\u00B0C, Percipitation 30%, Wind 5m/s NW\"}", writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testOkPostRequest() {
		Service service = getService();
		
		// create and test request
		Map<String, String[]> params = null;
		ServiceRequest request = null;
		StringWriter writer = null;
		
		params = new HashMap<>();
		params.put("cityCode", new String[] {"TLN"});
		request = new ServiceRequest("POST", params);
		writer = new StringWriter();
				
		try {
			assertEquals(Service.STATUS.OK , service.processAndWrite(request, writer));
			assertEquals("{\"data\":\"Cloudy 4\u00B0C, Percipitation 30%, Wind 5m/s NW\"}", writer.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testIncorrectRequest() {
		Service service = getService();
		
		// create and test request
		Map<String, String[]> params = null;
		ServiceRequest request = null;
		StringWriter writer = null;
		
		params = new HashMap<>();
		params.put("cityCodeWrong", new String[] {"TLN"});
		request = new ServiceRequest("GET", params);
		writer = new StringWriter();
		
		try {
			assertEquals(Service.STATUS.INVALID_REQUEST , service.processAndWrite(request, writer));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testNotFound() {
		Service service = getService();
		
		// create and test request
		Map<String, String[]> params = null;
		ServiceRequest request = null;
		StringWriter writer = null;
		
		params = new HashMap<>();
		params.put("cityCode", new String[] {"XYZ"});
		request = new ServiceRequest("GET", params);
		writer = new StringWriter();
		
		try {
			assertEquals(Service.STATUS.NOTHING , service.processAndWrite(request, writer));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Service getService() {
		assertNotNull(ServiceRegistry.getInstance());
		
		try {
			ServiceRegistry.getInstance().loadAndRegister(new FileInputStream(new File("target/classes/serviceWeather.json")));
		} catch (Exception e) {
			e.printStackTrace();
		}

		// check service is created
		Service service = ServiceRegistry.getInstance().get("/weather").orElse(null);
		assertNotNull(service);
		
		return service;
	}
}
