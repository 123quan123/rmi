package com.HTT.classTableMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class PropertiesUtil {
	private static final Map<String, String> propertyMap = new HashMap<>();
	
	
	public static void loadProperties(String path) {
		InputStream is = Class.class.getResourceAsStream(path);
		loadProperties(is);
	}
	
	public static void loadProperties(InputStream is) {
		java.util.Properties properties = new java.util.Properties();
		try {
			properties.load(is);
			Set<Object> keySet = properties.keySet();
			Iterator<Object> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = properties.getProperty(key);
				propertyMap.put(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static Set<String> getSet() {
		return propertyMap.keySet();
	}
	
	public static String getValue(String key) {
		return propertyMap.get(key);
	}
		
 }
