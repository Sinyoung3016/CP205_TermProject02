package Gui;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import book.Book;
import book.Book.HBoxCell;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class MyBookList_Controller extends Base_Controller implements Initializable {

	@FXML
	public Button btn_LoanedBook, btn_BorrowedBook, btn_RegisteredBook, btn_SoldBook, btn_Back;
	@FXML
	public ListView lv_MybooklistField;
	private Socket socket;
	
	private ObservableList<String> ItemList_myBook;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		DataModel.ItemList_myBook.clear();
		// Base start
		super.base();
		// Base end
		ItemList_myBook=DataModel.ItemList_myBook;
		lv_MybooklistField.setItems(ItemList_myBook);
		
	
	}

	public void showborrowedAction() { // 빌린 책 MybooklistField에 보여줘 LoanedBook 전체 보여줘
		
		DataModel.ItemList_myBook.clear();
	}

	public void showloanedAction() { // 빌려준 책 MybooklistField에 보여줘 registeredBook 중 lend 된 책
		DataModel.ItemList_myBook.clear();
	}

	public void showregisteredAction() { // 등록한 책 MybooklistField에 보여줘 registeredBook 전체 보여줘
		DataModel.ItemList_myBook.clear();
		socket=DataModel.socket;
		PrintWriter pw;
		try {
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			pw.println("PrintBookList:"+DataModel.ID+":Registered");
			pw.flush();

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}

	public void showsoldAction() { // 팔린 책 MybooklistField에 보여줘 registeredBook 중 soldout된 책
		DataModel.ItemList_myBook.clear();
	}

	public void backAction() { // 전 화면으로

	}

}
