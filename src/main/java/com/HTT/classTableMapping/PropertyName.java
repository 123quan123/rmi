package com.HTT.classTableMapping;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PropertyName {
	private Field field;
	private String column;
	
	public Field getField() {
		return field;
	}
	
	public void setField(ResultSet resultSet, Object object) {
		
		try {
			field.setAccessible(true);
			Object value = resultSet.getObject(column);
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
	
	public void setField(Field field) {
		this.field = field;
	}
	
	public String getColumn() {
		return column;
	}
	
	public void setColumn(String column) {
		this.column = column;
	}

	@Override
	public String toString() {
		return field + "<<=>>" + column;
	}
}
