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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import user.User;

public class UploadBook_Controller extends Base_Controller implements Initializable {

	@FXML
	public Button btn_Back, btn_Save;
	@FXML
	public ChoiceBox<String> cb_Genre;
	@FXML
	public RadioButton rb_BCTop;
	@FXML
	public RadioButton rb_BCMid;
	@FXML
	public RadioButton rb_BCLow;
	@FXML
	public TextField tf_Title;
	@FXML
	public TextField tf_Author;
	@FXML
	public TextField tf_Publisher;
	@FXML
	public TextField tf_FullPrice;
	@FXML
	public TextField tf_SalePrice;
	@FXML
	public TextField tf_LendPrice;
	
	@FXML
	public TextArea ta_Introduction;
	@FXML
	private ToggleGroup condition;
	
	private Socket socket;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		socket=DataModel.socket;
		// Base start
		super.base();
		// Base end
		ObservableList<String> publicComboList = FXCollections.observableArrayList("인문/사회", "수학/과학",
				"공학","역사","소설","동화","기타");
		cb_Genre.setItems(publicComboList);
	}

	public void backAction() {// 전의 화면으로 

	}

	public void saveAction() {// 책 업로드
		if(tf_Title.getText().length()==0||tf_Author.getText().length()==0||tf_Publisher.getText().length()==0||cb_Genre.getSelectionModel().getSelectedItem()==null||
				condition.getSelectedToggle()==null||tf_FullPrice.getText().length()==0||tf_SalePrice.getText().length()==0||tf_LendPrice.getText().length()==0||ta_Introduction.getText().length()==0) {
			new Alert(Alert.AlertType.WARNING, "칸을 모두 채워주세요", ButtonType.CLOSE).show();
		}else if(tf_Title.getText().contains(":")||tf_Author.getText().contains(":")||tf_Publisher.getText().contains(":")||ta_Introduction.getText().contains(":")) {
			new Alert(Alert.AlertType.WARNING, "특수문자 ':'을 사용하실 수 없습니다. ", ButtonType.CLOSE).show();
		}
		else {

			try {
				Integer.parseInt(tf_FullPrice.getText());
				Integer.parseInt(tf_SalePrice.getText());
				Integer.parseInt(tf_LendPrice.getText());
	
				PrintWriter pw = new PrintWriter(
					new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
		
				String request="AddBookData:";
				request+=tf_Title.getText()+":";
				request+=tf_Author.getText()+":";
				request+=tf_Publisher.getText()+":";
				request+=cb_Genre.getSelectionModel().getSelectedItem().toString()+":";
				request+=condition.getSelectedToggle().getUserData()+":";
				request+=tf_FullPrice.getText()+":";
				request+=tf_SalePrice.getText()+":";
				request+=tf_LendPrice.getText()+":";
				request+="true:";
				request+=ta_Introduction.getText();
		
				pw.println(request);
				pw.flush();

			}catch(NumberFormatException e) {
				new Alert(Alert.AlertType.WARNING, "가격은 숫자만 입력해주세요", ButtonType.CLOSE).show();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}

	}
}
