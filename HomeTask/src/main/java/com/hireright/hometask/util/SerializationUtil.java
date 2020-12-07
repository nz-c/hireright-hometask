package com.hireright.hometask.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.hireright.hometask.service.DataProvider;
import com.hireright.hometask.service.Validator;
import com.hireright.hometask.service.providers.DummyDataProvider;
import com.hireright.hometask.service.providers.JsonFileDataProvider;
import com.hireright.hometask.service.validators.MandantoryParameterValidator;
import com.hireright.hometask.service.validators.MethodValidator;

/**
 * Class provides helper methods used to serialize and deserialize
 * objects. Class uses FasterXML Jackson library to work with json data.
 * 
 * @author Aleksei Nazarov
 *
 */
public class SerializationUtil {

	/**
	 * Serialize the given object to the give {@link Writer}
	 * 
	 * @param writer Destination of serialization.
	 * @param data Object to be written.
	 * @return true if serialization succeeded, false if failed.
	 */
	public static boolean serializeTo(Writer writer, Object data) {
		ObjectMapper mapper = getMapper();
		try {
			mapper.writeValue(writer, data);
		} catch (IOException e) {
			Log.TO.error(e);
			return false;
		}
		return true;
	}
	
	/**
	 * Serialize the given object in a file.
	 * 
	 * @param fileName Destination file name.
	 * @param data Object to be written to the file.
	 * @return true if serialization succeeded, false if failed.
	 */
	public static boolean serializeToFile(String fileName, Object data) {
		ObjectMapper mapper = getMapper();
		
		try {
			mapper.writeValue(new File(fileName), data);
		} catch (IOException e) {
			Log.TO.error(e);
			return false;
		}
		return true;
	}
	
	/**
	 * Read an object from the given {@link InputSteam}
	 * 
	 * @param input Source of data.
	 * @param className Class name of the object being read.
	 * @return
	 */
	public static <T> Optional<T> deserializeFrom(InputStream input, Class<T> className) {
		ObjectMapper mapper = getMapper();
	
		try {
			return Optional.of(mapper.readValue(input, className));
		} catch (IOException e) {
			Log.TO.error(e);
		}
		
		return Optional.empty();
	}
	
	
	private static ObjectMapper getMapper() {
		ObjectMapper mapper = new ObjectMapper();
		
		mapper.addMixIn(Validator.class, ValidatorMixIn.class);
		mapper.registerSubtypes(
				   new NamedType(MethodValidator.class, "method"), 
				   new NamedType(MandantoryParameterValidator.class, "param"));
		
		mapper.addMixIn(DataProvider.class, DataProviderMixIn.class);
		mapper.registerSubtypes(
				   new NamedType(DummyDataProvider.class, "dummy"), 
				   new NamedType(JsonFileDataProvider.class, "jsonFile"));
		
		return mapper;
	}
	
	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "ruleType")
	abstract class ValidatorMixIn {
	}
	
	@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "sourceType") 
	abstract class DataProviderMixIn {
	}

}
