package Gui;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import book.Book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class BorrowedBook_Controller extends Base_Controller implements Initializable {

	@FXML
	public Button btn_Back, btn_OK;
	@FXML
	public Label lb_from_who, lb_Title, lb_Author, lb_Publisher, lb_BookCondition, lb_FullPrice, lb_SalePrice,
			lb_LendPrice, lb_Introduction;

	private Book book;
	@FXML
	public AnchorPane AnchorPane;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		this.book = DataModel.book_for_detail;

		// Base start
		super.base();
		// Base end

		lb_from_who.setText( DataModel.borrowed_form_who);
		lb_Title.setText(book.getTitle());
		lb_Author.setText(book.getAuther());
		lb_Publisher.setText(book.getPublisher());
		lb_BookCondition.setText(book.getBook_condition());
		lb_FullPrice.setText(book.getFull_price() + "");
		lb_SalePrice.setText(book.getSale_price() + "");
		lb_LendPrice.setText(book.getLend_price() + "");
		lb_Introduction.setText(book.getIntroduction());

	}

	@FXML
	public void backAction() { //전 화면으로 
		AnchorPane root=(AnchorPane) AnchorPane.getScene().getRoot();
		root.getChildren().remove(AnchorPane);
	}



	@FXML
	public void OKAction() { // 전 화면으로

	}
}
