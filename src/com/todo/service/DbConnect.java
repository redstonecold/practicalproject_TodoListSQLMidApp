package com.todo.service;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbConnect {
	//java.sql.Connection : 데이터베이스 연결, 종료
	private static Connection conn = null;
	
	public static void closeConnection() {
		if (conn != null) {
			try {
				conn.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static Connection getConnection() {
		if (conn == null) {
			try {
				//SQLite JDBC 체크
				Class.forName("org.sqlite.JDBC");
				//SQLite 데이터베이스 파일에 연결
				conn = DriverManager.getConnection("jdbc:sqlite:"+"todolistmid.db");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conn;
	}
	
}
