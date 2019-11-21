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
	public Button left, right;
	@FXML
	public ImageView adPicture;
	@FXML
	public Label adExplain;
	@FXML
	public ListView newBookField, bestSellerField;

	private Image[] ad = {

	};

	private Image[] user = {

	};

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		userName.setText(DataModel.user.getName());
		userId.setText(DataModel.user.getID());
		if(DataModel.user.isLend_OK()) {
			lendOK.setText("대여가능");
		}else {
			lendOK.setText("대여불가");
		}
	}

	@FXML
	public void goLeftAction() {
		// adPicture.setImage();
	}

	@FXML
	public void goRightAction() {

	}

}
