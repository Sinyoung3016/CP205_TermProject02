package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;

public class MyBookList_Controller extends Base_Controller implements Initializable {

	@FXML
	public Button btn_LoanedBook, btn_BorrowedBook, btn_RegisteredBook, btn_SoldBook, btn_Back;
	@FXML
	public ListView MybooklistField;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		super.base();
		// Base end
	}

	public void showborrowedAction() { // 빌린 책 MybooklistField에 보여줘 LoanedBook 전체 보여줘

	}

	public void showloanedAction() { // 빌려준 책 MybooklistField에 보여줘 registeredBook 중 lend 된 책

	}

	public void showregisteredAction() { // 등록한 책 MybooklistField에 보여줘 registeredBook 전체 보여줘

	}

	public void showsoldAction() { // 팔린 책 MybooklistField에 보여줘 registeredBook 중 soldout된 책

	}

	public void backAction() { // 전 화면으로

	}

}
