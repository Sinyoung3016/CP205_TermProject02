package Gui.model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import book.Book;
import book.Book.HBoxCell;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import user.User;

public class DataModel {

	public final static String SERVER_IP = "10.3.166.25";
	public final static int PORT = 26432;

	public static Socket socket = null;
	public static String ID;
	public static User user;

	public static List<Book> book_list;
	public static boolean exist_new_book = false;
	public static ObservableList<HBoxCell> ItemList_newBook;
	public static ObservableList<HBoxCell> ItemList_myBook;
	public static ObservableList<HBoxCell> ItemList_searchBook;

	public static ArrayList<Image> advertisement_list = new ArrayList<>();

	public static Book book_for_detail;
	public static Book book_for_loaned;
	public static Book book_for_registered;

	// public static BufferedReader br;

	public static void addNewBook(Book book) {
		ItemList_newBook.add(0, book.getBook());
		if (DataModel.ItemList_newBook.size() == 21) {
			DataModel.ItemList_newBook.remove(20);
		}
	}

	public static void addMyBookList(Book book) {
		ItemList_myBook.add(book.getBook());
	}

	public static void removeMyBookList(Book book) {
		ItemList_myBook.remove(book.getBook());
	}

	public static void addSearchBookList(Book book) {
		ItemList_searchBook.add(book.getBook());
	}

	public static void addNewBook(String no_book_message) {
		ItemList_newBook.add(Book.getBook(no_book_message));
	}

	public static void addMyBookList(String no_book_message) {
		ItemList_myBook.add(Book.getBook(no_book_message));// 등록된 책이 없습니다.
	}

	public static void addSearchBookList(String no_book_message) {
		ItemList_searchBook.add(Book.getBook(no_book_message));// 등록된 책이 없습니다.
	}
}
