package server;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import alter.UserAlter;
import authentication.LogInContext;
import book.Book;
import database.DB_ALTER;
import database.DB_BOOK;
import database.DB_USER;
import database.DB_UsersBook;
import exception.MyException;
import user.User;

public class LibraryServerThread extends Thread{
	private List<Book> new_book_list;
	private Map<String, String> client_id_ip;
	private Map<String,PrintWriter> listUser =null;
	private Socket client;
	BufferedReader br=null;
	PrintWriter pw=null;
	private String id=null;
	private String method=null;
	
	LibraryServerThread(Socket client, Map<String, String> client_id_ip,  Map<String,PrintWriter> listUser, List<Book> new_book_list){
		this.client=client;
		this.client_id_ip=client_id_ip;
		this.listUser=listUser;
		this.new_book_list=new_book_list;
		
	}
	
	
	@Override
	public void run() {
		try {
	
			br = new BufferedReader(new InputStreamReader(client.getInputStream(),StandardCharsets.UTF_8));
			pw=new PrintWriter(new OutputStreamWriter(client.getOutputStream(), StandardCharsets.UTF_8));

			
			while(true) {
				try {
					String request=br.readLine();
					if(request==null) {
						break;
					}
					String [] request_tokens=request.split(":");// [ex] LOGIN:ID:IP
					if(request_tokens[0].equals(ServerRequest.SIGN_UP.getRequest())) {//SignUp:ID:PW:Name:Phone:Email,Address
						method="SignUp";
						SignUp(request_tokens[1],request_tokens[2],request_tokens[3],request_tokens[4],request_tokens[5],request_tokens[6]);
					
					}else if(request_tokens[0].equals(ServerRequest.LOG_IN.getRequest())) {//LogIn:ID:PW
						method="LogIn";
						LogIn(request_tokens[1], request_tokens[2], pw);
					}else if(request_tokens[0].equals(ServerRequest.SIGN_OUT.getRequest())) {//SignOut:ID:PW
						SignOut(request_tokens[1], request_tokens[2],pw);
					
					}else if(request_tokens[0].equals(ServerRequest.LOG_OUT.getRequest())) {//LogOut:ID
						method="LogOut";
						LogOut(request_tokens[1],pw);
					
					}else if(request_tokens[0].equals(ServerRequest.MODIFY_USER_DATA.getRequest())) {//ModifyUserData:ID:PW:NAME:PHONE:EMAIL:ADDRESS  !아이디는 변경 불가!
						method="ModifyUserData";
						ModifyUserData(request_tokens[1],request_tokens[2],request_tokens[3],request_tokens[4],request_tokens[5],request_tokens[6]);
					
					}else if(request_tokens[0].equals(ServerRequest.SEARCH_BOOK.getRequest())) {//SearchBook:제목-~~:작가-~~
						method="SearchBook";
						SearchBook(request_tokens, pw);
						
					}else if(request_tokens[0].equals(ServerRequest.PRINT_BOOK_LIST.getRequest())) {//PrintBookList:제목-~~:작가-~~
						method="PrintBookList";
						PrintBookList(request_tokens[1],request_tokens[2],pw);
						
					}else if(request_tokens[0].equals(ServerRequest.ADD_BOOK_DATA.getRequest())) {//AddBookData:제목:작가:ㅇㅇㅇ:, 이게 되면 메인화면에 새로 들어온 책에 띄울거임, BroadCast를 만들어서 띄어주는 형식으로 만들자!!
						method="AddBookData";
						AddBookData(request_tokens);
						
					}else if(request_tokens[0].equals(ServerRequest.REMOVE_BOOK_DATA.getRequest())) {//RemoveBookData:책번호
						RemoveBookData(request_tokens[1]);
						
					}	else if(request_tokens[0].equals(ServerRequest.PRINT_BOOK_DATA.getRequest())) {//PrintBookData:책번호
						PrintBookData(request_tokens[1],Integer.parseInt(request_tokens[2]));
						
					}else if(request_tokens[0].equals(ServerRequest.BORROW_REQUEST.getRequest())) {//BorrowRequest:요청자:책번호:책제목
						method="BorrowRequest";
						BorrowRequest(request_tokens[1],request_tokens[2],request_tokens[3]);
						
					}else if(request_tokens[0].equals(ServerRequest.BORROW_ANSWER.getRequest())) {//BorrowAnswer:(수락,거부):요청자:책번호:책제목:요청받는자
						BorrowAnswer(request_tokens[1],request_tokens[2],request_tokens[3],request_tokens[4],request_tokens[5]);
						
					}else if(request_tokens[0].equals(ServerRequest.PURCHASE_REQUEST.getRequest())) {//PurchaseRequest:요청자:책번호:책제목
						method="PurchaseRequest";
						PurchaseRequest(request_tokens[1],request_tokens[2],request_tokens[3]);
						
					}else if(request_tokens[0].equals(ServerRequest.PURCHASE_ANSWER.getRequest())) {//PurchaseAnswer:(수락,거부):요청자:책번호:책제목:요청받는자
						PurchaseAnswer(request_tokens[1],request_tokens[2],request_tokens[3],request_tokens[4],request_tokens[5]);
						
					}else if(request_tokens[0].equals(ServerRequest.ALTER_OK.getRequest())) {//AlterOK:요청자:책번호:(빌리다,빌려주다,안빌려주다,등등)
						AlterOK(request_tokens[1],request_tokens[2],request_tokens[3]);
						
					}else if(request_tokens[0].equals(ServerRequest.RETURN_BOOK.getRequest())) {//ReturnBook:요청자:책번호,책제목
						ReturnBook(request_tokens[1],request_tokens[2],request_tokens[3]);
					}else if(request_tokens[0].equals(ServerRequest.LATE_IN_RETURN.getRequest())) {//LateInReturn:대여불가가 될 ID
						LateInReturn(request_tokens[1]);
					}

				}catch(MyException e) {
					pw.println(method+":"+e.getMessage());
					pw.flush();
				}catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}catch(IOException e) {
		
			if(e.getMessage().equals("Connection reset")) {
				if(id==null) {
					System.out.println(client.getInetAddress().toString()+"에서 연결 종료");
				}else {
					System.out.println(id+"님이 시스템을 종료하셨습니다.");
					LogOut(id,pw);
				}
			}else if(e.getMessage().equals("socket closed")){
				
			}else{
				e.printStackTrace();
				LogOut(id,pw);
			}
		
		}
	}
	






	private synchronized void SignUp(String id, String password, String name, String phone, String Email, String Address) throws MyException {
		if(LogInContext.SignUp(name, phone, id, password,Email,Address)) {//아이디,비밀번호,이름,전화번호,연체료,빌린 책 수, 연결여부(로그인여부[0이면 연결X, 1이면 연결O])
			pw.println("SignUp:성공:");
			pw.flush();
		}
	}
	
	private synchronized void LogIn(String id, String password, PrintWriter pw) throws MyException, SQLException {
		if(LogInContext.LogIn(id,password)) {//로그인 성공하면 정보 저장
			DB_USER.userLogIn(id);
			
			pw.println("LogIn:성공:"+DB_USER.getUser(id).toString());
			pw.flush();
			client_id_ip.put(id, client.getInetAddress().toString());
			listUser.put(id, pw);
			this.id=id;
			
			try {
				sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			UpdateNewBookforStart();
			printNewAlter(id,pw);
			storeUserLog("LogIn");
		}
	}
	private synchronized void SignOut(String id, String password, PrintWriter pw) throws MyException {
		if(LogInContext.SignOut(id, password)) {
			storeUserLog("LogOut");
			client_id_ip.remove(id);
			listUser.remove(id);
			pw.close();
		}
	}
	private synchronized void LogOut(String id, PrintWriter pw){
		LogInContext.LogOut(id);
		if(id==null) {
			System.out.println(client.getInetAddress().toString()+"에서 연결 종료");
		}else {
			System.out.println(id+"님이 로그아웃 되었습니다.");
			storeUserLog("LogOut");
			pw.close();
		}
		client_id_ip.remove(id);
		listUser.remove(id);
		this.id=null;
	}
	private synchronized void ModifyUserData(String id, String changePw, String changeName, String changePhone,String changeEmail, String changeAddress) {//로그인 할 상태에서 바꾸는 거기 때문에 정보가 이상해질 일 없음
		User old=DB_USER.getUser(id);
		String []changeData= {id, changePw, changeName, changePhone,changeEmail,changeAddress,old.isLend_OK()+"",old.is_connected()+""};
		User changeMem=new User(changeData);
		DB_USER.modifyUser(changeMem);
		pw.println("ModifyUserData:회원정보가 변경되었습니다.:"+changeMem.toString());
		pw.flush();
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
			}else if(info.length==7) {//검색 정보 5개

				searchResult=DB_BOOK.searchBook(info[1], info[2], info[3], info[4],info[5],info[6]);
			}
			
			
			if(searchResult==null||searchResult.size()==0) {
				String result="SearchBookList:정보에 부합하는 책이 없습니다.";

				cleint.println(result);
				cleint.flush();
			}else {
				for(int i=0; i<searchResult.size(); i++) {
					String result="SearchBookList:"+searchResult.get(i).getBookInfoTokens();
					cleint.println(result);
					cleint.flush();
				}
				
		
		}
	}
	private synchronized void AddBookData(String[] bookData) throws  SQLException {
		int bookNum=DB_BOOK.getBookCount();
		DB_BOOK.insertBook(bookData[1],bookData[2],bookData[3],bookData[4],bookData[5],Integer.parseInt(bookData[6]),Integer.parseInt(bookData[7]),Integer.parseInt(bookData[8]),Boolean.parseBoolean(bookData[9]),bookData[10]);
		DB_UsersBook.uploadBook(id,bookData[1]);//등록번호,아이디,제목
		bookNum++;
		pw.println("AddBookData:성공");
		pw.flush();
		String[] newBookData=new String[11];
		newBookData[0]=bookNum+"";
		for(int i=1; i<11; i++) {
			newBookData[i]=bookData[i];
		}
		Book newBook=new Book(newBookData);
		new_book_list.add(0,newBook);

		if(new_book_list.size()==21) {
			new_book_list.remove(20);
			
		}
		broadcastNewBook(newBook);
	}
	
	private void broadcastNewBook(Book addBook) {
		synchronized(listUser) {//모든 사람들에게 메세지 출력
			for(String writer : listUser.keySet()) {
				PrintWriter pw=	listUser.get(writer);
				pw.println("NewBook:"+addBook.getBookInfoTokens());
				pw.flush();
			}
		}
	}
	private void broadcastRemoveNewBook(int book_num) {
		synchronized(listUser) {//모든 사람들에게 메세지 출력
			for(String writer : listUser.keySet()) {
				PrintWriter pw=	listUser.get(writer);
				pw.println("RemoveNewBook:"+book_num);
				pw.flush();
			}
		}
	}
	private synchronized void removeNewBook(int Book_Number) {//지우기
		for(int i=0; i<new_book_list.size(); i++) {
			if(new_book_list.get(i).getBook_num()==Book_Number) {
				new_book_list.remove(i);
				break;
			}
		}
	}

	private synchronized void RemoveBookData(String Book_Number) {
		if(DB_UsersBook.checkRemovePossibility(Integer.parseInt(Book_Number))) {//지울 수 있는 상황이면
			DB_UsersBook.deleateBook(Integer.parseInt(Book_Number));
			DB_BOOK.deleateBook(Integer.parseInt(Book_Number));
			pw.println("RemoveBookData:책을 성공적으로 삭제하였습니다.");
			pw.flush();
			removeNewBook(Integer.parseInt(Book_Number));
			broadcastRemoveNewBook(Integer.parseInt(Book_Number));
		}else {
			pw.println("RemoveBookData:현재 지울 수 없는 상태입니다. 처리하지 않은 요청이 있는지 확인해 주십시오.");
			pw.flush();
		}
	}
	
	private synchronized void storeUserLog(String status) {
		SimpleDateFormat format_day = new SimpleDateFormat ( "yyyy-MM-dd");			
		SimpleDateFormat format_clock = new SimpleDateFormat ( "HH:mm:ss");		
		Calendar time = Calendar.getInstance();	       
		String day = format_day.format(time.getTime());
		String clock = format_clock.format(time.getTime());
		PrintWriter pw=null;
		try {
			if(status.equals("LogIn")) {
				pw=new PrintWriter(new FileWriter("src/server/userlog/"+day+"UserLogIn.txt",true));
				StringBuffer stb= new StringBuffer(day+", "+clock);
				stb.append("  [User ID: "+this.id+"]  [User IP: "+this.client_id_ip.get(id)+"], LogIn");
				pw.println(stb);
				pw.close();
			}else if(status.equals("LogOut")){
				pw=new PrintWriter(new FileWriter("src/server/userlog/"+day+"UserLogOut.txt",true));
				StringBuffer stb= new StringBuffer(day+", "+clock);
				stb.append("  [User ID: "+this.id+"]  [User IP: "+this.client_id_ip.get(id)+"], LogOut");
				pw.println(stb);
				pw.close();
			}

		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void UpdateNewBookforStart() {
		if(new_book_list.size()==0) {
			pw.println("NewBook:"+"새로운 책이 없습니다.");
			pw.flush();
		}
		for(int i=new_book_list.size()-1; i>=0; i--) {
			pw.println("NewBook:"+new_book_list.get(i).getBookInfoTokens());
			pw.flush();
			
		}
	}
	
	private synchronized void PrintBookList(String id, String status, PrintWriter pw) throws SQLException {
		List<Book> book_list=new ArrayList<>();
		List<Integer> book_num_list=null;
		if(status.equals("Registered")) {
			book_num_list=DB_UsersBook.getRegisteredBookNumber(id);
			
			for(int i=0; i<book_num_list.size();i++) {
				book_list.add(DB_BOOK.searchBookByNum(book_num_list.get(i)));
				
			}
			if(book_list.size()==0) {
				pw.println("PrintBookList:등록한 책이 없습니다.");
				pw.flush();
			}
			for(int i=book_list.size()-1; i>=0; i--) {
				pw.println("PrintBookList:"+book_list.get(i).getBookInfoTokens());
				pw.flush();
			}
		}else if(status.equals("Borrowed")) {
			book_num_list=DB_UsersBook.getBorrowedBookNumber(id);
			
			for(int i=0; i<book_num_list.size();i++) {
				book_list.add(DB_BOOK.searchBookByNum(book_num_list.get(i)));
				
			}
			if(book_list.size()==0) {
				pw.println("PrintBookList:빌린 책이 없습니다.");
				pw.flush();
			}
	
			for(int i=book_list.size()-1; i>=0; i--) {
				pw.println("PrintBookList:"+book_list.get(i).getBookInfoTokens());
				pw.flush();
			}
		}else if(status.equals("Loaned")) {
			book_num_list=DB_UsersBook.getLoanedBookNumber(id);
			
			for(int i=0; i<book_num_list.size();i++) {
				book_list.add(DB_BOOK.searchBookByNum(book_num_list.get(i)));
				
			}
			if(book_list.size()==0) {
				pw.println("PrintBookList:빌려준 책이 없습니다.");
				pw.flush();
			}
	
			for(int i=book_list.size()-1; i>=0; i--) {
				pw.println("PrintBookList:"+book_list.get(i).getBookInfoTokens());
				pw.flush();
			}
		}
	}
	
	private synchronized void PrintBookData(String Status,int book_num) throws SQLException {//Detail, Registered, Loaned

		Book returnBook=DB_BOOK.searchBookByNum(book_num);
		if(Status.equals("Detail")) {
			if(returnBook==null) {
				pw.println("PrintBookData:Detail:책이 존재하지 않습니다.");
				pw.flush();
			}else {
				pw.println("PrintBookData:Detail:"+returnBook.getBookInfoTokens());
				pw.flush();

			}
		}else if(Status.equals("Registered")) {
			if(returnBook==null) {
				pw.println("PrintBookData:Registered:책이 존재하지 않습니다.");
				pw.flush();
			}else {
				String borrowBookId="";
				if((borrowBookId=DB_UsersBook.getBorrowedBookUser(book_num))==null) {
					pw.println("PrintBookData:Registered:"+returnBook.getBookInfoTokens());//25사이즈
					pw.flush();
				}else {
					pw.println("PrintBookData:Registered:"+returnBook.getBookInfoTokens()+":"+DB_UsersBook.getBorrowedBookUser(book_num));//26사이즈
					pw.flush();
				}
			}
		}
		else if(Status.equals("Loaned")) {
			if(returnBook==null) {
				pw.println("PrintBookData:Loaned:책이 존재하지 않습니다.");
				pw.flush();
			}else {
				pw.println("PrintBookData:Loaned:"+returnBook.getBookInfoTokens()+":"+DB_UsersBook.getBorrowedBookUser(book_num));
				pw.flush();

			}
		}
		
	}
	private synchronized void printNewAlter(String id,PrintWriter pw) throws SQLException{
		List<UserAlter> alter_list=DB_ALTER.getAlter(id);
		for(int i=0; i<alter_list.size(); i++) {
			pw.println("Alter:"+alter_list.get(i).getToken());
			pw.flush();
		}
	}
	
	private synchronized void BorrowRequest(String Requester_ID,String Book_Number, String Book_Title) throws SQLException, MyException {
		if(DB_UsersBook.searchRegisterByNum(Integer.parseInt(Book_Number)).equals(this.id)) {
			throw new MyException("당신의 책입니다.");
		}
		if(DB_UsersBook.CheckRequest(Integer.parseInt(Book_Number))!=null) {
			throw new MyException("이미 누군가가 대여/구매 신청을 하였습니다.");
		
		}
		
		String Requested_ID=DB_UsersBook.searchRegisterByNum(Integer.parseInt(Book_Number));
		
		DB_UsersBook.BorrowRequest(Integer.parseInt(Book_Number), Requester_ID);
		DB_ALTER.BorrowRequest(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID);
		this.pw.println("BorrowRequest:대여요청을 보냈습니다.");
		this.pw.flush();
		if(listUser.containsKey(Requested_ID)) {//요청받는 사람, 즉 책의 주인이 현재 접속중이면
			PrintWriter pw=listUser.get(Requested_ID);
			pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "빌리다", "0").getToken());
			pw.flush();
		}
		
	}
	
	private synchronized void BorrowAnswer(String answer,String Requester_ID,String Book_Number, String Book_Title,String Requested_ID) throws SQLException {
		if(answer.equals("수락")) {
			DB_ALTER.AlterOK(Requester_ID, Integer.parseInt(Book_Number),"빌리다");//빌리겠다는 요청을 보낸 사람의 alter을 처리한걸로 바꿈
					
			DB_UsersBook.BorrowBook(Integer.parseInt(Book_Number));
			DB_BOOK.BorrowBook(Integer.parseInt(Book_Number));
			DB_ALTER.RequestAnswer(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID, "빌려주다");//빌렸다는메세지
			
			if(listUser.containsKey(Requested_ID)) {//요청받는 사람, 즉 책의 주인이 현재 접속중이면
				
				PrintWriter pw=listUser.get(Requested_ID);
				pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "빌려주다", "0").getToken());
				pw.flush();
			}
		}else {//거절
			DB_ALTER.AlterOK(Requester_ID, Integer.parseInt(Book_Number),"빌리다");//빌리겠다는 요청을 보낸 사람의 alter을 처리한걸로 바꿈
			
			DB_UsersBook.NoBorrowSell(Integer.parseInt(Book_Number));
			DB_ALTER.RequestAnswer(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID, "안빌려주다");//안빌려줬다는 메세지
			
			if(listUser.containsKey(Requested_ID)) {//요청받는 사람, 즉 책의 주인이 현재 접속중이면
				
				PrintWriter pw=listUser.get(Requested_ID);
				pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "안빌려주다", "0").getToken());
				pw.flush();
			}
		}
	}
	
	private synchronized void AlterOK(String Requester_ID, String Book_Number, String Request_Status) throws  SQLException {
		DB_ALTER.AlterOK(Requester_ID, Integer.parseInt(Book_Number),Request_Status);
	}
	
	
	private synchronized void ReturnBook(String Requester_ID,String Book_Number, String Book_Title) throws SQLException {
		String Requested_ID=DB_UsersBook.searchRequesterByNum(Integer.parseInt(Book_Number));
		
		DB_UsersBook.ReturnBook(Integer.parseInt(Book_Number));
		DB_BOOK.ReturnBook(Integer.parseInt(Book_Number));
		DB_ALTER.ReturnBook(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID);
					
		pw.println("ReturnBook:반납처리가 완료되었습니다.");
		pw.flush();
		
		

		if(listUser.containsKey(Requested_ID)) {//요청받는 사람, 즉 책의 주인이 현재 접속중이면
			PrintWriter pw=listUser.get(Requested_ID);
			pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "반납하다", "0").getToken());
			pw.flush();
		}
		
		DB_USER.userGoodCredit(Requested_ID);
		if(listUser.containsKey(Requested_ID)) {//요청받는 사람, 즉 책의 주인이 현재 접속중이면
			PrintWriter pw=listUser.get(Requested_ID);
			pw.println("LateInReturn:Good");
			pw.flush();
		}
	
	}
	
	
	private synchronized void PurchaseRequest(String Requester_ID,String Book_Number, String Book_Title) throws SQLException, MyException {
		if(DB_UsersBook.searchRegisterByNum(Integer.parseInt(Book_Number)).equals(this.id)) {
			throw new MyException("당신의 책입니다.");
		}
		if(DB_UsersBook.CheckRequest(Integer.parseInt(Book_Number))!=null) {
			throw new MyException("이미 누군가가 대여/구매 신청을 하였습니다.");
		
		}
		String Requested_ID=DB_UsersBook.searchRegisterByNum(Integer.parseInt(Book_Number));
		
		DB_UsersBook.PurchaseRequest(Integer.parseInt(Book_Number), Requester_ID);
		DB_ALTER.PurchaseRequest(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID);
		this.pw.println("PurchaseRequest:구매요청을 보냈습니다.");
		this.pw.flush();
		if(listUser.containsKey(Requested_ID)) {//요청받는 사람, 즉 책의 주인이 현재 접속중이면
			PrintWriter pw=listUser.get(Requested_ID);
			pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "사다", "0").getToken());
			pw.flush();
		}
		
	}
	
	private synchronized void PurchaseAnswer(String answer,String Requester_ID,String Book_Number, String Book_Title,String Requested_ID) throws SQLException {
		if(answer.equals("수락")) {
			DB_ALTER.AlterOK(Requester_ID, Integer.parseInt(Book_Number),"사다");//빌리겠다는 요청을 보낸 사람의 alter을 처리한걸로 바꿈
					
			DB_UsersBook.deleateBook(Integer.parseInt(Book_Number));
			DB_BOOK.deleateBook(Integer.parseInt(Book_Number));
			DB_ALTER.RequestAnswer(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID, "팔다");//빌렸다는메세지
			
			removeNewBook(Integer.parseInt(Book_Number));
			broadcastRemoveNewBook(Integer.parseInt(Book_Number));
			
			
			if(listUser.containsKey(Requested_ID)) {//요청받는 사람, 즉 책의 주인이 현재 접속중이면
				PrintWriter pw=listUser.get(Requested_ID);
				pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "팔다", "0").getToken());
				pw.flush();
			}
		}else {//거절
			DB_ALTER.AlterOK(Requester_ID, Integer.parseInt(Book_Number),"사다");//빌리겠다는 요청을 보낸 사람의 alter을 처리한걸로 바꿈
			
			DB_UsersBook.NoBorrowSell(Integer.parseInt(Book_Number));
			DB_ALTER.RequestAnswer(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID, "안팔다");//안빌려줬다는 메세지
			
			if(listUser.containsKey(Requested_ID)) {//요청받는 사람, 즉 책의 주인이 현재 접속중이면
				
				PrintWriter pw=listUser.get(Requested_ID);
				pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "안팔다", "0").getToken());
				pw.flush();
			}
		}
	}
	
	private void LateInReturn( String id) {
		DB_USER.userBadCredit(id);
		if(listUser.containsKey(id)) {//요청받는 사람, 즉 책의 주인이 현재 접속중이면
			
			PrintWriter pw=listUser.get(id);
			pw.println("LateInReturn:Bad");
			pw.flush();
		}
	}
}
