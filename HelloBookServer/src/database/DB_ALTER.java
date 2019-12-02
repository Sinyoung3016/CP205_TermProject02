package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import alter.UserAlter;

public class DB_ALTER extends DBManager{
	
	public synchronized static void BorrowBook(String Requester_ID,int Book_Number, String Book_Title,String Requested_ID) throws SQLException {//ºô¸®±â
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			
			String sql;
			sql = "INSERT INTO useralter (Requester_ID, Book_Number, Book_Title, Requested_ID, Request_Status, is_Treatmented)VALUES (?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, Requester_ID);
			pstmt.setString(2, Book_Number+"");
			pstmt.setString(3, Book_Title);
			pstmt.setString(4, Requested_ID);
			pstmt.setString(5, "ºô¸®´Ù" );
			pstmt.setString(6, "0" );

	
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} finally {	
			try {
				if (pstmt != null)	pstmt.close();
				if (conn != null)	conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
				}
			}
		}
		
		public synchronized static void BorrowAnswer(String Requester_ID,int Book_Number, String Book_Title,String Requested_ID,String Request_Status)  {//ºô¸®±â
			Connection conn = null;
			PreparedStatement pstmt = null;

			try {

				conn = getConn();

				
				String sql;
				sql = "INSERT INTO useralter (Requester_ID, Book_Number, Book_Title, Requested_ID, Request_Status, is_Treatmented)VALUES (?,?,?,?,?,?)";
				pstmt = conn.prepareStatement(sql);

				pstmt.setString(1, Requester_ID);
				pstmt.setString(2, Book_Number+"");
				pstmt.setString(3, Book_Title);
				pstmt.setString(4, Requested_ID);
				pstmt.setString(5, Request_Status );
				pstmt.setString(6, "0" );

		
				pstmt.executeUpdate();
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {	
				try {
					if (pstmt != null)	pstmt.close();
					if (conn != null)	conn.close();
				} catch (SQLException e) {}
			}
		}
		
		public synchronized static void AlterOK(String Requester_ID,int Book_Number)  {//ºô¸®±â
			Connection conn = null;
			PreparedStatement pstmt = null;

			try {

				conn = getConn();

				
				String sql ="update useralter set is_Treatmented=? where Book_Number=? and Requested_ID=?";
		        
				pstmt = conn.prepareStatement(sql);

	        	pstmt.setString(1,"1"); 
	        	pstmt.setString(2, Book_Number+""); 
	        	pstmt.setString(3, Requester_ID);
	        	
	        	pstmt.executeUpdate();
		
		
				pstmt.close();
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {	
				try {
					if (pstmt != null)	pstmt.close();
					if (conn != null)	conn.close();
				} catch (SQLException e) {}
			}
		}
		public synchronized static List <UserAlter> getAlter(String Requested_ID){
			Connection conn = null;
			Statement state = null;
			ResultSet rs =null;
			List <UserAlter> alter_list=new ArrayList<>();
			
			String[] AlterInfo = new String[6];
			try {
				conn = getConn();
				state = conn.createStatement();// conn¿¬°áÁ¤º¸¸¦ state·Î »ý¼º.
				String sql;
				sql = "SELECT * FROM useralter where Requested_ID ='"+Requested_ID+"' and is_Treatmented=0";	
				
				rs = state.executeQuery(sql);
				while (rs.next()) {
					AlterInfo[0] = rs.getString("Requester_ID");
					AlterInfo[1] = rs.getString("Book_Number");
					AlterInfo[2] = rs.getString("Book_Title");
					AlterInfo[3] = rs.getString("Requested_ID");
					AlterInfo[4] = rs.getString("Request_Status");
					AlterInfo[5] = rs.getString("is_Treatmented");

					alter_list.add(new UserAlter(AlterInfo[0],AlterInfo[1],AlterInfo[2],AlterInfo[3],AlterInfo[4],AlterInfo[5]));
				}
				return alter_list;			
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
