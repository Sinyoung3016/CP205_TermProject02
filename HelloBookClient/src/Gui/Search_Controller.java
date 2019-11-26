package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Search_Controller extends Base_Controller implements Initializable {

	@FXML
	public ChoiceBox cb_genre, cb_BookCondition, cb_RentalStatus, cb_About;
	@FXML
	public ListView lv_BookList;
	@FXML
	public Button btn_DetailSearch;
	@FXML
	public TextField tf_Title, tf_Author , tf_Publisher;
	

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		super.base();
		// Base end
	}
	@FXML
	public void DetailSearchAction() {
		
	}
}
