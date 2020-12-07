package com.hireright.hometask.service;

import java.util.Map;
import java.util.Optional;

/**
 * The DataProvider interface must be implemented by a class
 * that can be used to read data.
 * 
 * @author Aleksei Nazarov
 *
 */
public interface DataProvider {

	/**
	 * Read data.
	 * 
	 * @param params Filtering parameters with values
	 * @return Data according to the given filtering parameters.
	 */
	public Optional<?> read(Map<String, String> params);

}
