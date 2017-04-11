package lab4;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectDB {
	private Connection connect;
	public ConnectDB() {
		// TODO Auto-generated constructor stub
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.print("Error loading Mysql Driver!");
			e.printStackTrace();
		}
		try {
			connect = DriverManager.getConnection(
					"jdbc:mysql://localhost:3307/lab5", "root", "1111");
		} catch (Exception e) {
			System.out.print("get data error!");
			e.printStackTrace();
		}
	}
	public Connection getconnect(){
		return connect;
	}
}
