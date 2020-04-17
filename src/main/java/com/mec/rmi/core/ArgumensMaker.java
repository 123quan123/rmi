package com.mec.rmi.core;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class ArgumensMaker {
	public static final Gson GSON = new GsonBuilder().create();
	public static final Type type = new TypeToken<Map<String, String>>(){}.getType();
	private Map<String, String> paraMap;
	
	{
		paraMap = new HashMap<String, String>();
	}
	
	public ArgumensMaker() {
	}
	
	public ArgumensMaker(String str) {
		paraMap = GSON.fromJson(str, type);
	}
	
	public ArgumensMaker addArg(String name, Object value) {
		paraMap.put(name, GSON.toJson(value));
		return this;
	}
	
	public Object getValue(String name, Type type) throws Exception {
		Object valueObj = paraMap.get(name);
		if (valueObj == null) {
			return null;
		}
		Object result = null;
		try {
		    result = GSON.fromJson(valueObj.toString(), type);
        } catch (Exception e) {
            throw new Exception("rmi gson type transfer failure.");
        }
		return result;
	}
	
	public Object getValue(String name, Class<?> klass) {
		String value = paraMap.get(name);
		if (value == null) {
			return null;
		}
		return GSON.fromJson(value, klass);
	}
	
	public String mapToJson() {
		return GSON.toJson(paraMap, type);
	}
}
