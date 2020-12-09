package com.hr.hometask.service;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

import com.hr.hometask.util.SerializationUtil;

/**
 * Service class represents mocking service. The class is constructed
 * based on the configuration stored in a file. 
 * The mocking service is used to process ServiceRequest objects and 
 * write processing results into the provided Writer.
 * 
 * @author Aleksei Nazarov
 *
 */
public class Service {
	
	public enum STATUS {
		OK,
		ERROR,
		INVALID_REQUEST,
		NOTHING
	}
	
	private String name;
	private String path;
	private List<Validator> rules;
	private DataProvider dataProvider;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public List<Validator> getRules() {
		return rules;
	}
	public void setRules(List<Validator> rules) {
		this.rules = rules;
	}
	public DataProvider getDataProvider() {
		return dataProvider;
	}
	public void setDataProvider(DataProvider dataProvider) {
		this.dataProvider = dataProvider;
	}
	
	/**
	 * Process received {@link ServiceRequest} and write results to the {@link Writer}
	 * 
	 * @param request Request to process
	 * @param writer
	 * @return
	 * @throws IOException
	 */
	public Service.STATUS processAndWrite(ServiceRequest request, Writer writer) throws IOException {
		// 1. validation
		if (rules.stream().map(rule -> rule.validate(request)).anyMatch(p -> !p)) {
			//failed
			return Service.STATUS.INVALID_REQUEST;
		}
		
		// 2. retrieve data
		Optional<?> data = this.dataProvider.read(request.getParamsConverted());
		
		// 3. serialize data
		return data.map(s -> SerializationUtil.serializeTo(writer, s) ? Service.STATUS.OK : Service.STATUS.ERROR).orElse(Service.STATUS.NOTHING);
	}
	
}
