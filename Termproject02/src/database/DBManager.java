package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DBManager {
	public final static String JDBC_DRIVER="com.mysql.cj.jdbc.Driver";
	public final static String DB_URL ="jdbc:mysql://localhost/library?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
	public final static String USER_NAME="manager";
	public final static String PASSWORD="term01030507";
	
	public static Connection getConn(){
        Connection conn = null;
       
        try {	// 1. 드라이버 로딩
        	// 드라이버 인터페이스를 구현한 클래스를 로딩
        	// mysql, oracle 등 각 벤더사 마다 클래스 이름이 다르다.
        	// mysql은 "com.mysql.jdbc.Driver"이며, 이는 외우는 것이 아니라 구글링하면 된다.
        	// 참고로 이전에 연동했던 jar 파일을 보면 com.mysql.jdbc 패키지에 Driver 라는 클래스가 있다.
            Class.forName(JDBC_DRIVER);
            conn = DriverManager.getConnection(DB_URL, USER_NAME,PASSWORD); //2. 드라이버 연결
           
        } catch (Exception e) {
            e.printStackTrace();
        }
        
       
        return conn;
    }
	
}
