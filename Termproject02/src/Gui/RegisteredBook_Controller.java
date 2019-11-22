package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class RegisteredBook_Controller extends Base_Controller implements Initializable {

	public Button btn_Back, btn_Left, btn_Right, btn_Remove, btn_Comfirm;
	public Label lb_RentalStatus, lb_Title, lb_Author, lb_Publisher, lb_BookCondition, lb_FullPrice, lb_SalePrice,
			lb_LendPrice, lb_Introduction;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		super.base();
		// Base end
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
