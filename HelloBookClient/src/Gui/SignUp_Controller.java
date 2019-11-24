package Gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import exception.FormException;
import exception.MyException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignUp_Controller implements Initializable {

	@FXML
	public TextField tf_ID, tf_Name, tf_Email, tf_phoneNum;
	@FXML
	public PasswordField pf_Password, pf_Confirm;
	@FXML
	public ChoiceBox<String> cb_Email;
	@FXML
	public TextArea ar_address;
	@FXML
	public Button btn_Main, btn_SignUp;
	@FXML
	public Label lb_error_id , lb_error_pw , lb_error_confirm , lb_error_name , lb_error_email , lb_error_phone;
	
	
	
	public Socket socket;

	@FXML
	public void mainAction() throws Exception {
		try {
			Stage primaryStage = (Stage) btn_Main.getScene().getWindow();
			Parent root = FXMLLoader.load(getClass().getResource("/Gui/Login_GUI.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("HelloBooks");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@FXML
	public void signupAction() {
		//회원가입 내역 전부 작성 X
		if (tf_ID.getText().length() == 0 || pf_Password.getText().length() == 0
				|| tf_Name.getText().length() == 0 || tf_Email.getText().length() == 0
				|| tf_phoneNum.getText().length() == 0 || cb_Email.getValue() == null
				|| ar_address.getText().length() == 0)
			new Alert(Alert.AlertType.WARNING, "빈칸을 전부 채워주세요.", ButtonType.CLOSE).show();

		//회원가입 내역 완벽할때
		else {
			String message=null;
			try {
				String m="SignUp:"+tf_ID.getText()+":"+pf_Password.getText()+":"+tf_Name.getText()+":"+tf_phoneNum.getText()+":"+tf_Email.getText()+"@"+cb_Email.getSelectionModel().getSelectedItem().toString()+":"+ar_address.getText();
				BufferedReader br= new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),true);
				pw.println(m);
				pw.flush();
				message = br.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String [] tokens=message.split(":");
			if(tokens[0].equals("SignUp")) {
				if(!tokens[1].equals("성공")) {//실패
					new Alert(Alert.AlertType.WARNING, tokens[1], ButtonType.CLOSE).show();
				}
				else {
					try {
						Stage primaryStage = (Stage) btn_SignUp.getScene().getWindow();
						Parent search = FXMLLoader.load(getClass().getResource("/Gui/Login_GUI.fxml"));
						Scene scene = new Scene(search);
						primaryStage.setTitle("HelloBooks");
						primaryStage.setScene(scene);
						primaryStage.show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
			
		}
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		socket=DataModel.socket;
		ObservableList<String> publicComboList = FXCollections.observableArrayList("naver.com", "gmail.com",
				"hanmail.net");
		cb_Email.setItems(publicComboList);
		
		tf_ID.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if(newValue.length()==0||FormException.IDFormCheck(newValue)) {
						lb_error_id.setText("");
					}
				} catch (MyException e) {
					lb_error_id.setText(e.getMessage());
				}

			}
		});
		pf_Password.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if(newValue.length()==0||FormException.passwordFormCheck(newValue)) {
						lb_error_pw.setText("");
					}
				} catch (MyException e) {
					lb_error_pw.setText(e.getMessage());
				}

			}
		});
		pf_Confirm.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
					if(newValue.length()==0||pf_Password.getText().equals(pf_Confirm.getText())) {
						lb_error_confirm.setText("");
					}
					else {
					lb_error_confirm.setText("비밀번호가 일치하지 않습니다.");
				}

			}
		});
		tf_Name.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if(newValue.length()==0||FormException.NameFormCheck(newValue)) {
						lb_error_name.setText("");
					}
				} catch (MyException e) {
					lb_error_name.setText(e.getMessage());
				}

			}
		});
		tf_Email.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if(newValue.length()==0||FormException.emailFormCheck(newValue)) {
						lb_error_email.setText("");
					}
				} catch (MyException e) {
					lb_error_email.setText(e.getMessage());
				}

			}
		});
		tf_phoneNum.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if(newValue.length()==0||FormException.phoneFormCheck(newValue)) {
						lb_error_phone.setText("");
					}
				} catch (MyException e) {
					lb_error_phone.setText(e.getMessage());
				}

			}
		});
	}

}
