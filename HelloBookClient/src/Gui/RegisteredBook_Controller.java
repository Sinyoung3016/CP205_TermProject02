package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import book.Book;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

public class RegisteredBook_Controller extends Base_Controller implements Initializable {

	public Button btn_Back, btn_Left, btn_Right, btn_Remove, btn_Comfirm;
	@FXML
	public RadioButton Rbtn_canLend;
	@FXML
	public RadioButton Rbtn_soldOut;
	@FXML
	public RadioButton Rbtn_lend;	
	@FXML
	public TextField tf_who;	
	
	public Label lb_RentalStatus, lb_Title, lb_Author, lb_Publisher, lb_BookCondition, lb_FullPrice, lb_SalePrice,
			lb_LendPrice, lb_Introduction;
	
	private Book book;
	@FXML
	private ToggleGroup status; 
	


	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		this.book=DataModel.book_for_registered;
		super.base();
		// Base end
		if(book==null) {
			lb_Title.setText("책이 존재하지 않습니다.");
		}
		else {
			
			
			if(book.getRental_status()) {//가능하다면
				Rbtn_canLend.setSelected(true);
				Rbtn_soldOut.setDisable(true);
				Rbtn_lend.setDisable(true);
				tf_who.setDisable(true);
			}else {
				//팔렸을때랑, 빌려줬을때 구분
			}
			
			
			lb_Title.setText(book.getTitle());
			lb_Author.setText(book.getAuther());
			lb_Title.setText(book.getTitle());
			lb_Author.setText(book.getAuther());
			lb_Publisher.setText(book.getPublisher());
			lb_BookCondition.setText(book.getBook_condition());
			lb_FullPrice.setText(book.getFull_price()+"");
			lb_SalePrice.setText(book.getSale_price()+"");
			lb_LendPrice.setText(book.getLend_price()+"");
			lb_Introduction.setText(book.getIntroduction());
		}	
	}

	public void backAction() {// 전의 화면으로
	}

	public void removeAction() {// 책 삭제
	}

	public void comfirmAction() {//정보 수정된 상태로 저장 (만약 책을 빌리거나 구매한다는 alert가 뜨면, 그거에 맞춰서 수정해줌,)
	}

	public void goLeftAction() {//왼쪽으로 이동하면서 다른 RegisteredBook보여주기
	}

	public void goRightAction() {//오른쪽으로 이동하면서 다른 RegisteredBook보여주기
	}
}
