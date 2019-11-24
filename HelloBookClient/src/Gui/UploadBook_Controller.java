package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UploadBook_Controller extends Base_Controller implements Initializable {

	public Button btn_Back, btn_Save;
	public ChoiceBox cb_Genre;
	public RadioButton rb_BCTop, rb_BCMid, rb_BCLow;
	public TextField tf_Title, tf_Author, tf_Publisher, tf_FullPrice, tf_SalePrice, tf_LendPrice;
	public TextArea ta_Introduction;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		super.base();
		// Base end
	}

	public void backAction() {// 전의 화면으로 

	}

	public void saveAction() {// 책 업로드

	}
}
