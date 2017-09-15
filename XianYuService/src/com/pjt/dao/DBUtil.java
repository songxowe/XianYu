package com.pjt.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil {
	
	
	
	public static Connection getConnection(){
		Connection conn=null;
		
		String driverName="oracle.jdbc.driver.OracleDriver";
		String url="jdbc:oracle:thin:@127.0.0.1:1521:orcl";
		String user="scott";
		String password="pjt111";
		
		try {
			Class.forName(driverName);
			conn=DriverManager.getConnection(url, user, password);
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		
		
		
		return conn;
		
	}
	
	
	public static void close(Connection conn,PreparedStatement ps,ResultSet rs){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		if(ps!=null){
			try {
				ps.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		if(conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				
				e.printStackTrace();
			}
		}
		
		
		
		
	}
	
	
	public static void main(String[] args) {
		System.out.println(getConnection());
	}

}
