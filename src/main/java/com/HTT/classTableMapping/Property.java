package com.HTT.classTableMapping;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class Property {
	private static final Map<String, String> map = new HashMap<>();
	
	public Property() {
	}
	
	public static void loadProperties(String path) {
		InputStream is = Class.class.getResourceAsStream(path);
		loadProperties(is);
	}
	
	public static void loadProperties(InputStream is) {
		try {
			Properties properties = new Properties();
			properties.load(is);
			
			Set<Object> keySet = properties.keySet();
			Iterator<Object> iterator = keySet.iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				String value = (String) properties.get(key);
				
				map.put(key, value);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static String getValue(String key) {
		return map.get(key);
	}
}
