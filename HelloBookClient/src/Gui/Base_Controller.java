package Gui;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

import Gui.model.DataModel;
import Gui.model.LogInModel;
import alert.UserAlert;
import alter.UserAlter;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Base_Controller { // 변하지 않는 화면 = Base

	/*
	 * lb = label btn = button tf = textfield ts = textArea lv = listview
	 * 
	 */
	public Button btn_Main, btn_Search, btn_BookSales, btn_LogOut, btn_MyInfo, btn_MyBookList, 
	btn_ProfileNewAlert;

	public TextField tf_Search;

	public Label lb_ProfileName, lb_ProfileID, lb_ProfileLend;

	public ListView lv_alter_list;
	
	File dirFile = new File(".\\sound\\click");
	File[] fileList = dirFile.listFiles();
	Media[] me= {new Media(fileList[0].toURI().toString()),
			new Media(fileList[1].toURI().toString())
			};
	
	MediaPlayer[] mp= {new MediaPlayer(me[0]),new MediaPlayer(me[1])};
	
	@FXML
	public AnchorPane AnchorPane;
	
	private ObservableList<UserAlter> ItemList_alter;
	
	@SuppressWarnings("unchecked")
	public void base() {
		mp[0].setVolume(0.1);
		mp[1].setVolume(0.1);
		// Profile start
		lb_ProfileName.setText(DataModel.user.getName());
		lb_ProfileID.setText(DataModel.user.getID());
		if (DataModel.user.isLend_OK()) {
			lb_ProfileLend.setText("대여 가능");
		} else {
			lb_ProfileLend.setStyle("-fx-text-fill: Red;");
			lb_ProfileLend.setText("대여 불가");
		
		}
		// Profile end

		// ProfileList 초기화 start
		// ProfileList 초기화 endt
		tf_Search.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
		        	try {
						searchAction();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
		    });
		
		this.ItemList_alter=DataModel.ItemList_alter;
		lv_alter_list.setItems(ItemList_alter);



		lv_alter_list.setOnMouseClicked(new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent event) {
	        	
	        	int index=lv_alter_list.getSelectionModel().getSelectedIndex();
	        	
	        	if(index>=0) {
	        	UserAlter newValue= ItemList_alter.get( lv_alter_list.getSelectionModel().getSelectedIndex());
	        	if(newValue!=null) {
		    		if(newValue.getRequest_Status().equals("빌리다")) {
		    			Alert alert = new Alert(Alert.AlertType.WARNING, lv_alter_list.getSelectionModel().getSelectedItem().toString(), ButtonType.YES, ButtonType.NO);
		    			Optional<ButtonType> result = alert.showAndWait();
		    			if (result.get() == ButtonType.YES) {
		    				// 등록된 책을 삭제
		    				try {
		    					PrintWriter pw=new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));//BorrowAnswer:(수락,거부):요청자:책번호:책제목:요청받는자
		    					pw.println("BorrowAnswer:수락:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getBook_Title()+":"+newValue.getRequester_ID());
		    					pw.flush();
		    					int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
		    					Platform.runLater(() -> {ItemList_alter.remove(index1);});
		    				} catch (IOException e) {e.printStackTrace();}
		    			}else {
		    				try {
		    					PrintWriter pw=new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));
		    					pw.println("BorrowAnswer:거절:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getBook_Title()+":"+newValue.getRequester_ID());
		    					pw.flush();
		    					int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
		    					Platform.runLater(() -> {ItemList_alter.remove(index1);});
							} catch (IOException e) {e.printStackTrace();}
		    			}
		    		}//대여요청에 대한 알림 끝
		    		
		    		else if(newValue.getRequest_Status().equals("빌려주다")||newValue.getRequest_Status().equals("안빌려주다")){
		    			Alert alert = new Alert(Alert.AlertType.WARNING, lv_alter_list.getSelectionModel().getSelectedItem().toString(), ButtonType.YES);
		    			alert.show();
						try {
							PrintWriter pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));
							pw.println("AlterOK:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getRequest_Status());//AlterOK:요청자:책번호:(빌려주다,안빌려주다)
	    					pw.flush();
							int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
							Platform.runLater(() -> {ItemList_alter.remove(index1);});
						} catch (IOException e) {e.printStackTrace();}
						
		    		}//빌렸다, 못빌렸다에 대한 알림 끝
		    		else if(newValue.getRequest_Status().equals("반납하다")) {
		    			Alert alert = new Alert(Alert.AlertType.WARNING, lv_alter_list.getSelectionModel().getSelectedItem().toString(), ButtonType.YES);
		    			alert.show();
		    			try {
							PrintWriter pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));
							pw.println("AlterOK:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getRequest_Status());//AlterOK:요청자:책번호:반납하다
	    					pw.flush();
							int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
							Platform.runLater(() -> {ItemList_alter.remove(index1);});
						} catch (IOException e) {e.printStackTrace();}
		    		}//반납했다에 대한 알림 끝
		    		
		    		
		    		if(newValue.getRequest_Status().equals("사다")) {
		    			Alert alert = new Alert(Alert.AlertType.WARNING, lv_alter_list.getSelectionModel().getSelectedItem().toString(), ButtonType.YES, ButtonType.NO);
		    			Optional<ButtonType> result = alert.showAndWait();
		    			if (result.get() == ButtonType.YES) {
		    				try {
		    					PrintWriter pw=new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));//BorrowAnswer:(수락,거부):요청자:책번호:책제목:요청받는자

		    					pw.println("PurchaseAnswer:수락:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getBook_Title()+":"+newValue.getRequester_ID());
		    					pw.flush();
		    					int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
		    					Platform.runLater(() -> {ItemList_alter.remove(index1);});
		    					
					
		    				} catch (IOException e) {e.printStackTrace();}
					
		    			}else {
		    				try {
		    					PrintWriter pw=new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));//BorrowAnswer:(수락,거부):요청자:책번호:책제목:요청받는자

		    					pw.println("PurchaseAnswer:거절:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getBook_Title()+":"+newValue.getRequester_ID());
		    					pw.flush();
		    					int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
		    					Platform.runLater(() -> {ItemList_alter.remove(index1);});
							} catch (IOException e) {e.printStackTrace();}
		    			}
		    		}//구매요청에 대한 알림 끝
		    		else if(newValue.getRequest_Status().equals("팔다")||newValue.getRequest_Status().equals("안팔다")){
		    			Alert alert = new Alert(Alert.AlertType.WARNING, lv_alter_list.getSelectionModel().getSelectedItem().toString(), ButtonType.YES);
		    			alert.show();
		    			
						try {
							PrintWriter pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));
							pw.println("AlterOK:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getRequest_Status());//AlterOK:요청자:책번호:(빌려주다,안빌려주다)
	    					pw.flush();
							int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
							Platform.runLater(() -> {ItemList_alter.remove(index1);});
						} catch (IOException e) {e.printStackTrace();}
						
		    		}//샀다 못샀다에 대한 알림 끝
		    	}
	        	}
	        }
	    });
		
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
		LogInModel.status_log_outed=true;
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
	public static void CloseButtonActione() {
		if(DataModel.socket!=null) {
			LogOut();
		}
		System.exit(0);//이걸 안 해주면, 로그인 버튼 누르고 메인화면 뜨기 전에 X버튼을 누르면, 로그아웃이 됨과 동시에 Main 화면이 떠 오류가 발생한다.!
	}
	
	public static void LogOut() {
		PrintWriter pw;
		try {
			pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8), true);
			if(DataModel.ID==null) {
				pw.println("LogOut:");
				pw.flush();
			}else {
			pw.println("LogOut:"+DataModel.ID);
			pw.flush();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}	
			DataModel.socket=null;	
			DataModel.ID=null;
			
	}
		
	

	public void searchAction() throws Exception { 
		//tf_Search 내용 받기 start
		if (tf_Search.getText().contains("-")) {
			new Alert(Alert.AlertType.WARNING, "특수문자'-'를 사용하실 수 없습니다.", ButtonType.CLOSE).show();
		} else if (tf_Search.getText().contains(":") ) {
			new Alert(Alert.AlertType.WARNING, "특수문자':'를 사용하실 수 없습니다.", ButtonType.CLOSE).show();
		} else {

			if (tf_Search.getText().length() != 0) {
				PrintWriter pw;
				try {
					pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8), true);
					pw.println("SearchBook:"+"Title-" + tf_Search.getText());
					pw.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		}
				
			
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

	public void alertAction() { 
		// lv_ProfileList에 새로운 알람 보여주기
		//Alert에 있어야 하는 것 : 판매한다고 하는 유저 ID, btn_내용 삭제하기 버튼, btn_그책으로 바로가기
	}
	
	public void ButtonHover() {
		mp[1].stop();
		mp[1].play();
	}
	public void ButtonExited() {
		mp[1].stop();
	}
	public void ButtonClicked() {
		mp[0].play();
		mp[0].setOnEndOfMedia(new Runnable() {
		        public void run() {
		        	mp[0].stop();
		        }
		    });
	}


}
