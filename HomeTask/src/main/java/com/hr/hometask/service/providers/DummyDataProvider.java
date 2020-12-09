package com.hr.hometask.service.providers;

import java.util.Map;
import java.util.Optional;

import com.hr.hometask.service.DataProvider;

public class DummyDataProvider implements DataProvider {
	

	public DummyDataProvider() {
	}

	@Override
	public Optional<?> read(Map<String, String> params) {
		return Optional.of("Dummy Data Provider");
	}

}
