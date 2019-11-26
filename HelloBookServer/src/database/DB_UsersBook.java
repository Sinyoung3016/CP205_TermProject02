package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import book.Book;

public class DB_UsersBook extends DBManager{

	public synchronized static void uploadBook(int bookNum,String ID, String title) throws SQLException {//삽입 
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String sql;
			sql = "INSERT INTO usersbook (Num ,ID,Book,status)VALUES (?,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, bookNum+"");
			pstmt.setString(2, ID);
			pstmt.setString(3, title);
			pstmt.setString(4, "Registered" );

			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} finally {	
			try {
				if (pstmt != null)	pstmt.close();
				if (conn != null)	conn.close();
			} catch (SQLException e) {}
		}
	}
	
	public synchronized static List<Integer> getRegisteredBookNumber(String ID) throws SQLException {//삽입 
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;

		try {
			conn = getConn();
			state = conn.createStatement();// conn연결정보를 state로 생성.
			String sql;
			sql = "SELECT *FROM usersbook WHERE id='" +ID+ "' AND STATUS= 'Registered'";
			List<Integer> book_num_list=new ArrayList<>();
			rs = state.executeQuery(sql);
			if(rs==null) {
				return null;//존재 X
			}
			while (rs.next()) {//나중에 바꿔
				book_num_list.add(Integer.parseInt(rs.getString("Num")));


			}
			return book_num_list;		
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}  finally {	
			try {
				if (state != null)	state.close();
				if (conn != null)	conn.close();
				if(rs!=null)	rs.close();
			} catch (SQLException e) {}
		}
	}
}
