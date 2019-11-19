package authentication;

import book.Book;

public class User {
	private String ID;
	private String password;
	private String name;
	private String phone;
	private String Email; //연체료
	private String Address;
	private boolean Lend_OK;
	private boolean is_connected;

	
	public User(String[] UserInfo) {
		this.ID=UserInfo[0];
		this.password=UserInfo[1];
		this.name=UserInfo[2];
		this.phone=UserInfo[3];
		this.Email=UserInfo[4];
		this.Address=UserInfo[5];
		if(Integer.parseInt(UserInfo[6])==0) {//false
			this.Lend_OK=false;
		}
		else {
			this.Lend_OK=true;
		}
		if(Integer.parseInt(UserInfo[7])==0) {//false
			this.is_connected=false;
		}
		else {
			this.is_connected=true;
		}
	}


	public String getID() {
		return ID;
	}



	public String getPassword() {
		return password;
	}



	public String getName() {
		return name;
	}



	public String getPhone() {
		return phone;
	}



	public String getEmail() {
		return Email;
	}



	public String getAddress() {
		return Address;
	}



	public boolean isLend_OK() {
		return Lend_OK;
	}



	public boolean is_connected() {
		return is_connected;
	}



	public String toString() {
		StringBuilder sb=new StringBuilder("회원정보: [");
		sb.append("{ID: "+this.ID+"}  {PW: "+this.password+"}  {이름: "+this.name+"}  {전화번호: "+this.phone+"}  {이메일: "+this.Email
				+"} {주소: "+this.getAddress()+"} {대여가능여부: "+this.Lend_OK+"} {접속여부: "+this.is_connected+"} ]");
		return new String(sb);
	}

	
}
