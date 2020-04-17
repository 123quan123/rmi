package com.HTT.classTableMapping;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class ClassTable {
	private Class<?> className;
	private String table;
	private List<PropertyName> memberList;
	private PropertyName id;
	
	public ClassTable() {
		memberList = new ArrayList<>();
	}
	
	public void setMemberList(ResultSet resultSet, Object object) {
		for (PropertyName propertyName : memberList) {
			propertyName.setField(resultSet, object);
		}
	
	}
	public String getColumnString() {
		StringBuffer buffer = new StringBuffer();
		Boolean flag = true;
		for (PropertyName name : memberList) {
			buffer.append(flag == true ? "" : ",")
			.append(name.getColumn());
			flag = false;
		}
		return buffer.toString();
	}
	
	public void addMember(PropertyName propertyName) {
		memberList.add(propertyName);
	}
	
	public Class<?> getClassName() {
		return className;
	}
	
	public void setClassName(Class<?> className) {
		this.className = className;
	}
	
	public String getTable() {
		return table;
	}
	
	public void setTable(String table) {
		this.table = table;
	}
	
	public List<PropertyName> getMemberList() {
		return memberList;
	}
	
	public void setConditions(PropertyName id) {
		this.id = id;
	}
	
	public String getIdString() {
		return this.table + "." + id.getColumn();
	}

	int getFiledCount() {
		return memberList.size();
	}
	
	public String getMemberString() {
		StringBuffer memberBuffer = new StringBuffer("");
		boolean flag = true;
		for (PropertyName propertyName : memberList) {
			memberBuffer.append(flag == true ? "" : ",")
			.append(this.table).append('.')
			.append(propertyName.getColumn());
			flag = false;
		}
		return memberBuffer.toString();
	}
	
	@Override
	public String toString() {
		StringBuffer str = new StringBuffer("ç±? :");
		
		str.append(className.getName()).append("<<=>>")
		.append("è¡?:").append(table).append("\n");
		for (PropertyName propertyName : memberList) {
			str.append("\t").append(propertyName.getField().getName())
			.append("<<=>>").append(propertyName.getColumn()).append("\n");
		}
		return str.toString();
	}
	
	
}
