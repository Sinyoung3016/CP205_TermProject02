package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import user.User;

public class DB_USER extends DBManager {
	
	public synchronized static List<User> getAllUser() {
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		List<User> userList = new ArrayList<>();
		String [] MemberInfo=new String[8];
		try {

			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.

			String sql;
			sql = "SELECT *FROM user";
			
			rs = state.executeQuery(sql);

			
			while (rs.next()) {
				MemberInfo[0] = rs.getString("ID");
				MemberInfo[1] = rs.getString("PassWord");
				MemberInfo[2] = rs.getString("Name");
				MemberInfo[3] = rs.getString("Phone");
				MemberInfo[4] = rs.getString("Email");
				MemberInfo[5] = rs.getString("Address");
				MemberInfo[6] = rs.getString("Lend_OK");
				MemberInfo[7]=rs.getString("is_connected");//연결되면 1, 아니면 0
				userList.add(new User(MemberInfo));//빌린 책 등은 아직 안나오게 해둠, Member에서 생성자 추가로 만들어!

				// RentalBookRegistrationNum
			}
			return userList;

			
		
		} catch (Exception e) {
			System.out.println(e);
			return null;
		} finally {	
			try {
				if (state != null)
					state.close();
			} catch (SQLException sqle) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle1) {
				sqle1.printStackTrace();
			}
			try {if(rs!=null)
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}


	public synchronized static User getUser(String ID) {
	
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		String[] userInfo = new String[8];
		
		try {
			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			
			String sql;
			sql = "SELECT * FROM user WHERE id='" + ID + "'";
			
			rs = state.executeQuery(sql);
			if(rs==null) {
				return null;//존재 X
			}

			if (rs.next()) {
				userInfo[0] = rs.getString("id");
				userInfo[1] = rs.getString("password");
				userInfo[2] = rs.getString("name");
				userInfo[3] = rs.getString("Phone");
				userInfo[4] = rs.getString("Email");
				userInfo[5] = rs.getString("Address");
				userInfo[6] = rs.getString("Lend_OK");
				userInfo[7]=rs.getString("is_connected");//연결되면 1, 아니면 0
				// RentalBookRegistrationNum
			}
			User returnUser=new User(userInfo);
			return returnUser;

			
		
		} catch (Exception e) {
			return null;
		} finally {	
			try {
				if (state != null)
					state.close();
			} catch (SQLException sqle) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle1) {
				sqle1.printStackTrace();
			}
			try {if(rs!=null)
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}



	
	public synchronized static void modifyUser(User changeUser) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {
			conn = getConn();

			String sql;
			sql = "update user set PassWord=?, Name=?, Phone=?, Email=?, Address=? where id=?";
			pstmt = conn.prepareStatement(sql);

			
			pstmt.setString(1, changeUser.getPassword());
			pstmt.setString(2, changeUser.getName());
			pstmt.setString(3, changeUser.getPhone());
			pstmt.setString(4, changeUser.getEmail());
			pstmt.setString(5,  changeUser.getAddress()+ "");
			
			pstmt.setString(6, changeUser.getID());
			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {if (conn != null)conn.close();
				if (pstmt != null)pstmt.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized static boolean getLentalStatus(String ID) {
		
		Connection conn = null;
		Statement state = null;
		ResultSet rs =null;
		
		try {
			conn = getConn();

			state = conn.createStatement();// conn연결정보를 state로 생성.
			
			String sql;
			sql = "SELECT * FROM user WHERE id='" + ID + "'";
			
			rs = state.executeQuery(sql);
			int count=-1;
			if (rs.next()) {
				count = Integer.parseInt(rs.getString("Lend_OK"));

			}
			if(count==1) {
				return true;
			}
			return false;

			
		
		} catch (Exception e) {
			return true;
		} finally {	
			try {
				if (state != null)
					state.close();
			} catch (SQLException sqle) {
			}
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException sqle1) {
				sqle1.printStackTrace();
			}
			try {if(rs!=null)
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
	
	public synchronized static void userLogIn(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String sql;
			sql = "UPDATE user SET is_connected = '1' WHERE id= '"+id+"'";
			pstmt = conn.prepareStatement(sql);

			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {if (conn != null)conn.close();
				if (pstmt != null)pstmt.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized static void userLogOut(String id) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String sql;
			sql = "UPDATE user SET is_connected='0' WHERE id='"+id+"'";
			pstmt = conn.prepareStatement(sql);

			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {if (conn != null)conn.close();
				if (pstmt != null)pstmt.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized static void userBadCredit(String id) {//대여가능여부 flase로
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String sql;
			sql = "UPDATE user SET Lend_OK='0' WHERE id='"+id+"'";
			pstmt = conn.prepareStatement(sql);

			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {if (conn != null)conn.close();
				if (pstmt != null)pstmt.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized static void userGoodCredit(String id) {//대여가능여부 flase로
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String sql;
			sql = "UPDATE user SET Lend_OK='1' WHERE id='"+id+"'";
			pstmt = conn.prepareStatement(sql);

			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {if (conn != null)conn.close();
				if (pstmt != null)pstmt.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	public synchronized static void allUserLogOut() {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String sql;
			sql = "UPDATE user SET is_connected='0'" ;
			pstmt = conn.prepareStatement(sql);

			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {if (conn != null)conn.close();
				if (pstmt != null)pstmt.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public synchronized static void insertUser(String ID, String PassWord, String Name, String Phone ,String Email, String Address, int Lend_OK,int is_connected) throws SQLException {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String sql;
			sql = "INSERT INTO user (ID, PassWord, Name, Phone, Email, Address, Lend_OK, is_connected)VALUES (?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, ID);
			pstmt.setString(2, PassWord);
			pstmt.setString(3, Name);
			pstmt.setString(4, Phone);
			pstmt.setString(5, Email);
			pstmt.setString(6,  Address);
			pstmt.setString(7,  Lend_OK+ "");
			pstmt.setString(8,  is_connected+ "");
			

			pstmt.executeUpdate();
			pstmt.close();
			conn.close();
		}finally {
			try {if (conn != null)conn.close();
				if (pstmt != null)pstmt.close();
			} 
			catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

	public synchronized static void deleateUser(String ID) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		try {

			conn = getConn();

			String sql;
			sql = "DELETE FROM user WHERE ID=?";
			pstmt = conn.prepareStatement(sql);

			pstmt.setString(1, ID);
			pstmt.execute();

			pstmt.close();
			conn.close();

		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {
			try {if (conn != null)conn.close();
			if (pstmt != null)pstmt.close();
		} 
		catch (SQLException e) {
			e.printStackTrace();
		}
	}
	}

}
