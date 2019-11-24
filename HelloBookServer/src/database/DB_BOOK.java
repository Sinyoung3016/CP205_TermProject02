package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import book.Book;

public class DB_BOOK  extends DBManager{
	
	
	public synchronized static List<Book> getAllBook() {
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		List<Book > returnBookList = new ArrayList<>();
		String [] BookInfo=new String[11];
		try {
			conn = getConn();
			state = conn.createStatement();// conn연결정보를 state로 생성.
			String sql;
			sql = "SELECT *FROM BOOK";	
			rs = state.executeQuery(sql);
			while (rs.next()) {//나중에 바꿔
				BookInfo[0] = rs.getString("Book_Number");
				BookInfo[1] = rs.getString("Title");
				BookInfo[2] = rs.getString("Auther");
				BookInfo[3] = rs.getString("Publisher");
				BookInfo[4] = rs.getString("Genre");
				BookInfo[5] = rs.getString("Book_Condition");
				BookInfo[6] = rs.getString("Full_Price");
				BookInfo[7] = rs.getString("Sale_Price");//지우고
				BookInfo[8] = rs.getString("Lend_Price");
				BookInfo[9] = rs.getString("Rental_Status");
				BookInfo[10] = rs.getString("Introduction");
				returnBookList.add(new Book(BookInfo));

			}
			return returnBookList;
				
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
	
	public synchronized static void deleateBook(int Book_Number) {//삭제하고 
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConn();

			String sql;
			sql = "DELETE FROM Book WHERE Book_Number=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Book_Number+"");
			pstmt.execute();

			pstmt.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}  finally {	
			try {
				if (pstmt != null)	pstmt.close();
				if (conn != null)	conn.close();
			} catch (SQLException e) {}
		}
	}
	public synchronized static void insertBook(int Book_Number,String Title,String Auther, String Publisher, String Genre, String Book_Condition,int Full_Price, int Sale_Price, int Lend_Price, Boolean Rental_Status, String Introduction) {//삽입 
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String insertRentalAvailability="1";
			
			String sql;
			sql = "INSERT INTO book (Book_Number, Title, Auther, Publisher, Genre, Book_Condition,Full_Price,Sale_Price,Lend_Price,Rental_Status,Introduction)VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Book_Number+"");
			pstmt.setString(2, Title);
			pstmt.setString(3, Auther);
			pstmt.setString(4, Publisher);
			pstmt.setString(5, Genre );
			pstmt.setString(6, Book_Condition );
			pstmt.setString(7, Full_Price+"");
			pstmt.setString(8, Sale_Price+"");
			pstmt.setString(9, Lend_Price+"");
			if(Rental_Status.equals("true")) {
				insertRentalAvailability="1";
			}else if(Rental_Status.equals("false")){
				insertRentalAvailability="0";
			}
			pstmt.setString(10,  insertRentalAvailability);
			pstmt.setString(11, Introduction );
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}  finally {	
			try {
				if (pstmt != null)	pstmt.close();
				if (conn != null)	conn.close();
			} catch (SQLException e) {}
		}
	}
	
	public synchronized static void changeBook(int Book_Number,String Title,String Auther, String Publisher, String Genre, String Book_Condition,int Full_Price, int Sale_Price, int Lend_Price, Boolean Rental_Status, String Introduction) {//삽입 
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			
			deleateBook(Book_Number);
			
			conn = getConn();

			String insertRentalAvailability="1";
			
			String sql;
			sql = "INSERT INTO book (Book_Number, Title, Auther, Publisher, Genre, Book_Condition,Full_Price,Sale_Price,Lend_Price,Rental_Status,Introduction)VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Book_Number+"");
			pstmt.setString(2, Title);
			pstmt.setString(3, Auther);
			pstmt.setString(4, Publisher);
			pstmt.setString(5, Genre );
			pstmt.setString(6, Book_Condition );
			pstmt.setString(7, Full_Price+"");
			pstmt.setString(8, Sale_Price+"");
			pstmt.setString(9, Lend_Price+"");
			if(Rental_Status.equals("true")) {
				insertRentalAvailability="1";
			}else if(Rental_Status.equals("false")){
				insertRentalAvailability="0";
			}
			pstmt.setString(10,  insertRentalAvailability);
			pstmt.setString(11, Introduction );

			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}  finally {	
			try {
				if (pstmt != null)	pstmt.close();
				if (conn != null)	conn.close();
			} catch (SQLException e) {}
		}
	}

	public synchronized static Book searchBook(int Book_Number) {//등록번호로 찾기-관리자가 사용할 메소드
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		Book returnBook=null;
		String[] BookInfo = new String[11];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			

			String sql;
			sql = "SELECT *FROM book WHERE Book_Number='" +Book_Number+ "'";
			
			rs = state.executeQuery(sql);
			if(rs==null) {
				return null;//존재 X
			}
			if (rs.next()) {
				BookInfo[0] = rs.getString("Book_Number");
				BookInfo[1] = rs.getString("Title");
				BookInfo[2] = rs.getString("Auther");
				BookInfo[3] = rs.getString("Publisher");
				BookInfo[4] = rs.getString("Genre");
				BookInfo[5] = rs.getString("Book_Condition");
				BookInfo[6] = rs.getString("Full_Price");
				BookInfo[7] = rs.getString("Sale_Price");
				BookInfo[8] = rs.getString("Lend_Price");
				BookInfo[9] = rs.getString("Rental_Status");
				BookInfo[10] = rs.getString("Introduction");
				
				returnBook=new Book(BookInfo);

			}
			return returnBook;

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
	public synchronized static List<Book> searchBook(String info) {//한개로 찾기 ex)제목이면 Title-~~~, 작가면 Auther-~~~이런식으로 입력받아서 :로 스플릿 해서 경우 나눔
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		List<Book> returnBookList=new ArrayList<>();
		String[] BookInfo = new String[9];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			
			String[] searchInfo=info.split("-");

			String sql;
			sql = "SELECT *FROM book WHERE "+searchInfo[0]+" like '%" +searchInfo[1]+ "%'";
			
			rs = state.executeQuery(sql);
			if(rs==null) {
				System.out.println("존재안해");
				return null;//존재 X
			}
			while (rs.next()) {//나중에 바꿔
				BookInfo[0] = rs.getString("Book_Number");
				BookInfo[1] = rs.getString("Title");
				BookInfo[2] = rs.getString("Auther");
				BookInfo[3] = rs.getString("Publisher");
				BookInfo[4] = rs.getString("Genre");
				BookInfo[5] = rs.getString("Book_Condition");
				BookInfo[6] = rs.getString("Full_Price");
				BookInfo[7] = rs.getString("Sale_Price");
				BookInfo[8] = rs.getString("Lend_Price");
				BookInfo[9] = rs.getString("Rental_Status");
				BookInfo[10] = rs.getString("Introduction");
				returnBookList.add(new Book(BookInfo));

			}
			return returnBookList;

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

	public synchronized static List<Book> searchBook(String info1, String info2) {//정보 두개
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		List<Book> returnBookList=new ArrayList<>();
		String[] BookInfo = new String[9];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			String[] searchInfo1=info1.split("-");
			String[] searchInfo2=info2.split("-");

			String sql;
			sql = "SELECT *FROM book WHERE "+searchInfo1[0]+" like'%" +searchInfo1[1]+ "%' AND "+searchInfo2[0]+" like'%"+searchInfo2[1]+"%'";
			
			rs = state.executeQuery(sql);
			if(rs==null) {
				return null;//존재 X
			}
			while (rs.next()) {//나중에 바꿔
				BookInfo[0] = rs.getString("Book_Number");
				BookInfo[1] = rs.getString("Title");
				BookInfo[2] = rs.getString("Auther");
				BookInfo[3] = rs.getString("Publisher");
				BookInfo[4] = rs.getString("Genre");
				BookInfo[5] = rs.getString("Book_Condition");
				BookInfo[6] = rs.getString("Full_Price");
				BookInfo[7] = rs.getString("Sale_Price");
				BookInfo[8] = rs.getString("Lend_Price");
				BookInfo[9] = rs.getString("Rental_Status");
				BookInfo[10] = rs.getString("Introduction");
				
				returnBookList.add(new Book(BookInfo));

			}
			return returnBookList;

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
	public synchronized static List<Book> searchBook(String info1, String info2, String info3) {//정보 세개
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		List<Book> returnBookList=new ArrayList<>();
		String[] BookInfo = new String[9];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			String[] searchInfo1=info1.split("-");
			String[] searchInfo2=info2.split("-");
			String[] searchInfo3=info3.split("-");
			String sql;
			sql = "SELECT *FROM book WHERE "+searchInfo1[0]+" like '%"+searchInfo1[1]
																		 +"%' AND "+searchInfo2[0]+" like '%"+searchInfo2[1]
																		 +"%' AND "+searchInfo3[0]+" like '%"+searchInfo3[1]+"%'";
			
			rs = state.executeQuery(sql);
			if(rs==null) {
				return null;//존재 X
			}
			while (rs.next()) {//나중에 바꿔
				BookInfo[0] = rs.getString("Book_Number");
				BookInfo[1] = rs.getString("Title");
				BookInfo[2] = rs.getString("Auther");
				BookInfo[3] = rs.getString("Publisher");
				BookInfo[4] = rs.getString("Genre");
				BookInfo[5] = rs.getString("Book_Condition");
				BookInfo[6] = rs.getString("Full_Price");
				BookInfo[7] = rs.getString("Sale_Price");
				BookInfo[8] = rs.getString("Lend_Price");
				BookInfo[9] = rs.getString("Rental_Status");
				BookInfo[10] = rs.getString("Introduction");
				
				returnBookList.add(new Book(BookInfo));

			}
			return returnBookList;

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
	
	public synchronized static List<Book> searchBook(String info1, String info2, String info3,String info4) {//정보 네개
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		List<Book> returnBookList=new ArrayList<>();
		String[] BookInfo = new String[9];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			String[] searchInfo1=info1.split("-");
			String[] searchInfo2=info2.split("-");
			String[] searchInfo3=info3.split("-");
			String[] searchInfo4=info4.split("-");
			String sql;
			sql = "SELECT *FROM book WHERE "+searchInfo1[0]+" like '%"+searchInfo1[1]
																		 +"%' AND "+searchInfo2[0]+" like '%"+searchInfo2[1]
																		 +"%' AND "+searchInfo3[0]+" like '%"+searchInfo3[1]
																		 +"%' AND "+searchInfo4[0]+" like '%"+searchInfo4[1]+"%'";
			
			rs = state.executeQuery(sql);
			if(rs==null) {
				return null;//존재 X
			}
			while (rs.next()) {//나중에 바꿔
				BookInfo[0] = rs.getString("Book_Number");
				BookInfo[1] = rs.getString("Title");
				BookInfo[2] = rs.getString("Auther");
				BookInfo[3] = rs.getString("Publisher");
				BookInfo[4] = rs.getString("Genre");
				BookInfo[5] = rs.getString("Book_Condition");
				BookInfo[6] = rs.getString("Full_Price");
				BookInfo[7] = rs.getString("Sale_Price");
				BookInfo[8] = rs.getString("Lend_Price");
				BookInfo[9] = rs.getString("Rental_Status");
				BookInfo[10] = rs.getString("Introduction");
				returnBookList.add(new Book(BookInfo));

			}
			return returnBookList;

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
	public synchronized static List<Book> searchBook(String info1, String info2, String info3,String info4,String info5) {//정보 5ro
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		List<Book> returnBookList=new ArrayList<>();
		String[] BookInfo = new String[9];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			String[] searchInfo1=info1.split("-");
			String[] searchInfo2=info2.split("-");
			String[] searchInfo3=info3.split("-");
			String[] searchInfo4=info4.split("-");
			String[] searchInfo5=info5.split("-");
			String sql;
			sql = "SELECT *FROM book WHERE "+searchInfo1[0]+" like '"+searchInfo1[1]
																		 +"' AND "+searchInfo2[0]+" like '"+searchInfo2[1]
																		 +"' AND "+searchInfo3[0]+" like '"+searchInfo3[1]
																		 +"' AND "+searchInfo4[0]+" like '"+searchInfo4[1]
																		 +"' AND "+searchInfo5[0]+" like '"+searchInfo5[1]+"'";
			
			rs = state.executeQuery(sql);
			if(rs==null) {
				return null;//존재 X
			}
			while (rs.next()) {//나중에 바꿔
				BookInfo[0] = rs.getString("Book_Number");
				BookInfo[1] = rs.getString("Title");
				BookInfo[2] = rs.getString("Auther");
				BookInfo[3] = rs.getString("Publisher");
				BookInfo[4] = rs.getString("Genre");
				BookInfo[5] = rs.getString("Book_Condition");
				BookInfo[6] = rs.getString("Full_Price");
				BookInfo[7] = rs.getString("Sale_Price");
				BookInfo[8] = rs.getString("Lend_Price");
				BookInfo[9] = rs.getString("Rental_Status");
				BookInfo[10] = rs.getString("Introduction");
				
				returnBookList.add(new Book(BookInfo));

			}
			return returnBookList;

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


	
	
	
}
