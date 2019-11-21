package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class BookDetail_Controller extends Base_Controller implements Initializable {

	@FXML
	public Button backButton, PutwishlistButton, lendButton, buynowButton;
	@FXML
	public Label booknameField, authorField, rentalstatus, title, author, publishdate, publisher, bookcondition,
			fullprice, saleprice, lendprice, introdution;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		super.base();
	}

	@FXML
	public void backAction() {

	}

	@FXML
	public void putBookonwishlist() {

	}

	@FXML
	public void lendAction() {

	}

	@FXML
	public void buyAction() {

	}

}
