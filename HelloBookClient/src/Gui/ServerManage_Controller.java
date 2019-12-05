package Gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class ServerManage_Controller implements Initializable {
	@FXML
	public TextField tf_notice, tf_shutdown; 
	@FXML//						강제종료							서버 닫기		화면 닫기
	public Button btn_Notice, btn_shutdown, btn_ServerOpen, btn_ServerClose, btn_Close, btn_Main;

	@FXML
	public void noticeAction() { //공지
	}

	@FXML
	public void shutdownAction() { // 강제종료

	}

	@FXML
	public void serverOpenAction() {// 서버 열기

	}

	@FXML
	public void serverCloseAction() { // 서버 닫기

	}

	@FXML
	public void closeAction() {// 화면 끄기
		Stage stage = (Stage) btn_Close.getScene().getWindow();
	    stage.close();
	}

	@FXML
	public void mainAction() { //다시 같은 화면 로드
		try {
			Stage primaryStage = (Stage) btn_Main.getScene().getWindow();
			Parent search = FXMLLoader.load(getClass().getResource("/Gui/ServerManage_GUI.fxml"));
			Scene scene = new Scene(search);
			primaryStage.setTitle("HelloBooks Server");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}

}
