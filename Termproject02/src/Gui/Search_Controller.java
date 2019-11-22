package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Search_Controller extends Base_Controller implements Initializable {

	public ChoiceBox cb_genre, cb_BookCondition, cb_RentalStatus, cb_About;
	public ListView lv_BookList;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		super.base();
		// Base end
	}
	
	
}
