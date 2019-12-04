package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import book.Book;

public class DB_BOOK  extends DBManager{
	
	
	public synchronized static int getBookCount() {
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		try {
			conn = getConn();
			state = conn.createStatement();// conn연결정보를 state로 생성.
			String sql;
			sql = "select * from book order by book_number desc limit 1";	
			
			rs = state.executeQuery(sql);
			if(rs.next());{
				return rs.getInt("book_number");				
			}
		} catch (Exception e) {
		
			PreparedStatement pstmt = null;
			
			
			try {
				String sql = "alter table book auto_increment=?";	
				pstmt = conn.prepareStatement(sql);
	        	pstmt.setInt(1, 1);
	        	pstmt.executeUpdate();
	        	sql = "alter table usersbook auto_increment=?";	
				pstmt = conn.prepareStatement(sql);
	        	pstmt.setInt(1, 1);
	        	pstmt.executeUpdate();
				System.out.println("등록된 책이 없어 등록번호를 1로 초기화시킵니다.");
				return 0;
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			return 0;
		}  finally {	
			try {
				if (state != null)	state.close();
				if (conn != null)	conn.close();
				if(rs!=null)	rs.close();
			} catch (SQLException e) {}
		}
	}
	public synchronized static void ReturnBook(int BookNum) throws SQLException {//빌리기
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			   String sql = "update book set Rental_Status=? where book_number = ?";

		        try {
		        	pstmt = conn.prepareStatement(sql);

		        	pstmt.setString(1, "1");
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
	
	
	public synchronized static void BorrowBook(int BookNum) throws SQLException {//빌리기
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String sql = "update book set Rental_Status = ? where book_number = ?";
	
	        try {
	         	pstmt = conn.prepareStatement(sql);

	        	pstmt.setString(1,"0"); 
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
	public synchronized static void insertBook(String Title,String Auther, String Publisher, String Genre, String Book_Condition,int Full_Price, int Sale_Price, int Lend_Price, Boolean Rental_Status, String Introduction) throws SQLException {//삽입 
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String insertRentalAvailability="1";
			
			String sql;
			sql = "INSERT INTO book ( Title, Auther, Publisher, Genre, Book_Condition,Full_Price,Sale_Price,Lend_Price,Rental_Status,Introduction)VALUES (?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);


			pstmt.setString(1, Title);
			pstmt.setString(2, Auther);
			pstmt.setString(3, Publisher);
			pstmt.setString(4, Genre );
			pstmt.setString(5, Book_Condition );
			pstmt.setString(6, Full_Price+"");
			pstmt.setString(7, Sale_Price+"");
			pstmt.setString(8, Lend_Price+"");
			if(Rental_Status.equals("true")) {
				insertRentalAvailability="1";
			}else if(Rental_Status.equals("false")){
				insertRentalAvailability="0";
			}
			pstmt.setString(9,  insertRentalAvailability);
			pstmt.setString(10, Introduction );
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
	

	public synchronized static Book searchBookByNum(int Book_Number) {//등록번호로 찾기-관리자가 사용할 메소드

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

		String[] BookInfo = new String[11];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			
			String[] searchInfo=info.split("-");
			String searchStr;
			if(searchInfo[0].equals("Rental_Status")) {//대여가능여부는 like%%로 검색 X..

				searchStr=searchInfo[0]+" like '" +searchInfo[1]+ "'";
			}else {
				searchStr=searchInfo[0]+" like '%" +searchInfo[1]+ "%'";
			}

			String sql;
			sql = "SELECT *FROM book WHERE "+searchStr;
			
			rs = state.executeQuery(sql);

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
		String[] BookInfo = new String[11];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			
			ArrayList<String[]> searchInfoArr=new ArrayList<String[]>();
			
			searchInfoArr.add(info1.split("-"));
			searchInfoArr.add(info2.split("-"));
			String[] searchStr=new String[2];
			for(int i=0; i<2; i++) {
				if(searchInfoArr.get(i)[0].equals("Rental_Status")) {//대여가능여부는 like%%로 검색 X..
					searchStr[i]=searchInfoArr.get(i)[0]+" like '" +searchInfoArr.get(i)[1]+ "'";
				}else {
					searchStr[i]=searchInfoArr.get(i)[0]+" like '%" +searchInfoArr.get(i)[1]+ "%'";
				}
			}

			String sql;
			sql = "SELECT *FROM book WHERE "+searchStr[0]+" AND "+searchStr[1];

			
			rs = state.executeQuery(sql);
	
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
		String[] BookInfo = new String[11];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.

			ArrayList<String[]> searchInfoArr=new ArrayList<String[]>();
			
			searchInfoArr.add(info1.split("-"));
			searchInfoArr.add(info2.split("-"));
			searchInfoArr.add(info3.split("-"));
			String[] searchStr=new String[3];
			for(int i=0; i<3; i++) {
				if(searchInfoArr.get(i)[0].equals("Rental_Status")) {//대여가능여부는 like%%로 검색 X..
					searchStr[i]=searchInfoArr.get(i)[0]+" like '" +searchInfoArr.get(i)[1]+ "'";
				}else {
					searchStr[i]=searchInfoArr.get(i)[0]+" like '%" +searchInfoArr.get(i)[1]+ "%'";
				}
			}

			String sql;
			
			sql = "SELECT *FROM book WHERE "+searchStr[0]+" AND "+searchStr[1]+" AND "+searchStr[2];

			rs = state.executeQuery(sql);

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
		String[] BookInfo = new String[11];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			
			ArrayList<String[]> searchInfoArr=new ArrayList<String[]>();
			
			searchInfoArr.add(info1.split("-"));
			searchInfoArr.add(info2.split("-"));
			searchInfoArr.add(info3.split("-"));
			searchInfoArr.add(info4.split("-"));
			String[] searchStr=new String[4];
			for(int i=0; i<4; i++) {
				if(searchInfoArr.get(i)[0].equals("Rental_Status")) {//대여가능여부는 like%%로 검색 X..
					searchStr[i]=searchInfoArr.get(i)[0]+" like '" +searchInfoArr.get(i)[1]+ "'";
				}else {
					searchStr[i]=searchInfoArr.get(i)[0]+" like '%" +searchInfoArr.get(i)[1]+ "%'";
				}
			}

			String sql;
			
			sql = "SELECT *FROM book WHERE "+searchStr[0]+" AND "+searchStr[1]+" AND "+searchStr[2]+" AND "+searchStr[3];
			rs = state.executeQuery(sql);
	
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
		String[] BookInfo = new String[11];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			
			ArrayList<String[]> searchInfoArr=new ArrayList<String[]>();
			
			searchInfoArr.add(info1.split("-"));
			searchInfoArr.add(info2.split("-"));
			searchInfoArr.add(info3.split("-"));
			searchInfoArr.add(info4.split("-"));
			searchInfoArr.add(info5.split("-"));
			String[] searchStr=new String[5];
			for(int i=0; i<5; i++) {
				if(searchInfoArr.get(i)[0].equals("Rental_Status")) {//대여가능여부는 like%%로 검색 X..
					searchStr[i]=searchInfoArr.get(i)[0]+" like '" +searchInfoArr.get(i)[1]+ "'";
				}else {
					searchStr[i]=searchInfoArr.get(i)[0]+" like '%" +searchInfoArr.get(i)[1]+ "%'";
				}
			}

			String sql;
			
			sql = "SELECT *FROM book WHERE "+searchStr[0]+" AND "+searchStr[1]+" AND "+searchStr[2]+" AND "+searchStr[3]+" AND "+searchStr[4];
			
			rs = state.executeQuery(sql);
	
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
	
	public synchronized static List<Book> searchBook(String info1, String info2, String info3,String info4,String info5, String info6) {//정보 5ro
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		List<Book> returnBookList=new ArrayList<>();
		String[] BookInfo = new String[11];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			
			ArrayList<String[]> searchInfoArr=new ArrayList<String[]>();
			
			searchInfoArr.add(info1.split("-"));
			searchInfoArr.add(info2.split("-"));
			searchInfoArr.add(info3.split("-"));
			searchInfoArr.add(info4.split("-"));
			searchInfoArr.add(info5.split("-"));
			searchInfoArr.add(info6.split("-"));
			String[] searchStr=new String[5];
			for(int i=0; i<5; i++) {
				if(searchInfoArr.get(i)[0].equals("Rental_Status")) {//대여가능여부는 like%%로 검색 X..
					searchStr[i]=searchInfoArr.get(i)[0]+" like '" +searchInfoArr.get(i)[1]+ "'";
				}else {
					searchStr[i]=searchInfoArr.get(i)[0]+" like '%" +searchInfoArr.get(i)[1]+ "%'";
				}
			}

			String sql;
			
			sql = "SELECT *FROM book WHERE "+searchStr[0]+" AND "+searchStr[1]+" AND "+searchStr[2]+" AND "+searchStr[3]+" AND "+searchStr[4]+" AND "+searchStr[5];
			
			rs = state.executeQuery(sql);
		
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
