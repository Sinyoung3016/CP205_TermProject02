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

import Gui.model.ClientThread;
import Gui.model.DataModel;
import javafx.application.Platform;
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
import javafx.stage.Stage;
import user.User;

public class Login_Controller implements Initializable {
	@FXML
	public Label lb_error;
	@FXML
	public TextField tf_ID;
	@FXML
	public PasswordField pf_Password;
	@FXML
	public Button btn_Login, btn_SignUp;

	public Socket socket;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		socket = new Socket();
		try {
			socket.connect(new InetSocketAddress(DataModel.SERVER_IP, DataModel.PORT));
			DataModel.socket = socket;
 
		}catch (Exception e) {
			e.printStackTrace();
			//if (e.getMessage().equals("Connection refused: connect")) {
			//	lb_error.setText("서버에 연결되지 않음");
				lb_error.setText(e.getMessage());
				btn_Login.setDisable(true);
				btn_SignUp.setDisable(true);
			//}
		}
	}

	@FXML
	public void loginAction() {
		// Login start
		// ID와 Password를 둘중 하나라도 입력하지 않을 때
		if (tf_ID.getText().length() == 0 || pf_Password.getText().length() == 0) {
			new Alert(Alert.AlertType.WARNING, "빈칸을 모두 채워주세요", ButtonType.CLOSE).show();
		} else {

			// ID와 Password를 둘다 입력했을 때
			String message = null;
			try {
				String m = "LogIn:" + tf_ID.getText() + ":" + pf_Password.getText();
				BufferedReader br = new BufferedReader(
						new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				PrintWriter pw = new PrintWriter(
						new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8), true);
				pw.println(m);
				pw.flush();
				message = br.readLine();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
			String[] tokens = message.split(":");
			if (tokens[0].equals("LogIn")) {
				if (!tokens[1].equals("성공")) {
					// 성공이 아닐 때 token[1]은 에러메세지
					new Alert(Alert.AlertType.WARNING, tokens[1], ButtonType.CLOSE).show();
				} else {
					// 로그인에 성공하면 Main으로
					try {
						DataModel.user = new User(tokens[2]);
						DataModel.ID = tf_ID.getText();
						new ClientThread(DataModel.socket).start();
						
						Thread.sleep(500);
						
						Stage primaryStage = (Stage) btn_Login.getScene().getWindow();
						Platform.runLater(() -> {//이걸 안 해주면 ClientThread에서 새로운 책을 등록할 때  Platform.runLater를 사용하여, 메인화면이 다 설정된 다음 새로운 책 등록이 됨-> 맨 처음 로그인했을 때 버튼을 눌러도 아무 효과가 발생되지 않음.
						Parent login;
						try {
							login = FXMLLoader.load(getClass().getResource("/Gui/Main_GUI.fxml"));
							Scene scene = new Scene(login);
							primaryStage.setTitle("HelloBooks/Main");
							primaryStage.setScene(scene);
							primaryStage.show();
							
						} catch (IOException e) {
							e.printStackTrace();
						}	
						});
		
						
						
						
						//primaryStage.show();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		// Login end
	}

	@FXML
	public void signUpAction() throws Exception {
		// To SignUp_GUI
		try {
			Stage primaryStage = (Stage) btn_SignUp.getScene().getWindow();
			Parent login = FXMLLoader.load(getClass().getResource("/Gui/SignUp_GUI.fxml"));
			Scene scene = new Scene(login);
			primaryStage.setTitle("HelloBooks/signup");
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
