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

public class Base_Controller { // ������ �ʴ� ȭ�� = Base

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
			lb_ProfileLend.setText("�뿩 ����");
		} else {
			lb_ProfileLend.setStyle("-fx-text-fill: Red;");
			lb_ProfileLend.setText("�뿩 �Ұ�");
		
		}
		// Profile end

		// ProfileList �ʱ�ȭ start
		// ProfileList �ʱ�ȭ endt
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
		    		if(newValue.getRequest_Status().equals("������")) {
		    			Alert alert = new Alert(Alert.AlertType.WARNING, lv_alter_list.getSelectionModel().getSelectedItem().toString(), ButtonType.YES, ButtonType.NO);
		    			Optional<ButtonType> result = alert.showAndWait();
		    			if (result.get() == ButtonType.YES) {
		    				// ��ϵ� å�� ����
		    				try {
		    					PrintWriter pw=new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));//BorrowAnswer:(����,�ź�):��û��:å��ȣ:å����:��û�޴���
		    					pw.println("BorrowAnswer:����:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getBook_Title()+":"+newValue.getRequester_ID());
		    					pw.flush();
		    					int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
		    					Platform.runLater(() -> {ItemList_alter.remove(index1);});
		    				} catch (IOException e) {e.printStackTrace();}
		    			}else {
		    				try {
		    					PrintWriter pw=new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));
		    					pw.println("BorrowAnswer:����:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getBook_Title()+":"+newValue.getRequester_ID());
		    					pw.flush();
		    					int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
		    					Platform.runLater(() -> {ItemList_alter.remove(index1);});
							} catch (IOException e) {e.printStackTrace();}
		    			}
		    		}//�뿩��û�� ���� �˸� ��
		    		
		    		else if(newValue.getRequest_Status().equals("�����ִ�")||newValue.getRequest_Status().equals("�Ⱥ����ִ�")){
		    			Alert alert = new Alert(Alert.AlertType.WARNING, lv_alter_list.getSelectionModel().getSelectedItem().toString(), ButtonType.YES);
		    			alert.show();
						try {
							PrintWriter pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));
							pw.println("AlterOK:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getRequest_Status());//AlterOK:��û��:å��ȣ:(�����ִ�,�Ⱥ����ִ�)
	    					pw.flush();
							int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
							Platform.runLater(() -> {ItemList_alter.remove(index1);});
						} catch (IOException e) {e.printStackTrace();}
						
		    		}//���ȴ�, �����ȴٿ� ���� �˸� ��
		    		else if(newValue.getRequest_Status().equals("�ݳ��ϴ�")) {
		    			Alert alert = new Alert(Alert.AlertType.WARNING, lv_alter_list.getSelectionModel().getSelectedItem().toString(), ButtonType.YES);
		    			alert.show();
		    			try {
							PrintWriter pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));
							pw.println("AlterOK:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getRequest_Status());//AlterOK:��û��:å��ȣ:�ݳ��ϴ�
	    					pw.flush();
							int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
							Platform.runLater(() -> {ItemList_alter.remove(index1);});
						} catch (IOException e) {e.printStackTrace();}
		    		}//�ݳ��ߴٿ� ���� �˸� ��
		    		
		    		
		    		if(newValue.getRequest_Status().equals("���")) {
		    			Alert alert = new Alert(Alert.AlertType.WARNING, lv_alter_list.getSelectionModel().getSelectedItem().toString(), ButtonType.YES, ButtonType.NO);
		    			Optional<ButtonType> result = alert.showAndWait();
		    			if (result.get() == ButtonType.YES) {
		    				try {
		    					PrintWriter pw=new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));//BorrowAnswer:(����,�ź�):��û��:å��ȣ:å����:��û�޴���

		    					pw.println("PurchaseAnswer:����:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getBook_Title()+":"+newValue.getRequester_ID());
		    					pw.flush();
		    					int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
		    					Platform.runLater(() -> {ItemList_alter.remove(index1);});
		    					
					
		    				} catch (IOException e) {e.printStackTrace();}
					
		    			}else {
		    				try {
		    					PrintWriter pw=new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));//BorrowAnswer:(����,�ź�):��û��:å��ȣ:å����:��û�޴���

		    					pw.println("PurchaseAnswer:����:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getBook_Title()+":"+newValue.getRequester_ID());
		    					pw.flush();
		    					int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
		    					Platform.runLater(() -> {ItemList_alter.remove(index1);});
							} catch (IOException e) {e.printStackTrace();}
		    			}
		    		}//���ſ�û�� ���� �˸� ��
		    		else if(newValue.getRequest_Status().equals("�ȴ�")||newValue.getRequest_Status().equals("���ȴ�")){
		    			Alert alert = new Alert(Alert.AlertType.WARNING, lv_alter_list.getSelectionModel().getSelectedItem().toString(), ButtonType.YES);
		    			alert.show();
		    			
						try {
							PrintWriter pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));
							pw.println("AlterOK:"+newValue.getRequested_ID()+":"+newValue.getBook_Number()+":"+newValue.getRequest_Status());//AlterOK:��û��:å��ȣ:(�����ִ�,�Ⱥ����ִ�)
	    					pw.flush();
							int index1=lv_alter_list.getSelectionModel().getSelectedIndex();
							Platform.runLater(() -> {ItemList_alter.remove(index1);});
						} catch (IOException e) {e.printStackTrace();}
						
		    		}//��� ����ٿ� ���� �˸� ��
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
		System.exit(0);//�̰� �� ���ָ�, �α��� ��ư ������ ����ȭ�� �߱� ���� X��ư�� ������, �α׾ƿ��� �ʰ� ���ÿ� Main ȭ���� �� ������ �߻��Ѵ�.!
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
		//tf_Search ���� �ޱ� start
		if (tf_Search.getText().contains("-")) {
			new Alert(Alert.AlertType.WARNING, "Ư������'-'�� ����Ͻ� �� �����ϴ�.", ButtonType.CLOSE).show();
		} else if (tf_Search.getText().contains(":") ) {
			new Alert(Alert.AlertType.WARNING, "Ư������':'�� ����Ͻ� �� �����ϴ�.", ButtonType.CLOSE).show();
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
				
			
		//tf_Search ���� �ޱ� end
		
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
		// lv_ProfileList�� ���ο� �˶� �����ֱ�
		//Alert�� �־�� �ϴ� �� : �Ǹ��Ѵٰ� �ϴ� ���� ID, btn_���� �����ϱ� ��ư, btn_��å���� �ٷΰ���
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
