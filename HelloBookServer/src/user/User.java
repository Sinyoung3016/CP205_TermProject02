package user;

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
		if(UserInfo[6].equals("0")||UserInfo[6].equals("false")) {//false
			this.Lend_OK=false;
		}
		else {
			this.Lend_OK=true;
		}
		if(UserInfo[7].equals("0")||UserInfo[7].equals("false")) {//false
			this.is_connected=false;
		}
		else {
			this.is_connected=true;
		}
	}

	public User(String UserInfo) {
		String[] info=UserInfo.split("-");
		this.ID=info[1];
		this.password=info[3];
		this.name=info[5];
		this.phone=info[7];
		this.Email=info[9];
		this.Address=info[11];
		this.Lend_OK=Boolean.parseBoolean(info[13]);
		this.is_connected=Boolean.parseBoolean(info[15]);

		
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
		StringBuilder sb=new StringBuilder("회원정보");//회원정보ID:~~:PW:~~
		sb.append("ID-"+this.ID);//token1
		sb.append("-PW-"+this.password);//token3
		sb.append("-이름-"+this.name);
		sb.append("-전화번호-"+this.phone);
		sb.append("-이메일-"+this.Email);
		sb.append("-주소-"+this.getAddress());
		sb.append("-대여가능여부-"+this.Lend_OK);
		sb.append("-접속여부-"+this.is_connected);//15
		return new String(sb);
	}

	
}
