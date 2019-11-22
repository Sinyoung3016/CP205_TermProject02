package exception;

public class MyException extends Exception{
	MyException(){
		super();
	}
	public MyException(String message){
		super(message);
	}
	
	public String changeKorean() {
		String message=super.getMessage();
		switch (message){
		case "ID does not match format":
			return "아이디의 형식이 올바르지 않습니다.";
		case "Password does not match format":
			return "비밀번호의 형식이 올바르지 않습니다.";
		case "Name does not match format":
			return "이름의 형식이 올바르지 않습니다.";
		case "Phone does not match format":
			return "전화번호의 형식이 올바르지 않습니다.";
		case "Already Exist ID":
			return "이미 존재하는 아이디입니다.";
		case "ID does not exist":
			return "아이디가 존재하지 않습니다.";
		case "Passwords do not match":
			return "비밀번호가 일치하지 않습니다.";
			
		case "Fill in the all fields":
			return "빈 칸이 있습니다.";
		case "You are already logged in":
			return "이미 접속한 아이디입니다.";
		case "Server Closed":
			return "서버가 닫혀있씁니다.";

		}
		return message;
	}

}
