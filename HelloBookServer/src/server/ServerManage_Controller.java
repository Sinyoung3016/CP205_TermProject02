package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;

import alter.UserAlter;
import authentication.LogInContext;
import book.Book;
import database.DB_BOOK;
import database.DB_USER;
import database.DB_ALTER;
import database.DB_UsersBook;
import exception.MyException;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import user.User;

public class ServerManage_Controller implements Initializable {
	@FXML
	public TextField tf_notice ,tf_ThreadCount; 
	@FXML//						��������							���� �ݱ�		ȭ�� �ݱ�
	public Button btn_Notice, btn_ServerOpen, btn_ServerClose, btn_Close,btn_ThreadCount;
	@FXML
	public Label lb_error,lb_Main;
	@FXML
	public Label lb_visiter;
	@FXML
	public Label lb_server_status;
	int count=0;
	
	ServerSocket server;
	Map<String, String> client_id_ip;
	Map<String,PrintWriter> listUser;
	List<Book> new_book_list;
	List<Socket> client_socket_list;
	
	@FXML
	public void noticeAction() { //����
		if(tf_notice.getText().length()!=0) {
		Iterator<Socket> e=client_socket_list.iterator();
		
		while(e.hasNext()) {
			Socket socket=e.next();
			PrintWriter pw;
			try {
				if(!socket.isClosed()) {
				pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));
				pw.println("Notice:"+tf_notice.getText());
				pw.flush();
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
	
		}
		new Alert(Alert.AlertType.INFORMATION, "������ ���½��ϴ�.", ButtonType.OK).show();
		tf_notice.setText("");
		
		}else {
			new Alert(Alert.AlertType.INFORMATION, "���ڸ� �Է����ּ���", ButtonType.OK).show();
		}
	}


	@FXML
	public void serverOpenAction() {// ���� ����
		client_id_ip=new HashMap<>();
		listUser= new HashMap<String,PrintWriter>();
		new_book_list=new Vector<>();
		client_socket_list=new Vector<>();
		try {
			server=new ServerSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
		new  ClientAcceptThread().start();
		btn_ServerOpen.setDisable(true);
		btn_ServerClose.setDisable(false);
		lb_server_status.setText(">Server On");
		lb_server_status.setStyle("-fx-text-fill:Red"); 

	}

	@FXML
	public void serverCloseAction() { // ���� �ݱ�
		btn_ServerOpen.setDisable(false);
		btn_ServerClose.setDisable(true);
		lb_server_status.setText(">Server Off");
		lb_server_status.setStyle("-fx-text-fill:#3065AC"); 
		try {
			Iterator<Socket> e=client_socket_list.iterator();
			
			while(e.hasNext()) {
				Socket socket=e.next();
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(),StandardCharsets.UTF_8));
				pw.println("ShutDown:");
				pw.flush();
				socket.close();
				e.remove();
			}
			
            if (server != null && !server.isClosed()) {
            	server.close();
            	
            }
        
            
        } catch (Exception e) {
            e.printStackTrace();
        }
		
	}

	@FXML
	public void closeAction() {// ȭ�� ����
		 if (server != null && !server.isClosed()) {
         	try {
				server.close();
				System.out.println("���� ����!");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         	}
		Stage stage = (Stage) btn_Close.getScene().getWindow();
	    stage.close();
	}
	@FXML
	public void ThreaCount() { //�ٽ� ���� ȭ�� �ε�	
		tf_ThreadCount.setText(java.lang.Thread.activeCount()+"");

	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {	
		lb_server_status.setText("Server Off");
	}
	
	
	
	
	
	class ClientAcceptThread extends Thread{
		private final static int PORT=26432;
		@Override
		public void run() {
			try {
				System.out.println("���� ������ ����");
				//String hostAddress = "0.0.0.0";
				String hostAddress = InetAddress.getLocalHost().getHostAddress();
				
				server.bind(new InetSocketAddress(hostAddress, PORT));
				System.out.println(server);
				System.out.println("Library Server Start!! "+"[Server IP:"+ server.getInetAddress()+"] [Server Port: "+PORT+"]");
				while(true) {
					Socket c_socket=server.accept();
					client_socket_list.add(c_socket);
					

					new LibraryServerThread(c_socket).start();
					
				}
				
			} catch (IOException e) {
				if(e.getMessage().equals("socket closed")) {
				}else {
				System.out.println(e.getMessage());
				}
			}finally {

			}
		}
		
	}
	
	class LibraryServerThread extends Thread{
		private Socket client;
		BufferedReader br=null;
		PrintWriter pw=null;
		private String id="";
		private String method=null;
		private boolean is_logout=true;
		
		LibraryServerThread(Socket client){
			this.client=client;
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
							client.close();
							break;
						}
						String [] request_tokens=request.split(":");// [ex] LOGIN:ID:IP
				
						if(request_tokens[0].equals(ServerRequest.SIGN_UP.getRequest())) {//SignUp:ID:PW:Name:Phone:Email,Address
							method="SignUp";
							SignUp(request_tokens[1],request_tokens[2],request_tokens[3],request_tokens[4],request_tokens[5],request_tokens[6]);
						
						}else if(request_tokens[0].equals(ServerRequest.LOG_IN.getRequest())) {//LogIn:ID:PW
							method="LogIn";
							LogIn(request_tokens[1], request_tokens[2], pw);
							Platform.runLater(()->{lb_visiter.setText(++count+"");});
						}else if(request_tokens[0].equals(ServerRequest.LOG_OUT.getRequest())) {//LogOut:ID
							method="LogOut";
							LogOut(request_tokens,pw);
							
						}else if(request_tokens[0].equals(ServerRequest.MODIFY_USER_DATA.getRequest())) {//ModifyUserData:ID:PW:NAME:PHONE:EMAIL:ADDRESS  !���̵�� ���� �Ұ�!
							method="ModifyUserData";
							ModifyUserData(request_tokens[1],request_tokens[2],request_tokens[3],request_tokens[4],request_tokens[5],request_tokens[6]);
						
						}else if(request_tokens[0].equals(ServerRequest.SEARCH_BOOK.getRequest())) {//SearchBook:����-~~:�۰�-~~
							method="SearchBook";
							SearchBook(request_tokens, pw);
							
						}else if(request_tokens[0].equals(ServerRequest.PRINT_BOOK_LIST.getRequest())) {//PrintBookList:����-~~:�۰�-~~
							method="PrintBookList";
							PrintBookList(request_tokens[1],request_tokens[2],pw);
							
						}else if(request_tokens[0].equals(ServerRequest.ADD_BOOK_DATA.getRequest())) {//AddBookData:����:�۰�:������:, �̰� �Ǹ� ����ȭ�鿡 ���� ���� å�� ������, BroadCast�� ���� ����ִ� �������� ������!!
							method="AddBookData";
							AddBookData(request_tokens);
							
						}else if(request_tokens[0].equals(ServerRequest.REMOVE_BOOK_DATA.getRequest())) {//RemoveBookData:å��ȣ
							RemoveBookData(request_tokens[1]);
							
						}	else if(request_tokens[0].equals(ServerRequest.PRINT_BOOK_DATA.getRequest())) {//PrintBookData:å��ȣ
							PrintBookData(request_tokens[1],Integer.parseInt(request_tokens[2]));
							
						}else if(request_tokens[0].equals(ServerRequest.BORROW_REQUEST.getRequest())) {//BorrowRequest:��û��:å��ȣ:å����
							method="BorrowRequest";
							BorrowRequest(request_tokens[1],request_tokens[2],request_tokens[3]);
							
						}else if(request_tokens[0].equals(ServerRequest.BORROW_ANSWER.getRequest())) {//BorrowAnswer:(����,�ź�):��û��:å��ȣ:å����:��û�޴���
							BorrowAnswer(request_tokens[1],request_tokens[2],request_tokens[3],request_tokens[4],request_tokens[5]);
							
						}else if(request_tokens[0].equals(ServerRequest.PURCHASE_REQUEST.getRequest())) {//PurchaseRequest:��û��:å��ȣ:å����
							method="PurchaseRequest";
							PurchaseRequest(request_tokens[1],request_tokens[2],request_tokens[3]);
							
						}else if(request_tokens[0].equals(ServerRequest.PURCHASE_ANSWER.getRequest())) {//PurchaseAnswer:(����,�ź�):��û��:å��ȣ:å����:��û�޴���
							PurchaseAnswer(request_tokens[1],request_tokens[2],request_tokens[3],request_tokens[4],request_tokens[5]);
							
						}else if(request_tokens[0].equals(ServerRequest.ALERT_OK.getRequest())) {//AlterOK:��û��:å��ȣ:(������,�����ִ�,�Ⱥ����ִ�,���)
							AlertOK(request_tokens[1],request_tokens[2],request_tokens[3]);
							
						}else if(request_tokens[0].equals(ServerRequest.RETURN_BOOK.getRequest())) {//ReturnBook:��û��:å��ȣ,å����
							ReturnBook(request_tokens[1],request_tokens[2],request_tokens[3]);
						}else if(request_tokens[0].equals(ServerRequest.LATE_IN_RETURN.getRequest())) {//LateInReturn:�뿩�Ұ��� �� ID
							LateInReturn(request_tokens[1]);
						}else if(request_tokens[0].equals(ServerRequest.CHAT.getRequest())) {//Chat:���̵�:�޼���
							Chat(request_tokens);
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
				}else if(e.getMessage().equals("socket closed")) {
				}
				else{
					System.out.println(e.getMessage());
				}
				LogOut(id,pw);
			}finally {
				
			}
		}
		






		private void Chat(String[] request_tokens) {
			String message="";
			String id=request_tokens[1];
			for(int i=2; i<request_tokens.length; i++) {
				if(i==request_tokens.length-1) {
					message+=request_tokens[i];
				}else {
				message+=request_tokens[i]+":";
				}
			}

				synchronized(listUser) {//��� ����鿡�� �޼��� ���
					for(String writer : listUser.keySet()) {
						PrintWriter pw=	listUser.get(writer);
						pw.println("Chat:"+id+":"+message);
						pw.flush();
						
				}
			}
			
		}


		private void SignUp(String id, String password, String name, String phone, String Email, String Address) throws MyException, SQLException {
			if(LogInContext.SignUp(name, phone, id, password,Email,Address)) {//���̵�,��й�ȣ,�̸�,��ȭ��ȣ,��ü��,���� å ��, ���Ῡ��(�α��ο���[0�̸� ����X, 1�̸� ����O])
				pw.println("SignUp:����:");
				pw.flush();
			}
		}
		
		private void LogIn(String id, String password, PrintWriter pw) throws MyException, SQLException {
			if(LogInContext.LogIn(id,password)) {//�α��� �����ϸ� ���� ����
				DB_USER.userLogIn(id);
				
				pw.println("LogIn:����:"+DB_USER.getUser(id).toString());
				pw.flush();
				client_id_ip.put(id, client.getInetAddress().toString());
				listUser.put(id, pw);
				this.id=id;
				is_logout=false;
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.out.println(id+"���� �α��� �ϼ̽��ϴ�.");
				UpdateNewBookforStart();
				printNewAlter(id,pw);
				storeUserLog("LogIn");
			}
		}

		private void LogOut(String id, PrintWriter pw){
			if(!is_logout) {
				
			LogInContext.LogOut(id);
			
			System.out.println(id+"���� �α׾ƿ� �Ǿ����ϴ�.");
			storeUserLog("LogOut");
			pw.close();
			
			
			client_socket_list.remove(this.client);
			client_id_ip.remove(id);
			listUser.remove(id);
			this.id=null;
			is_logout=true;
			Platform.runLater(()->{lb_visiter.setText(--count+"");});
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			}
		}
		private void LogOut(String[] id, PrintWriter pw){
			if(!is_logout) {
			if(id.length==2) {
				LogInContext.LogOut(id[1]);
				System.out.println(id[1]+"���� �α׾ƿ� �Ǿ����ϴ�.");
				storeUserLog("LogOut");
			}else {//null �� ��
				System.out.println(client.getInetAddress().toString()+"���� ���� ����");
			}
		
			pw.close();
			client_socket_list.remove(this.client);
			client_id_ip.remove(id);
			listUser.remove(id);
			this.id=null;
			is_logout=true;
			Platform.runLater(()->{lb_visiter.setText(--count+"");});
			try {
				client.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			}
		}
		private void ModifyUserData(String id, String changePw, String changeName, String changePhone,String changeEmail, String changeAddress) {//�α��� �� ���¿��� �ٲٴ� �ű� ������ ������ �̻����� �� ����
			User old=DB_USER.getUser(id);
			String []changeData= {id, changePw, changeName, changePhone,changeEmail,changeAddress,old.isLend_OK()+"",old.is_connected()+""};
			User changeMem=new User(changeData);
			DB_USER.modifyUser(changeMem);
			pw.println("ModifyUserData:ȸ�������� ����Ǿ����ϴ�.:"+changeMem.toString());
			pw.flush();
		}
		
		private void SearchBook(String[] info,PrintWriter cleint) {
				
				List<Book> searchResult=null;
				if(info.length==1) {//�˻� ���� ����-��� å �˻�
					searchResult=DB_BOOK.getAllBook();
				}else if(info.length==2) {//�˻� ���� 1��
					searchResult=DB_BOOK.searchBook(info[1]);
				}else if(info.length==3) {//�˻� ���� 2��
					searchResult=DB_BOOK.searchBook(info[1],info[2]);
				}else if(info.length==4) {//�˻� ���� 3��
					searchResult=DB_BOOK.searchBook(info[1],info[2],info[3]);
				}else if(info.length==5) {//�˻� ���� 4��
					searchResult=DB_BOOK.searchBook(info[1], info[2], info[3], info[4]);
				}else if(info.length==6) {//�˻� ���� 5��
					searchResult=DB_BOOK.searchBook(info[1], info[2], info[3], info[4],info[5]);
				}else if(info.length==7) {//�˻� ���� 5��
					searchResult=DB_BOOK.searchBook(info[1], info[2], info[3], info[4],info[5],info[6]);
				}
				
				if(searchResult==null||searchResult.size()==0) {
					String result="SearchBookList:������ �����ϴ� å�� �����ϴ�.";

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
		private void AddBookData(String[] bookData) throws  SQLException {
			
			DB_BOOK.insertBook(bookData[1],bookData[2],bookData[3],bookData[4],bookData[5],Integer.parseInt(bookData[6]),Integer.parseInt(bookData[7]),Integer.parseInt(bookData[8]),Boolean.parseBoolean(bookData[9]),bookData[10]);
			DB_UsersBook.uploadBook(id,bookData[1]);//��Ϲ�ȣ,���̵�,����
			int bookNum=DB_BOOK.getBookCount();
			pw.println("AddBookData:����");
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
			synchronized(listUser) {//��� ����鿡�� �޼��� ���
				for(String writer : listUser.keySet()) {
					PrintWriter pw=	listUser.get(writer);
					pw.println("NewBook:"+addBook.getBookInfoTokens());
					pw.flush();
				}
			}
		}
		private void broadcastRemoveNewBook(int book_num) {
			synchronized(listUser) {//��� ����鿡�� �޼��� ���
				for(String writer : listUser.keySet()) {
					PrintWriter pw=	listUser.get(writer);
					pw.println("RemoveNewBook:"+book_num);
					pw.flush();
				}
			}
		}
		private void removeNewBook(int Book_Number) {//�����
			for(int i=0; i<new_book_list.size(); i++) {
				if(new_book_list.get(i).getBook_num()==Book_Number) {
					new_book_list.remove(i);
					break;
				}
			}
		}

		private void RemoveBookData(String Book_Number) {
			if(DB_UsersBook.checkRemovePossibility(Integer.parseInt(Book_Number))) {//���� �� �ִ� ��Ȳ�̸�
				DB_UsersBook.deleateBook(Integer.parseInt(Book_Number));
				DB_BOOK.deleateBook(Integer.parseInt(Book_Number));
				pw.println("RemoveBookData:å�� ���������� �����Ͽ����ϴ�.");
				pw.flush();
				removeNewBook(Integer.parseInt(Book_Number));
				broadcastRemoveNewBook(Integer.parseInt(Book_Number));
			}else {
				pw.println("RemoveBookData:���� ���� �� ���� �����Դϴ�. ó������ ���� ��û�� �ִ��� Ȯ���� �ֽʽÿ�.");
				pw.flush();
			}
		}
		
		private void storeUserLog(String status) {
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
					stb.append("  [User ID: "+this.id+"]  [User IP: "+client_id_ip.get(id)+"], LogIn");
					pw.println(stb);
					pw.close();
				}else if(status.equals("LogOut")){
					pw=new PrintWriter(new FileWriter("src/server/userlog/"+day+"UserLogOut.txt",true));
					StringBuffer stb= new StringBuffer(day+", "+clock);
					stb.append("  [User ID: "+this.id+"]  [User IP: "+client_id_ip.get(id)+"], LogOut");
					pw.println(stb);
					pw.close();
				}

			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		private void UpdateNewBookforStart() {
			if(new_book_list.size()==0) {
				pw.println("NewBook:"+"���ο� å�� �����ϴ�.");
				pw.flush();
			}
			for(int i=new_book_list.size()-1; i>=0; i--) {
				pw.println("NewBook:"+new_book_list.get(i).getBookInfoTokens());
				pw.flush();
				
			}
		}
		
		private void PrintBookList(String id, String status, PrintWriter pw) throws SQLException, MyException {
			List<Book> book_list=new ArrayList<>();
			List<Integer> book_num_list=null;
			if(status.equals("Registered")) {
				book_num_list=DB_UsersBook.getRegisteredBookNumber(id);
				
				for(int i=0; i<book_num_list.size();i++) {
					book_list.add(DB_BOOK.searchBookByNum(book_num_list.get(i)));
					
				}
				if(book_list.size()==0) {
					pw.println("PrintBookList:����� å�� �����ϴ�.");
					pw.flush();
				}else {
					for(int i=book_list.size()-1; i>=0; i--) {
					pw.println("PrintBookList:"+book_list.get(i).getBookInfoTokens());
					pw.flush();
					}
				}
			}else if(status.equals("Borrowed")) {
				book_num_list=DB_UsersBook.getBorrowedBookNumber(id);
				
				for(int i=0; i<book_num_list.size();i++) {
					book_list.add(DB_BOOK.searchBookByNum(book_num_list.get(i)));
					
				}
				if(book_list.size()==0) {
					pw.println("PrintBookList:���� å�� �����ϴ�.");
					pw.flush();
				}else {
		
				for(int i=book_list.size()-1; i>=0; i--) {
					pw.println("PrintBookList:"+book_list.get(i).getBookInfoTokens());
					pw.flush();
					}
				}
			}else if(status.equals("Loaned")) {
				book_num_list=DB_UsersBook.getLoanedBookNumber(id);
				
				for(int i=0; i<book_num_list.size();i++) {
					book_list.add(DB_BOOK.searchBookByNum(book_num_list.get(i)));
					
				}
				if(book_list.size()==0) {
					pw.println("PrintBookList:������ å�� �����ϴ�.");
					pw.flush();
				}else {
		
				for(int i=book_list.size()-1; i>=0; i--) {
					pw.println("PrintBookList:"+book_list.get(i).getBookInfoTokens());
					pw.flush();
					}
				}
			}
		}
		
		private void PrintBookData(String Status,int book_num) throws SQLException {//Detail, Registered, Loaned

			Book returnBook=DB_BOOK.searchBookByNum(book_num);
			if(Status.equals("Detail")) {
				if(returnBook==null) {
					pw.println("PrintBookData:Detail:å�� �������� �ʽ��ϴ�.");
					pw.flush();
				}else {
					String loanBookId="";
					if((loanBookId=DB_UsersBook.searchRegisterByNum(book_num))==null) {	
						pw.println("PrintBookData:Detail:"+returnBook.getBookInfoTokens());
						pw.flush();
					}else {
						pw.println("PrintBookData:Detail:"+returnBook.getBookInfoTokens()+":"+loanBookId);//26������
						pw.flush();
					}
				}
			}else if(Status.equals("Registered")) {
				if(returnBook==null) {
					pw.println("PrintBookData:Registered:å�� �������� �ʽ��ϴ�.");
					pw.flush();
				}else {
					String borrowBookId="";
					if((borrowBookId=DB_UsersBook.getBorrowedBookUser(book_num))==null) {
						pw.println("PrintBookData:Registered:"+returnBook.getBookInfoTokens());//25������
						pw.flush();
					}else {
						pw.println("PrintBookData:Registered:"+returnBook.getBookInfoTokens()+":"+borrowBookId);//26������
						pw.flush();
					}
				}
			}
			else if(Status.equals("Loaned")) {
				if(returnBook==null) {
					pw.println("PrintBookData:Loaned:å�� �������� �ʽ��ϴ�.");
					pw.flush();
				}else {
					pw.println("PrintBookData:Loaned:"+returnBook.getBookInfoTokens()+":"+DB_UsersBook.getBorrowedBookUser(book_num));
					pw.flush();

				}
			}
			
		}
		private void printNewAlter(String id,PrintWriter pw) throws SQLException{
			List<UserAlter> alter_list=DB_ALTER.getAlter(id);
			for(int i=0; i<alter_list.size(); i++) {
				pw.println("Alter:"+alter_list.get(i).getToken());
				pw.flush();
			}
		}
		
		private void BorrowRequest(String Requester_ID,String Book_Number, String Book_Title) throws SQLException, MyException {
			String Register_id=DB_UsersBook.searchRegisterByNum(Integer.parseInt(Book_Number));
			if(Register_id==null) {
				throw new MyException("�̹� ���ŵǾ��ų� ���ŵ� å�Դϴ�.");
			}
			else if(DB_UsersBook.searchRegisterByNum(Integer.parseInt(Book_Number)).equals(this.id)) {
				throw new MyException("����� å�Դϴ�.");
			}
			if(DB_UsersBook.CheckRequest(Integer.parseInt(Book_Number))!=null) {
				throw new MyException("�̹� �������� �뿩/���� ��û�� �Ͽ����ϴ�.");
			
			}
			String Requested_ID=DB_UsersBook.searchRegisterByNum(Integer.parseInt(Book_Number));
			
			DB_UsersBook.BorrowRequest(Integer.parseInt(Book_Number), Requester_ID);
			DB_ALTER.BorrowRequest(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID);
			this.pw.println("BorrowRequest:�뿩��û�� ���½��ϴ�.");
			this.pw.flush();
			if(listUser.containsKey(Requested_ID)) {//��û�޴� ���, �� å�� ������ ���� �������̸�
				PrintWriter pw=listUser.get(Requested_ID);
				pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "������", "0").getToken());
				pw.flush();
			}
			
		}
		
		private void BorrowAnswer(String answer,String Requester_ID,String Book_Number, String Book_Title,String Requested_ID) throws SQLException {
			if(answer.equals("����")) {
				DB_ALTER.AlterOK(Requester_ID, Integer.parseInt(Book_Number),"������");//�����ڴٴ� ��û�� ���� ����� alter�� ó���Ѱɷ� �ٲ�
						
				DB_UsersBook.BorrowBook(Integer.parseInt(Book_Number));
				DB_BOOK.BorrowBook(Integer.parseInt(Book_Number));
				DB_ALTER.RequestAnswer(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID, "�����ִ�");//���ȴٴ¸޼���
				
				if(listUser.containsKey(Requested_ID)) {//��û�޴� ���, �� å�� ������ ���� �������̸�
					
					PrintWriter pw=listUser.get(Requested_ID);
					pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "�����ִ�", "0").getToken());
					pw.flush();
				}
			}else {//����
				DB_ALTER.AlterOK(Requester_ID, Integer.parseInt(Book_Number),"������");//�����ڴٴ� ��û�� ���� ����� alter�� ó���Ѱɷ� �ٲ�
				
				DB_UsersBook.NoBorrowSell(Integer.parseInt(Book_Number));
				DB_ALTER.RequestAnswer(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID, "�Ⱥ����ִ�");//�Ⱥ�����ٴ� �޼���
				
				if(listUser.containsKey(Requested_ID)) {//��û�޴� ���, �� å�� ������ ���� �������̸�
					
					PrintWriter pw=listUser.get(Requested_ID);
					pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "�Ⱥ����ִ�", "0").getToken());
					pw.flush();
				}
			}
		}
		
		private void AlertOK(String Requester_ID, String Book_Number, String Request_Status) throws  SQLException {
			DB_ALTER.AlterOK(Requester_ID, Integer.parseInt(Book_Number),Request_Status);
		}
		
		
		private void ReturnBook(String Requester_ID,String Book_Number, String Book_Title) throws SQLException {//Requester_ID�� å�� ������ ��� �� ����

			String Requested_ID=DB_UsersBook.searchRequesterByNum(Integer.parseInt(Book_Number));		

			DB_UsersBook.ReturnBook(Integer.parseInt(Book_Number));
			DB_BOOK.ReturnBook(Integer.parseInt(Book_Number));
			DB_ALTER.ReturnBook(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID);
		
			pw.println("ReturnBook:�ݳ�ó���� �Ϸ�Ǿ����ϴ�.");
			pw.flush();
			
			

			if(listUser.containsKey(Requested_ID)) {//��û�޴� ���, �� å�� ���� �����
				PrintWriter pw=listUser.get(Requested_ID);
				pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "�ݳ��ϴ�", "0").getToken());
				pw.flush();
			}
		
			if(!DB_USER.getLentalStatus(Requested_ID)) {//false���� ��
			DB_USER.userGoodCredit(Requested_ID);
			if(listUser.containsKey(Requested_ID)) {//��û�޴� ���, �� å�� ���� �����
				PrintWriter pw=listUser.get(Requested_ID);
				pw.println("LateInReturn:Good");
				pw.flush();
			}
			}
			
		
		}
		
		
		private void PurchaseRequest(String Requester_ID,String Book_Number, String Book_Title) throws SQLException, MyException {
			if(DB_UsersBook.searchRegisterByNum(Integer.parseInt(Book_Number)).equals(this.id)) {
				throw new MyException("����� å�Դϴ�.");
			}
			if(DB_UsersBook.CheckRequest(Integer.parseInt(Book_Number))!=null) {
				throw new MyException("�̹� �������� �뿩/���� ��û�� �Ͽ����ϴ�.");
			
			}
			String Requested_ID=DB_UsersBook.searchRegisterByNum(Integer.parseInt(Book_Number));
			
			DB_UsersBook.PurchaseRequest(Integer.parseInt(Book_Number), Requester_ID);
			DB_ALTER.PurchaseRequest(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID);
			this.pw.println("PurchaseRequest:���ſ�û�� ���½��ϴ�.");
			this.pw.flush();
	
			if(listUser.containsKey(Requested_ID)) {//��û�޴� ���, �� å�� ������ ���� �������̸�
				PrintWriter pw=listUser.get(Requested_ID);
				pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "���", "0").getToken());
				pw.flush();
			}
			
		}
		
		private void PurchaseAnswer(String answer,String Requester_ID,String Book_Number, String Book_Title,String Requested_ID) throws SQLException {
			if(answer.equals("����")) {
				DB_ALTER.AlterOK(Requester_ID, Integer.parseInt(Book_Number),"���");//�����ڴٴ� ��û�� ���� ����� alter�� ó���Ѱɷ� �ٲ�
						
				DB_UsersBook.deleateBook(Integer.parseInt(Book_Number));
				DB_BOOK.deleateBook(Integer.parseInt(Book_Number));
				DB_ALTER.RequestAnswer(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID, "�ȴ�");//���ȴٴ¸޼���
				
				removeNewBook(Integer.parseInt(Book_Number));
				broadcastRemoveNewBook(Integer.parseInt(Book_Number));
				
				
				if(listUser.containsKey(Requested_ID)) {//��û�޴� ���, �� å�� ������ ���� �������̸�
					PrintWriter pw=listUser.get(Requested_ID);
					pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "�ȴ�", "0").getToken());
					pw.flush();
				}
			}else {//����
				DB_ALTER.AlterOK(Requester_ID, Integer.parseInt(Book_Number),"���");//�����ڴٴ� ��û�� ���� ����� alter�� ó���Ѱɷ� �ٲ�
				
				DB_UsersBook.NoBorrowSell(Integer.parseInt(Book_Number));
				DB_ALTER.RequestAnswer(Requester_ID, Integer.parseInt(Book_Number), Book_Title, Requested_ID, "���ȴ�");//�Ⱥ�����ٴ� �޼���
				
				if(listUser.containsKey(Requested_ID)) {//��û�޴� ���, �� å�� ������ ���� �������̸�
					
					PrintWriter pw=listUser.get(Requested_ID);
					pw.println("Alter:"+new UserAlter(Requester_ID, Book_Number+"", Book_Title, Requested_ID, "���ȴ�", "0").getToken());
					pw.flush();
				}
			}
		}
		
		private void LateInReturn( String id) {
			DB_USER.userBadCredit(id);
			if(listUser.containsKey(id)) {//��û�޴� ���, �� å�� ������ ���� �������̸�
				
				PrintWriter pw=listUser.get(id);
				pw.println("LateInReturn:Bad");
				pw.flush();
			}
		}
	}
	
	

}
