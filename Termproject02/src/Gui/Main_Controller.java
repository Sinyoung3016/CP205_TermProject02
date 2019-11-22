package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class Main_Controller extends Base_Controller implements Initializable {

	@FXML
	public Button btn_Left, btn_Right;
	@FXML
	public ImageView im_ad;
	@FXML
	public Label lb_adExplain;
	@FXML
	public ListView lv_NewBooks, lv_BestSeller;

	private Image[] ad = {

	};

	private Image[] user = {

	};

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		super.base();
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
