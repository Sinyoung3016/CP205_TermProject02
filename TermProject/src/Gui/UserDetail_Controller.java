package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import authentication.User;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class UserDetail_Controller extends Base_Controller implements Initializable{

	
	public TextField nameField, passwordField, email, phonenumberField;
	public Label canuserrentField;
	public ChoiceBox email_next;
	public TextArea addressField;
	public Button changeinfoButton, comfirmButton;
	public User user;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		user=DataModel.user;
		// TODO Auto-generated method stub
		userName.setText(user.getName());
		nameField.setText(user.getName());
		
		passwordField.setText(user.getPassword());
		
		userId.setText(user.getID());
		if(user.isLend_OK()) {
			lendOK.setText("대여가능");
			canuserrentField.setText("대여가능");
		}else {
			lendOK.setText("대여불가");
			canuserrentField.setText("대여불가");
		}
		email.setText(user.getEmail());
		phonenumberField.setText(user.getPhone());
		addressField.setText(user.getAddress());
		
	}
	
	public void changeinfoAction() { //만약 field에 값을 넣어줬으면 수정
		nameField.getText();
		passwordField.getText();
		email.getText();
		email_next.getValue();
		phonenumberField.getText();
		addressField.getText();
	}
	
	public void confirmAction() { //화면종료하고 전의 화면으로 
		
	}

}
