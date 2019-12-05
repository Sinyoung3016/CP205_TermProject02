package Gui;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import user.User;

public class UserDetail_Controller extends Base_Controller implements Initializable {

	@FXML
	public TextField tf_Name, tf_Email, tf_Phone;
	@FXML
	public PasswordField pf_Password;
	@FXML
	public Label lb_Rent;
	@FXML
	public Label lb_error_pw, lb_error_name, lb_error_email, lb_error_phone, lb_error_address;
	@FXML
	public ChoiceBox<String> cb_Email;
	@FXML
	public TextArea ta_Address;
	@FXML
	public Button btn_ChangeInfo, btn_Confirm;
	public User user;
	public Socket socket;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		super.base();
		// Base end

		// email init start
		ObservableList<String> emailList = FXCollections.observableArrayList("naver.com", "gmail.com", "hanmail.net");
		cb_Email.setItems(emailList);
		// email init end

		// MyInfo print start
		user = DataModel.user;
		tf_Name.setText(user.getName());
		pf_Password.setText(user.getPassword());
		if (user.isLend_OK()) {
			lb_Rent.setText("대여 가능");
		} else {
			lb_Rent.setText("대여 불가 : 사유) 책 미반납");
		}
		String[] email = user.getEmail().split("@");
		tf_Email.setText(email[0]);
		cb_Email.setValue(email[1]);
		tf_Phone.setText(user.getPhone());
		ta_Address.setText(user.getAddress());
		// MyInfo print end

		// 수정 양식 start
		pf_Password.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if (newValue.length() == 0 || FormException.passwordFormCheck(newValue)) {
						lb_error_pw.setText("");
					}
				} catch (MyException e) {
					lb_error_pw.setText(e.getMessage());
				}
			}
		});

		tf_Name.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if (newValue.length() == 0 || FormException.NameFormCheck(newValue)) {
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
					if (newValue.length() == 0 || FormException.emailFormCheck(newValue)) {
						lb_error_email.setText("");
					}
				} catch (MyException e) {
					lb_error_email.setText(e.getMessage());
				}
			}
		});
		tf_Phone.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if (newValue.length() == 0 || FormException.phoneFormCheck(newValue)) {
						lb_error_phone.setText("");
					}
				} catch (MyException e) {
					lb_error_phone.setText(e.getMessage());
				}
			}
		});
		ta_Address.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				try {
					if (newValue.length() == 0 || FormException.AddressFormCheck(newValue)) {
						lb_error_address.setText("");
					}
				} catch (MyException e) {
					lb_error_address.setText(e.getMessage());
				}
			}
		});

		// 수정 양식 end
	}

	public void changeinfoAction() {
		if (tf_Name.getText().length() == 0 || pf_Password.getText().length() == 0 || tf_Email.getText().length() == 0
				|| tf_Phone.getText().length() == 0 || ta_Address.getText().length() == 0) {
			new Alert(Alert.AlertType.WARNING, "빈칸을 모두 채워주세요", ButtonType.CLOSE).show();
		} else if (!lb_error_name.getText().equals("") ||  !lb_error_pw.getText().equals("")
				|| !lb_error_phone.getText().equals("") ||!lb_error_email.getText().equals("")||!lb_error_address.getText().equals("")) {
			new Alert(Alert.AlertType.WARNING, "형식에 맞지 않는 칸이 있습니다.", ButtonType.CLOSE).show();
		}else {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "수정된 정보로 저장하시겠습니까?", ButtonType.YES,ButtonType.NO);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.YES) {
				// user info modify start
				try {
		
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8));
					pw.println("ModifyUserData:"+user.getID()+":"+pf_Password.getText()+":"+tf_Name.getText()+":"+tf_Phone.getText()+":"+tf_Email.getText()+ "@"
							+ cb_Email.getSelectionModel().getSelectedItem().toString()+":"+ta_Address.getText());
					pw.flush();
					// ModifyUserData:ID:PW:NAME:PHONE:EMAIL:ADDRESS
					
				
				}catch (IOException e) {
					e.printStackTrace();
				} 
				try {
					Stage primaryStage = (Stage) btn_MyInfo.getScene().getWindow();
					Parent search = FXMLLoader.load(getClass().getResource("/Gui/UserDetail_GUI.fxml"));
					Scene scene = new Scene(search);
					primaryStage.setTitle("HelloBooks/MyInfo");
					primaryStage.setScene(scene);
					primaryStage.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
				

			}else if (result.get() == ButtonType.CANCEL) {
				try {
					Stage primaryStage = (Stage) btn_MyInfo.getScene().getWindow();
					Parent search = FXMLLoader.load(getClass().getResource("/Gui/UserDetail_GUI.fxml"));
					Scene scene = new Scene(search);
					primaryStage.setTitle("HelloBooks/MyInfo");
					primaryStage.setScene(scene);
					primaryStage.show();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void confirmAction() {// OK
		try {
			Stage primaryStage = (Stage) btn_Main.getScene().getWindow();
			Parent main = FXMLLoader.load(getClass().getResource("/Gui/Main_GUI.fxml"));
			Scene scene = new Scene(main);
			primaryStage.setTitle("HelloBooks/Main");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
