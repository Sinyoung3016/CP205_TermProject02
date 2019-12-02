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
	
	public synchronized static String searchRegisterByNum(int Book_Number) {

		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;


		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			

			String sql;
			sql = "SELECT *FROM usersbook WHERE Num='" +Book_Number+ "'";
			
			rs = state.executeQuery(sql);

			if (rs.next()) {
				return rs.getString("ID");
			}else {
				return null;
			}


		} catch (Exception e) {
			return null;
		} finally {	
			try {
				if (state != null)	state.close();
				if (conn != null)	conn.close();
				if(rs!=null)	rs.close();
			} catch (SQLException e) {}
		}
	}
	
	public synchronized static String CheckRequest(int Book_Number) {//요청이 있으면 더 못받게

		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;


		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			

			String sql;
			sql = "SELECT *FROM usersbook WHERE Num='" +Book_Number+ "'";
			
			rs = state.executeQuery(sql);

			if (rs.next()) {
				return rs.getString("Requester_ID");
			}else {
				return null;
			}


		} catch (Exception e) {
			return null;
		} finally {	
			try {
				if (state != null)	state.close();
				if (conn != null)	conn.close();
				if(rs!=null)	rs.close();
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
	public synchronized static List<Integer> getBorrowedBookNumber(String ID) throws SQLException {//삽입 
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;

		try {
			conn = getConn();
			state = conn.createStatement();// conn연결정보를 state로 생성.
			String sql;
			sql = "SELECT *FROM usersbook WHERE Requester_ID='" +ID+ "' AND STATUS= 'Borrowed'";
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
	public synchronized static List<Integer> getLoanedBookNumber(String ID) throws SQLException {//ID는 빌려간사람 ID(Requester_ID)
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;

		try {
			conn = getConn();
			state = conn.createStatement();// conn연결정보를 state로 생성.
			String sql;
			sql = "SELECT *FROM usersbook WHERE id='" +ID+ "' AND STATUS= 'Borrowed'";
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
	
	public synchronized static void BorrowRequest(int BookNum, String Requester_ID) throws SQLException {//빌리기
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			

	        String sql = "update usersbook set Requester_ID=?, Request_Info=? where Num = ?";

	        try {
	        	pstmt = conn.prepareStatement(sql);

	        	pstmt.setString(1,Requester_ID); 
	        	pstmt.setString(2, "빌리다"); 
	        	pstmt.setString(3, BookNum+"");
	        	pstmt.executeUpdate();
	        	
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }


			pstmt.close();
			conn.close();
		} finally {	
			try {
				if (pstmt != null)	pstmt.close();
				if (conn != null)	conn.close();
			} catch (SQLException e) {}
		}
	}
	
	public synchronized static void NoBorrow(int BookNum, String Requester_ID) throws SQLException {//빌리기
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			   String sql = "update usersbook set Requester_ID=?, Request_Info=? where Num = ?";

		        try {
		        	pstmt = conn.prepareStatement(sql);

		        	pstmt.setString(1,"NULL"); pstmt.setString(2, "NULL"); pstmt.setString(3, BookNum+"");
		        	pstmt.executeUpdate();
		        	
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }


			
			pstmt.close();
			conn.close();
		} finally {	
			try {
				if (pstmt != null)	pstmt.close();
				if (conn != null)	conn.close();
			} catch (SQLException e) {}
		}
	}
	
	public synchronized static void BorrowBook(int BookNum) throws SQLException {//빌리기
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String sql = "update usersbook set status=? where Num = ?";

	        try {
	        	pstmt = conn.prepareStatement(sql);

	        	pstmt.setString(1,"Borrowed"); 
	        	pstmt.setString(2, BookNum+"");
	        	pstmt.executeUpdate();
	        } catch (SQLException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }


			
			pstmt.close();
			conn.close();
		} finally {	
			try {
				if (pstmt != null)	pstmt.close();
				if (conn != null)	conn.close();
			} catch (SQLException e) {}
		}
	}
}
