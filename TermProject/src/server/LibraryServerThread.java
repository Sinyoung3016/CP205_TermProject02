package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import authentication.LogInContext;
import authentication.User;
import book.Book;
import database.DB_BOOK;
import database.DB_USER;
import exception.MyException;

public class LibraryServerThread extends Thread{
	private Map<String, String> client_id_ip;
	List<PrintWriter> listUser =null;
	private Socket client;
	BufferedReader br=null;
	PrintWriter pw=null;
	private String id=null;
	
	LibraryServerThread(Socket client, Map<String, String> client_id_ip, List<PrintWriter> listUser){
		this.client=client;
		this.client_id_ip=client_id_ip;
		this.listUser=listUser;
	}
	
	
	@Override
	public void run() {
		try {
			System.out.println("클라이언트 연결 요청 [Client IP: "+client.getInetAddress()+"]");
			br	= new	BufferedReader(new InputStreamReader(client.getInputStream()));
			pw=new PrintWriter(new OutputStreamWriter(client.getOutputStream()));
			
			while(true) {
				String request=br.readLine();
				if(request==null) {
					break;
				}
				String [] request_tokens=request.split(":");// [ex] LOGIN:ID:IP
				
				if(request_tokens[0].equals(ServerRequest.SIGN_UP.getRequest())) {//SignUp:ID:PW:Name:Phone:Email,Address
					SignUp(request_tokens[1],request_tokens[2],request_tokens[3],request_tokens[4],request_tokens[5],request_tokens[6]);
					
				}else if(request_tokens[0].equals(ServerRequest.LOG_IN.getRequest())) {//LogIn:ID:PW
					LogIn(request_tokens[1], request_tokens[2], pw);
					
				}else if(request_tokens[0].equals(ServerRequest.SIGN_OUT.getRequest())) {//SignOut:ID:PW
					SignOut(request_tokens[1], request_tokens[2],pw);
					
				}else if(request_tokens[0].equals(ServerRequest.LOG_OUT.getRequest())) {//LogOut:ID
					LogOut(request_tokens[1],pw);
					
				}else if(request_tokens[0].equals(ServerRequest.MODIFY_USER_DATA.getRequest())) {//ModifyUserData:ID:PW:NAME:PHONE:EMAIL:ADDRESS  !아이디는 변경 불가!
					ModifyUserData(request_tokens[1],request_tokens[2],request_tokens[3],request_tokens[4],request_tokens[5],request_tokens[6]);
					
				}else if(request_tokens[0].equals(ServerRequest.SEARCH_BOOK.getRequest())) {//SearchBook:제목-~~:작가-~~
					SearchBook(request_tokens, pw);
				}else if(request_tokens[0].equals(ServerRequest.ADD_BOOK_DATA.getRequest())) {//AddBookData:제목:작가:ㅇㅇㅇ:, 이게 되면 메인화면에 새로 들어온 책에 띄울거임, BroadCast를 만들어서 띄어주는 형식으로 만들자!!
					AddBookData(request_tokens);
				}else if(request_tokens[0].equals(ServerRequest.MODIFY_BOOK_DATA.getRequest())) {//ModifyBookData:책제목:작가:ㅐㅐㅐ
					modifyBookData(request_tokens);
				}else if(request_tokens[0].equals(ServerRequest.DELETE_BOOK_DATA.getRequest())) {//DeleteBookData:책번호
					deleteBookData(request_tokens[1]);
				}else if(request_tokens[0].equals(ServerRequest.PURCHASE_BOOK.getRequest())) {//PurchaseBook:살 책 제목:파는사람Id:사는사람ID
					
				}else if(request_tokens[0].equals(ServerRequest.RENTAL_BOOK.getRequest())) {//RentalBook:책제목-~~:판매자~~~:판매가:~~~
					
				}
				
				
				
				
			}
	
		}catch(MyException e) {
			System.out.println(id+"님이 로그아웃 하셨습니다.");
			LogOut(id,pw);
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void SignUp(String id, String password, String name, String phone, String Email, String Address) throws MyException {
		LogInContext.SignUp(name, phone, id, password,Email,Address);//아이디,비밀번호,이름,전화번호,연체료,빌린 책 수, 연결여부(로그인여부[0이면 연결X, 1이면 연결O])
	}
	private synchronized void LogIn(String id, String password, PrintWriter cleint) throws MyException {
		if(LogInContext.LogIn(id,password)) {//로그인 성공하면 정보 저장
			client_id_ip.put(id, client.getInetAddress().toString());
			listUser.add(cleint);
			this.id=id;
			cleint.println(DB_USER.getUser(id).toString());//회원정보를 보낸다, client 는 PrintWriter
		}
	}
	private synchronized void SignOut(String id, String password, PrintWriter client) throws MyException {
		if(LogInContext.SignOut(id, password)) {
			client_id_ip.remove(id);
			listUser.remove(client);
			pw.close();
		}
	}
	private synchronized void LogOut(String id, PrintWriter client){
		LogInContext.LogOut(id);
		client_id_ip.remove(id);
		listUser.remove(client);
		pw.close();
	}
	private synchronized void ModifyUserData(String id, String changePw, String changeName, String changePhone,String changeEmail, String changeAddress) {//로그인 할 상태에서 바꾸는 거기 때문에 정보가 이상해질 일 없음
		User old=DB_USER.getUser(id);
		String []changeData= {id, changePw, changeName, changePhone,changeEmail,changeAddress,old.isLend_OK()+"",old.is_connected()+""};
		User changeMem=new User(changeData);
		DB_USER.modifyUser(changeMem);
	}
	
	private synchronized void SearchBook(String[] info,PrintWriter cleint) {

			List<Book> searchResult=null;
			if(info.length==1) {//검색 정보 없음-모든 책 검색
				searchResult=DB_BOOK.getAllBook();
			}
			else if(info.length==2) {//검색 정보 1개
				searchResult=DB_BOOK.searchBook(info[1]);
			}else if(info.length==3) {//검색 정보 2개

				searchResult=DB_BOOK.searchBook(info[1],info[2]);
				
			}else if(info.length==4) {//검색 정보 3개
				searchResult=DB_BOOK.searchBook(info[1],info[2],info[3]);

			}else if(info.length==5) {//검색 정보 4개

				searchResult=DB_BOOK.searchBook(info[1], info[2], info[3], info[4]);
			}else if(info.length==6) {//검색 정보 5개

				searchResult=DB_BOOK.searchBook(info[1], info[2], info[3], info[4],info[5]);
			}else {//이외
				pw.println("이상한거 입력한 거 같아여");
				return;
			}
			String result="";
			if(searchResult.size()==0) {
				cleint.println(result);
				cleint.flush();
			}else {
				result="";
				Iterator e=searchResult.iterator();
				while(e.hasNext()) {
					result+=e.next()+"\r\n";
					}
				
				cleint.println(result);
				cleint.flush();
		
		}
	}
	private synchronized void AddBookData(String[] bookData) {//로그인 할 상태에서 바꾸는 거기 때문에 정보가 이상해질 일 없음
		
		DB_BOOK.insertBook(Integer.parseInt(bookData[1]),bookData[2],bookData[3],bookData[4],bookData[5],bookData[6],Integer.parseInt(bookData[7]),Integer.parseInt(bookData[8]),Integer.parseInt(bookData[9]),Boolean.parseBoolean(bookData[10]),bookData[11]);
		String[] newBookData=new String[11];
		for(int i=0; i<11; i++) {
			newBookData[i]=bookData[i+1];
		}
		Book newBook=new Book(newBookData);
		broadcast(newBook);
	}
	private void broadcast(Book addBook) {
		synchronized(listUser) {//모든 사람들에게 메세지 출력
			for(PrintWriter writer : listUser) {
				writer.println(addBook);
				writer.flush();
			}
		}
		
	}
	
	private synchronized void modifyBookData(String[] bookData) {
		DB_BOOK.changeBook(Integer.parseInt(bookData[1]),bookData[2],bookData[3],bookData[4],bookData[5],bookData[6],Integer.parseInt(bookData[7]),Integer.parseInt(bookData[8]),Integer.parseInt(bookData[9]),Boolean.parseBoolean(bookData[10]),bookData[11]);
		
	}
	private synchronized void deleteBookData(String Book_Number) {
		DB_BOOK.deleateBook(Integer.parseInt(Book_Number));
	}
	
}
