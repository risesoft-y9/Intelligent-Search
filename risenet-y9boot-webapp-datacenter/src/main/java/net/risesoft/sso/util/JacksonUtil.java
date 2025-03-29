package net.risesoft.sso.util;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JacksonUtil {

	//jackson的objectMapper 设计为单例，其他地方使用时，不要重复创建
	public static ObjectMapper objectMapper = new ObjectMapper();

	static {
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public static String writeValueAsString(Object value) {
		String s = "";
		try {
			s = objectMapper.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static String writeValueAsStringWithDefaultPrettyPrinter(Object value) {
		String s = "";
		try {
			s = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static String writeValueAsString(Object value, boolean include, String... filters) {
		FilterProvider filterProvider = null;
		if (include) {
			filterProvider = new SimpleFilterProvider().addFilter("propertyFilter", SimpleBeanPropertyFilter.filterOutAllExcept(filters));
		} else {
			filterProvider = new SimpleFilterProvider().addFilter("propertyFilter", SimpleBeanPropertyFilter.serializeAllExcept(filters));
		}
		ObjectWriter writer = objectMapper.writer(filterProvider);
		String s = "";
		try {
			s = writer.writeValueAsString(value);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		return s;
	}

	public static <T> T readValue(String content, Class<T> valueType) {
		try {
			return objectMapper.readValue(content, valueType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T readValue(String content, TypeReference<T> valueTypeRef) {
		try {
			return objectMapper.readValue(content, valueTypeRef);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T readValue(String content, JavaType valueType) {
		try {
			return objectMapper.readValue(content, valueType);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> List<T> readList(String content, Class<T> valueType) {
		try {
			return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, valueType));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <K, V> List<Map<K, V>> readListOfMap(String content, Class<K> keyClass, Class<V> valueClass) {
		try {
			JavaType inner = objectMapper.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
			return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, inner));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static List<Map<String, Object>> readListOfMap(String content) {
		try {
			JavaType inner = objectMapper.getTypeFactory().constructMapType(Map.class, String.class, Object.class);
			return objectMapper.readValue(content, objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, inner));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <T> T[] readArray(String content, Class<T> valueType) {
		try {
			return objectMapper.readValue(content, objectMapper.getTypeFactory().constructArrayType(valueType));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static <K, V> HashMap<K, V> readHashMap(String content, Class<K> keyClass, Class<V> valueClass) {
		try {
			return objectMapper.readValue(content, objectMapper.getTypeFactory().constructMapType(HashMap.class, keyClass, valueClass));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static HashMap<String, Object> readHashMap(String content) {
		try {
			return objectMapper.readValue(content, objectMapper.getTypeFactory().constructMapType(HashMap.class, String.class, Object.class));
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

}
