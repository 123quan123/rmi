package com.HTT.classTableMapping;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public class ClassTableFactory {
	private static final Map<String, ClassTable> classTableMap = new HashMap<>();

	public static void loadXmlMapping(String path) {
		try {
			new DOMXMLParser() {
				
				@Override
				public void parseElement(Element element) {
					ClassTable ct = new ClassTable();
					
					String className = element.getAttribute("className");
					String tableName = element.getAttribute("table");
					try {
						ct.setClassName(Class.forName(className));
						ct.setTable(tableName);
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
					
					new DOMXMLParser() {
						
						@Override
						public void parseElement(Element element) {
							PropertyName pt = new PropertyName();
							String property = element.getAttribute("property");
							String name = element.getAttribute("name");
							String id = element.getAttribute("id");
							try {
								pt.setField(Class.forName(className).getDeclaredField(property));
								pt.setColumn(name);
								
								if (id.length() > 0) {
									ct.setConditions(pt);
								}
								ct.addMember(pt);
							} catch (NoSuchFieldException e) {
								e.printStackTrace();
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}							
						}
					}.parseByTagName(element, "subColumn");
					classTableMap.put(className, ct);
				}
			}.parseByTagName(DOMXMLParser.getDocument(path), "mapping");
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};
		
	}
	
	public static ClassTable getClasstablemap(Class<?> klass) {
		
		return classTableMap.get(klass.getName());
	}
	
	public static ClassTable getClasstablemap(String klassName) {
		
		return classTableMap.get(klassName);
	}

	
}
