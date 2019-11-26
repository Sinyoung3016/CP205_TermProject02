package database;

import java.sql.Connection;
import java.sql.DriverManager;

public abstract class DBManager {
	public final static String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
	public final static String DB_URL ="jdbc:mysql://localhost/library?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public final static String USER_NAME="manager";
	public final static String PASSWORD="term01030507";
	
	public static Connection getConn(){
        Connection conn = null;
       
        try {	// 1. ����̹� �ε�
        	// ����̹� �������̽��� ������ Ŭ������ �ε�
        	// mysql, oracle �� �� ������ ���� Ŭ���� �̸��� �ٸ���.
        	// mysql�� "com.mysql.jdbc.Driver"�̸�, �̴� �ܿ�� ���� �ƴ϶� ���۸��ϸ� �ȴ�.
        	// ������ ������ �����ߴ� jar ������ ���� com.mysql.jdbc ��Ű���� Driver ��� Ŭ������ �ִ�.
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER_NAME,PASSWORD); //2. ����̹� ����
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        
       
        return conn;
    }
	
}