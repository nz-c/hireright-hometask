package com.hr.hometask.service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.hr.hometask.util.SerializationUtil;

/**
 * Service Registry. Can be used by clients to register and retrieve 
 * registered services.
 * 
 * @author Aleksei Nazarov
 *
 */
public class ServiceRegistry {

	private static ServiceRegistry registry;
	
	public static ServiceRegistry getInstance() {
		if (registry == null) {
			registry = new ServiceRegistry();
		}
		
		return registry;
	}
	
	private Map<String, Service> registeredService;
	
	private ServiceRegistry() {
		this.registeredService = new HashMap<>();
	}

	/**
	 * Read serialized service from the source and register it in the registry.
	 * 
	 * @param from reading source
	 */
	public void loadAndRegister(InputStream from) {
		SerializationUtil.deserializeFrom(from, Service.class).ifPresent(this::register);
	}
	
	public void register(Service service) {
		register(service.getPath(), service);
	}
	
	public void register(String path, Service service) {
		registeredService.put(path, service);
	}
	
	public Optional<Service> get(String path) {
		return Optional.ofNullable(registeredService.get(path));
	}
}
