package Gui.model;

import java.net.Socket;
import java.util.ArrayList;
import alter.UserAlter;
import book.Book;
import book.Book.HBoxCell;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.text.Text;
import user.User;

public class DataModel {

	public final static String SERVER_IP = "172.30.1.47";
	public final static int PORT = 26432;

	public static Socket socket = null;
	public static String ID=null;
	public static User user;

	public static boolean is_thread_on=false;
	public static boolean is_exist_new_book = false;
	public static ObservableList<HBoxCell> ItemList_newBook;
	public static ObservableList<HBoxCell> ItemList_myBook;
	public static ObservableList<HBoxCell> ItemList_searchBook;
	public static ObservableList<Text> chatList;
	
	public static ObservableList<UserAlter> ItemList_alter;

	public static ArrayList<Image> advertisement_list = new ArrayList<>();

	public static Book detail_book;
	
	public static String who_borrwed_book;//���� ����������
	public static String borrowed_form_who;//���� ���������

	// public static BufferedReader br;
	public static void addChat(String msg, ListView chatView) {
	     Text text = new Text(msg);
	        // ä�ú��� �ʺ� �°� �ڵ����� ������ �ٹٲ����ִ� ���ε��� �����Ѵ�.
	        text.wrappingWidthProperty().bind(new DoubleBinding() {
				@Override
				protected double computeValue() {
					return chatView.getPrefWidth();
				}
			});
	        chatList.add(text);
	        chatView.scrollTo(chatList.size());
	        
	}
	
	public static void serverNotice(String msg, ListView chatView) {
	     Text text = new Text(msg);
	        // ä�ú��� �ʺ� �°� �ڵ����� ������ �ٹٲ����ִ� ���ε��� �����Ѵ�.
	        text.wrappingWidthProperty().bind(new DoubleBinding() {
				@Override
				protected double computeValue() {
					return chatView.getPrefWidth();
				}
			});
	        chatList.add(text);      
	        chatView.scrollTo(chatList.size());
	        
	}
	
	public static void addAlter(UserAlter user_alter) {
		ItemList_alter.add(user_alter);
	}
	
	public static void addNewBook(Book book) {
		ItemList_newBook.add(0, book.getBook());
		if (DataModel.ItemList_newBook.size() == 21) {
			DataModel.ItemList_newBook.remove(20);
		}
	}
	public static void removeNewBook(String BookNum) {

		if(!ItemList_newBook.get(0).getBookNum().equals("���ο� å�� �����ϴ�.")) {//���ο� å�� �����ϴٰ� �ƴҶ�
			for(int i=0; i<ItemList_newBook.size(); i++) {
				if(ItemList_newBook.get(i).getBookNum().equals(BookNum)) {
					ItemList_newBook.remove(i);
					if(ItemList_newBook.size()==0) {
						is_exist_new_book=false;
					}
					break;
				}
			}
		}
		if(ItemList_newBook.size()==0) {
			NoNewBook("���ο� å�� �����ϴ�.");
			
		}
	}

	public static void addMyBookList(Book book) {
		ItemList_myBook.add(book.getBook());
	}

	public static void addSearchBookList(Book book) {
		ItemList_searchBook.add(book.getBook());
	}
	
	public static void removeMyBookList(Book book) {
		ItemList_myBook.remove(book.getBook());
	}
	

	
	public static void NoNewBook(String no_book_message) {
		ItemList_newBook.add(Book.getBook(no_book_message));
	}

	public static void NoMyBookList(String no_book_message) {
		ItemList_myBook.add(Book.getBook(no_book_message));// ��ϵ� å�� �����ϴ�.
	}

	public static void NoSearchBookList(String no_book_message) {
		ItemList_searchBook.add(Book.getBook(no_book_message));// ��ϵ� å�� �����ϴ�.
	}
	public synchronized Book getBookDetail() {
		while(detail_book==null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return detail_book;
	}
	public synchronized void setBookDetail(Book book) {
		detail_book=book;
		notifyAll();
	}
}
