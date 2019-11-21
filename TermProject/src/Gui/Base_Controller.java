package Gui;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Base_Controller { //변하지 않는 화면

	public Button borrowedButton, wishlistButton, mybooklistButton, mainbutton, logoutButton, myInfoButton,
			searchButton, BooksalesButton;

	public TextField searchField;

	public Label userName, userId, lendOK;

	public ListView listField;
	
	public void base() { //초기화 
		/* 1.listField에 장바구니부터 보여주기
		 * 2. userName, userId, lendOK 초기화
		*/
	}

	public void mainAction() throws Exception { // Main으로
		try {
			Stage primaryStage = (Stage) mainbutton.getScene().getWindow();
			Parent main = FXMLLoader.load(getClass().getResource("/Gui/Main_GUI.fxml"));
			Scene scene = new Scene(main);
			primaryStage.setTitle("HelloBooks/Main");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void logoutAction() throws Exception { // 로그아웃 해서 Login으로
		try {
			String m="LogOut:"+DataModel.ID;
			DataModel.ID=null;
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8),true);
			pw.println(m);
			Stage primaryStage = (Stage) logoutButton.getScene().getWindow();
			Parent search = FXMLLoader.load(getClass().getResource("/Gui/Login_GUI.fxml"));
			Scene scene = new Scene(search);
			primaryStage.setTitle("HelloBooks");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void serachAction() throws Exception { //  searchField내용 받아서 Search로
		try {
			Stage primaryStage = (Stage) searchButton.getScene().getWindow();
			Parent search = FXMLLoader.load(getClass().getResource("/Gui/Search_GUI.fxml"));
			Scene scene = new Scene(search);
			primaryStage.setTitle("HelloBooks/search");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void salesAction() { // UploadBook로

	}

	public void mybooklistAction() { // MyBookList로

	}

	public void myInfoAction() throws Exception { // UserDetail로
		try {
			Stage primaryStage = (Stage) myInfoButton.getScene().getWindow();
			Parent search = FXMLLoader.load(getClass().getResource("/Gui/UserDetail_GUI.fxml"));
			Scene scene = new Scene(search);
			primaryStage.setTitle("HelloBooks/MyInfo");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void wishlistAction() { // listField에 장바구니 리스트 보여주기

	}

	public void borrowButton() { // listField에 빌린 책 리스트 보여주기

	}

}
