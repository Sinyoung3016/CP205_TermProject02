package Gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import authentication.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Login_Controller implements Initializable {
	@FXML
	public Label lb_error;
	@FXML
	public TextField idField;
	@FXML
	public PasswordField passwordField;
	@FXML
	public Button loginButton, signUpButton;
	@FXML
	public VBox MainVbox;
	public Socket socket;

	@FXML
	public void loginAction(){
		if(idField.getText().length()==0||passwordField.getText().length()==0) {
			new Alert(Alert.AlertType.WARNING, "빈칸을 모두 채워주세요", ButtonType.CLOSE).show();
		}else {
			String message=null;
			try {
				String m="LogIn:"+idField.getText()+":"+passwordField.getText();
				BufferedReader br= new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.UTF_8));
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8),true);
				pw.println(m);
				pw.flush();
				message = br.readLine();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String [] tokens=message.split(":");
			if(tokens[0].equals("LogIn")) {
				if(!tokens[1].equals("성공")) {
					new Alert(Alert.AlertType.WARNING, tokens[1], ButtonType.CLOSE).show();//성공이 아닐 때 token[1]은 에러메세지
				}else {
					//로그인에 성공하면
					try {
						DataModel.user=new User(tokens[2]);
						DataModel.ID=idField.getText();
						Stage primaryStage = (Stage) loginButton.getScene().getWindow();
						Parent login = FXMLLoader.load(getClass().getResource("/Gui/Main_GUI.fxml"));
						Scene scene = new Scene(login);
						primaryStage.setTitle("HelloBooks/Main");
						primaryStage.setScene(scene);
						primaryStage.show();
					} 	catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}	
	}

	@FXML
	public void signUpAction() throws Exception {
		try {
			Stage primaryStage = (Stage) signUpButton.getScene().getWindow();
			Parent login = FXMLLoader.load(getClass().getResource("/Gui/SignUp_GUI.fxml"));
			Scene scene = new Scene(login);
			primaryStage.setTitle("HelloBooks/signup");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		socket=new Socket();
			try {
				socket.connect(new InetSocketAddress(DataModel.SERVER_IP, DataModel.PORT));
				DataModel.socket=socket;
				
			} catch (IOException e) {
				if(e.getMessage().equals("Connection refused: connect")) {
					lb_error.setText("서버에 연결되지 않음");
					loginButton.setDisable(true);
					signUpButton.setDisable(true);
				}
		}
	}
}
