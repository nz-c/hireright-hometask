package com.hireright.hometask.service.providers;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.hireright.hometask.service.DataProvider;
import com.hireright.hometask.util.Log;

/**
 * Use json file as a data provider. 
 * 
 * @author Aleksei Nazarov
 *
 */
public class JsonFileDataProvider implements DataProvider {
	private String fileName;
	private String dataField;

	public JsonFileDataProvider() {
		
	}
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public String getDataField() {
		return dataField;
	}

	public void setDataField(String dataField) {
		this.dataField = dataField;
	}

	@Override
	public Optional<?> read(Map<String, String> params) {
		ObjectMapper mapper = new ObjectMapper();
		
		SimpleModule module = new SimpleModule();
		module.addDeserializer(ResponseData.class, new ItemDeserializer(dataField, params));
		mapper.registerModule(module);
		
		try {
			return mapper.readValue(new File(this.fileName),  new TypeReference<List<ResponseData>>(){})
					.stream().filter(e -> e != null).findFirst();
		} catch (IOException e) {
			Log.TO.error(e);
		}
		return Optional.empty();
	}
	
	/**
	 * Represents response, wraps actual data read from the source file.
	 */
	class ResponseData {
		private String data;
		
		
		public ResponseData() {
		}

		public ResponseData(String data) {
			this.data = data;
		}

		public String getData() {
			return data;
		}

		public void setData(String data) {
			this.data = data;
		}
		
	}
	
	/**
	 * Custom deserializer is used to filter items using the given
	 * filtering criteria.
	 * 
	 */
	class ItemDeserializer extends StdDeserializer<ResponseData> { 
		
		private static final long serialVersionUID = 7425037862625217986L;
		
		private String dataField;
		private Map<String, String> params;

	    public ItemDeserializer(String dataField, Map<String, String> params) { 
	        this(null); 
	        this.dataField = dataField;
	        this.params = params;
	    } 

	    public ItemDeserializer(Class<?> vc) { 
	        super(vc); 
	    }

	    @Override
	    public ResponseData deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
	        JsonNode node = jp.getCodec().readTree(jp);
	        
	        Iterator<Entry<String, JsonNode>> it = node.fields();
	        Entry<String, JsonNode> e = null;
	        Map<String, String> pc = new HashMap<>(params);
	        String filterValue = null;
	        String value = null;
	        while (it.hasNext()) {
	        	e = it.next();
	        	
	        	if (this.dataField.equalsIgnoreCase(e.getKey())) {
	        		value = e.getValue().asText();
	        	}
	        	
	        	filterValue = pc.remove(e.getKey());
	        	
	        	if (filterValue != null && !filterValue.equalsIgnoreCase(e.getValue().asText())) {
	        			// does not satisfy filtering condition
	        			return null; // will be filtered out later
	        	}
			}
	        
	        if (!pc.isEmpty()) {
	        	// not all filters has been applied
	        	return null; 
	        }
	        
	        // found
	        return new ResponseData(value);
	    }
	}

}
