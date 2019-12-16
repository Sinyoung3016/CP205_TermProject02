package Gui;

import java.io.BufferedReader;
import java.io.File;
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
import Gui.model.LogInModel;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;
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
	File clickSoundDirFile = new File(".\\sound\\click");
	File[] clickSoundFileList = clickSoundDirFile.listFiles();
	@FXML
	Button btn_Sound;
	
	
	Media[] c_me= {new Media(clickSoundFileList[0].toURI().toString()),
			new Media(clickSoundFileList[1].toURI().toString())
			};
	MediaPlayer[] c_mp= {new MediaPlayer(c_me[0]),new MediaPlayer(c_me[1])};
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		c_mp[0].setVolume(0.1);
		c_mp[1].setVolume(0.1);
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
				btn_Sound.setDisable(true);
			//}
		}
		
		pf_Password.setOnAction(event -> {
			loginAction();
	    }
	    );
		LogInModel.b_mp.setVolume(0.2);
		btn_Sound.setText(LogInModel.BGM);
		btn_Sound.addEventHandler(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	if(btn_Sound.getText().equals("On")) {//on인 상태에서 누르면
            		btn_Sound.setText("Off");
            		LogInModel.BGM="Off";
            		LogInModel.b_mp.pause();
            	}
            	else {
            		btn_Sound.setText("On");
            		LogInModel.BGM="On";
            		LogInModel.b_mp.play();
            	}
            }
        });
		if(LogInModel.status_log_outed) {
			if(btn_Sound.getText().equals("On")) {
				LogInModel.b_mp.play();
				LogInModel.b_mp.setOnEndOfMedia(()->LogInModel.b_mp.seek(new Duration(120)));
			}	
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
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
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

						Stage primaryStage = (Stage) btn_Login.getScene().getWindow();
						Platform.runLater(() -> {//이걸 안 해주면 ClientThread에서 새로운 책을 등록할 때  Platform.runLater를 사용하여, 메인화면이 다 설정된 다음 새로운 책 등록이 됨-> 맨 처음 로그인했을 때 버튼을 눌러도 아무 효과가 발생되지 않음.
						Parent login;
						try {
							login = FXMLLoader.load(getClass().getResource("/Gui/Main_GUI.fxml"));
							Scene scene = new Scene(login);
							primaryStage.setTitle("HelloBooks/Main");
							primaryStage.setScene(scene);
							primaryStage.show();
							LogInModel.b_mp.stop();
						} catch (IOException e) {
							e.printStackTrace();
						}	
						});
		
						//primaryStage.show();
					} catch (Exception e) {
						e.printStackTrace();
						lb_error.setText(e.getMessage());
						btn_Login.setDisable(true);
						btn_SignUp.setDisable(true);
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
			LogInModel.status_log_outed=false;
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
	public void ButtonHover() {
		c_mp[1].play();
	}
	public void ButtonExited() {
		c_mp[1].stop();
	}
	public void ButtonClicked() {
		c_mp[0].play();
	}
	




}
