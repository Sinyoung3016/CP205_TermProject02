package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class BookDetail_Controller extends Base_Controller implements Initializable {

	@FXML
	public Button btn_Back, btn_Left, btn_WishList, btn_Borrow, btn_BuyNow, btn_Right;
	@FXML
	public Label lb_MainBookName, lb_MainBookAuthor, lb_RentalStatus, lb_Title, lb_Author, lb_Publisher, lb_BookCondition, lb_FullPrice, lb_SalePrice, lb_LendPrice, lb_Introduction; 

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		super.base();
		// Base end
	}

	@FXML
	public void backAction() { //전 화면으로 

	}

	@FXML
	public void putWishListAction() { //이 책을 wishlist에 넣기

	}

	@FXML
	public void borrowedNowAction() { //이 책을 바로 빌리기

	}

	@FXML
	public void buyNowAction() { //이 책을 바로 구매하기

	}
	@FXML
	public void goLeftAction() { // 왼쪽의 다른 책 보여주기
		
	}
	
	public void goRightAction() { // 오른쪽의 다른 책 보여주기
		
	}

}
