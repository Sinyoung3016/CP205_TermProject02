package Gui.model;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.List;

import book.Book;
import javafx.collections.ObservableList;
import user.User;

public class DataModel {

	//public final static String SERVER_IP="118.42.100.169";
	public final static String SERVER_IP="10.3.166.25";
	public final static int PORT=26432;
	public static Socket socket=null;
	public static String ID;
	public static User user;
	public static List<Book> book_list;
	public static ObservableList<Book> ItemList_newBook;
	public static ObservableList<String> ItemList_myBook;
	public static BufferedReader br;
	
	public static void addNewBook(Book book) {
		ItemList_newBook.add(0,book);
		if(DataModel.ItemList_newBook.size()==21) {
    		DataModel.ItemList_newBook.remove(20);
    	}

	}
	public static void addMyBookList(String book) {
		ItemList_myBook.add(book);

	}
	
	
}
