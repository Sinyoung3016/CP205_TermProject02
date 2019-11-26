package Gui.model;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import book.Book;
import book.Book.HBoxCell;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import user.User;

public class DataModel {

	//public final static String SERVER_IP="118.42.100.169";
	//public final static String SERVER_IP="10.3.166.25";
	public final static String SERVER_IP="118.42.101.104";
	public final static int PORT=26432;
	public static Socket socket=null;
	public static String ID;
	public static User user;
	public static List<Book> book_list;
	public static ObservableList<HBoxCell> ItemList_newBook;
	public static ObservableList<String> ItemList_myBook;
	public static BufferedReader br;
	public static ArrayList<Image> advertisement_list=new ArrayList<>();
	
	public static void addNewBook(Book book) {
		ItemList_newBook.add(0,book.getBook());
		if(DataModel.ItemList_newBook.size()==21) {
    		DataModel.ItemList_newBook.remove(20);
    	}

	}
	public static void addMyBookList(String book) {
		ItemList_myBook.add(book);
	}

}
