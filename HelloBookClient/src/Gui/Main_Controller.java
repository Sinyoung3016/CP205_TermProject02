package Gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import book.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main_Controller extends Base_Controller implements Initializable {

	@FXML
	public Button btn_Left, btn_Right;
	@FXML
	public ImageView im_ad;
	@FXML
	public Label lb_adExplain;
	@FXML
	public ListView lv_NewBooks, lv_BestSeller;
	private ObservableList<Book> ItemList_newBook;


	private Image[] ad = {

	};

	private Image[] user = {

	};

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		
		super.base();
		ItemList_newBook=DataModel.ItemList_newBook;
		lv_NewBooks.setItems(ItemList_newBook);
		// Base end
	
	}
	
	@FXML
	public void goLeftAction() {
		// 왼쪽으로 이동하며 다른 adPicture 보여주기  
	}

	@FXML
	public void goRightAction() {
		// 오른쪽으로 이동하며 다른 adPicture 보여주기
	}


}
