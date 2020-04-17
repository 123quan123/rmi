package com.HTT.classTableMapping;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DataBase {
	private volatile static Connection connection;
	
	private static String url;
	private static String user;
	private static String password;

	private static int index = 0;
	
	public DataBase() {
	}
	
	public static void loadDataBaseProperties(String path) {
		PropertiesUtil.loadProperties(path);
	}
	
	public static Connection getPropertyConnection() {
		try {
			Class.forName(PropertiesUtil.getValue("driver"));
			url = PropertiesUtil.getValue("url");
			user = PropertiesUtil.getValue("user");
			password = PropertiesUtil.getValue("password");
			connection = DriverManager.getConnection(url, user, password);
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return connection;
	}
	
	private PreparedStatement getState(String sql) {
		getPropertyConnection();
		try {
			PreparedStatement state = connection.prepareStatement(sql);

			return state;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private ResultSet getResultSet(String sql) {
		ResultSet result = null;
		try {
			PreparedStatement state = getState(sql);
			result = state.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return result;
	}

	@SuppressWarnings("unchecked")
	public  <T> List<T> getList(Class<?> klass) {
		System.out.println("访问数据�?" + ++index);
		ClassTable classTable = getClassTable(klass);
		if (classTable == null) {
			return null;
		}
		List<T> list = new ArrayList<T>();
		String sql = "SELECT " + classTable.getMemberString() 
			+ " FROM " + classTable.getTable();
		
		ResultSet resultSet = getResultSet(sql);
		try {
			while (resultSet.next()) {
				Object object = klass.newInstance();
				classTable.setMemberList(resultSet, object);
				list.add((T) object);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public  <T> T getById(Class<?> klass, Object id) {
		ClassTable classTable = getClassTable(klass);
		if (classTable == null) {
			return null;
		}
		String sql = "SELECT " + classTable.getMemberString()
			+ " FROM " + classTable.getTable() + " WHERE "
			+ classTable.getIdString() + " = '" + id + "'";
		
		//state.setObject(1, id);对于�?
		ResultSet resultSet = getResultSet(sql);
		try {
			if (resultSet.next()) {
				Object object = klass.newInstance();
				classTable.setMemberList(resultSet, object);
				return (T) object;
			}
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public  int save(Object obj) {
		Class<?> klass = obj.getClass();
		ClassTable classTable = getClassTable(klass);
		if (classTable == null) {
				return -2;
		}
		String sql = getInsertSql( classTable);
		
		PreparedStatement statement = setQuestionMark(sql, obj);
		try {
			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	private  PreparedStatement setQuestionMark(String sql, Object obj) {
		Class<?> klass = obj.getClass();
		PreparedStatement statement = getState(sql);
		int index = 1;
		for (Field field : klass.getDeclaredFields()) {
			field.setAccessible(true);
			Object value;
			try {
				value = field.get(obj);
			} catch (Exception e) {
				value = null;
			}
			
			try {
				statement.setObject(index++, value);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return statement;
	}
	
	private  String getInsertSql(ClassTable classTable) {
		StringBuffer buffer = new StringBuffer(" INSERT INTO ");
		buffer.append(classTable.getTable()).append("(")
			.append(classTable.getColumnString()).append(") VALUES ")
			.append("(");
		boolean flag = true;
		int filedCount = classTable.getFiledCount();
		for (int i = 0; i < filedCount; i++) {
			buffer.append(flag ? "?" : ",?");
			flag = false;
		}
		buffer.append(")");
		
		return buffer.toString();
	}
	
	private  ClassTable getClassTable(Class<?> klass) {
		ClassTable classTable = ClassTableFactory.getClasstablemap(klass);
		if (classTable == null) {
			ClassTableFactory.loadXmlMapping("/database.config.xml");
			classTable = ClassTableFactory.getClasstablemap(klass);
		}
		return classTable;
	}
	
	public  int modify(Object obj) {
		Class<?> klass = obj.getClass();
		ClassTable classTable = getClassTable(klass);
			if (classTable == null) {
				return -2;
		}
//
//		String sql = "UPDATE " + classTable.getTable()
//			+ " SET name = '" + name + "' WHERE " 
//			+ classTable.getIdString() + " = '" + Id + "'"; 
		String str =  getModifySql(classTable);
		String sql = str.replaceAll(",", " = ?,").replaceAll("!", " = ?");
		
		PreparedStatement statement = setUpDateQuestionMark(sql, obj);
		try {
			return statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	private PreparedStatement setUpDateQuestionMark(String sql, Object obj) {
		Class<?> klass = obj.getClass();
		PreparedStatement statement = getState(sql);
		int index = 1;
		for (Field field : klass.getDeclaredFields()) {
			field.setAccessible(true);
			Object value;
			try {
				value = field.get(obj);
			} catch (Exception e) {
				value = null;
			}
			try {
				statement.setObject(index++, value);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				statement.setObject(index, SetId(obj));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return statement;
	}
	
	private Object SetId(Object obj) {
		Class<?> klass = obj.getClass();
		Field field = null;
		Object value = null;
		try {
			try {
				field = klass.getDeclaredField("id");
				field.setAccessible(true);
				value = field.get(obj);
				} catch (Exception e) {
					value = null;
				}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return value;
	}
	
	private String getModifySql(ClassTable classTable) {
		StringBuffer buffer = new StringBuffer(" UPDATE ");
		buffer.append(classTable.getTable()).append(" SET ")
			.append(classTable.getColumnString()).append("!")
			.append(" WHERE ").append(classTable.getIdString())
			.append(" = ").append("?");
		return buffer.toString();
	}
	
	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
