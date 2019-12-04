package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import book.Book;

public class LibraryServer {
	
	private final static int PORT=26432;
	public static void main(String[] args) {
		Map<String, String> client_id_ip=new HashMap<>();
		Map<String,PrintWriter> listUser= new HashMap<String,PrintWriter>();
		List<Book> new_book_list=new LinkedList<Book>();
		
		try {
			
			ServerSocket server=new ServerSocket();
			//String hostAddress = "0.0.0.0";
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			
			server.bind(new InetSocketAddress(hostAddress, PORT));
			System.out.println(server);
			System.out.println("Library Server Start!! "+"[Server IP:"+ server.getInetAddress()+"] [Server Port: "+PORT+"]");
			
			while(true) {
				Socket c_socket=server.accept();
				
				
			
				System.out.println(c_socket.getInetAddress().toString()+"에서 연결 요청");
				new LibraryServerThread(c_socket, client_id_ip,listUser,new_book_list).start();
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
 

	
	
}
