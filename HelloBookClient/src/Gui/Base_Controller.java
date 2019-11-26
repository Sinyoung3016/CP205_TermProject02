package Gui;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

import Gui.model.DataModel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Base_Controller { // 변하지 않는 화면 = Base

	/*
	 * lb = label btn = button tf = textfield ts = textArea lv = listview
	 * 
	 */
	public Button btn_Main, btn_Search, btn_BookSales, btn_LogOut, btn_MyInfo, btn_MyBookList, btn_ProfileWishList,
	btn_ProfileNewAlert;

	public TextField tf_Search;

	public Label lb_ProfileName, lb_ProfileID, lb_ProfileLend;

	public ListView lv_ProfileList;
	

	
	public void base() {

		// Profile start
		lb_ProfileName.setText(DataModel.user.getName());
		lb_ProfileID.setText(DataModel.user.getID());
		if (DataModel.user.isLend_OK()) {
			lb_ProfileLend.setText("대여 가능");
		} else {
			lb_ProfileLend.setText("대여 불가");
		}
		// Profile end

		// ProfileList 초기화 start
		// ProfileList 초기화 end
	}

	public void mainAction() throws Exception {
		// To Main_GUI
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

	public void logoutAction() throws Exception {

		//Logout start
		LogOut();
		//Logout end

		//To Login_GUI
		try {
			Stage primaryStage = (Stage) btn_LogOut.getScene().getWindow();
			Parent search = FXMLLoader.load(getClass().getResource("/Gui/Login_GUI.fxml"));
			Scene scene = new Scene(search);
			primaryStage.setTitle("HelloBooks");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void LogOut() {
		if(DataModel.socket!=null) {
			String m = "LogOut:" + DataModel.ID;
			DataModel.ID = null;
			PrintWriter pw;
			try {
				pw = new PrintWriter(
						new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8), true);
				pw.println(m);
				pw.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}

	public void searchAction() throws Exception { 
		//tf_Search 내용 받기 start
		//tf_Search 내용 받기 end
		
		//To Search_Gui
		try {
			Stage primaryStage = (Stage) btn_Search.getScene().getWindow();
			Parent search = FXMLLoader.load(getClass().getResource("/Gui/Search_GUI.fxml"));
			Scene scene = new Scene(search);
			primaryStage.setTitle("HelloBooks/search");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salesAction() { 
		// To UploadBook_GUI
		try {
			Stage primaryStage = (Stage) btn_Search.getScene().getWindow();
			Parent search = FXMLLoader.load(getClass().getResource("/Gui/UploadBook_GUI.fxml"));
			Scene scene = new Scene(search);
			primaryStage.setTitle("HelloBooks/UploadBook");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mybooklistAction() { 
		// To MyBookList_GUI
		try {
			Stage primaryStage = (Stage) btn_MyInfo.getScene().getWindow();
			Parent search = FXMLLoader.load(getClass().getResource("/Gui/MyBookList_GUI.fxml"));
			Scene scene = new Scene(search);
			primaryStage.setTitle("HelloBooks/MyBookList");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		

	}

	public void myInfoAction() throws Exception { 
		// To UserDetail_GUI
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

	public void wishlistAction() { 
		// lv_ProfileList에 장바구니 리스트 보여주기

	}

	public void alertAction() { 
		// lv_ProfileList에 새로운 알람 보여주기
		//Alert에 있어야 하는 것 : 판매한다고 하는 유저 ID, btn_내용 삭제하기 버튼, btn_그책으로 바로가기

	}

}
