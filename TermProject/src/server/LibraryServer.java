package server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LibraryServer {
	
	private final static int PORT=8888;
	public static void main(String[] args) {
		Map<String, String> client_id_ip=new HashMap<>();
		List<PrintWriter> listUser= new ArrayList<PrintWriter>();
		
		try {
			
			ServerSocket server=new ServerSocket();
			String hostAddress = InetAddress.getLocalHost().getHostAddress();
			
			server.bind(new InetSocketAddress(hostAddress, PORT));
			System.out.println(server);
			System.out.println("Library Server Start!! "+"[Server IP:"+ server.getInetAddress()+"] [Server Port: "+PORT+"]");
			
			while(true) {
				Socket c_socket=server.accept();
				new LibraryServerThread(c_socket, client_id_ip,listUser);
				
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
