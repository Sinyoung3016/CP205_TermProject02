package Gui;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import book.Book.HBoxCell;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class MyBookList_Controller extends Base_Controller implements Initializable {

	@FXML
	public Button btn_LoanedBook, btn_BorrowedBook, btn_RegisteredBook, btn_Back;
	@FXML
	public ListView<HBoxCell> lv_MybooklistField;
	private ObservableList<HBoxCell> ItemList_myBook;
	private Socket socket;

	@FXML
	public AnchorPane AnchorPane;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		DataModel.ItemList_myBook.clear();
		// Base start
		super.base();
		// Base end
		ItemList_myBook = DataModel.ItemList_myBook;
		lv_MybooklistField.setItems(ItemList_myBook);
	}

	public void showborrowedAction() { 
		DataModel.ItemList_myBook.clear();
		socket = DataModel.socket;

		PrintWriter pw;
		try {
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
			pw.println("PrintBookList:" + DataModel.ID + ":Borrowed");
			pw.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			Thread.sleep(1000);

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Platform.runLater(() -> {
			for (HBoxCell item : ItemList_myBook) {

				item.title.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent evnet) {
						try {
							// item.num
							PrintWriter pw = new PrintWriter(
									new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8));
							pw.println("PrintBookData:Detail:" + item.num.getText());
							pw.flush(); // 책번호에 대한 정보를 달라고 요청
							
							Thread.sleep(500);
	
							Parent search = FXMLLoader.load(getClass().getResource("/Gui/BorrowedBook_GUI.fxml"));
							AnchorPane.getChildren().add(search);
							
							/*
							 * Scene scene = new Scene(search); primaryStage.setTitle("HelloBooks");
							 * primaryStage.setScene(scene); primaryStage.show();
							 */
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}
	
	

	public void showloanedAction() { // 
		DataModel.ItemList_myBook.clear();
		
		socket = DataModel.socket;

		PrintWriter pw;
		try {
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
			pw.println("PrintBookList:" + DataModel.ID + ":Loaned");
			pw.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			Thread.sleep(1000);

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Platform.runLater(() -> {
			for (HBoxCell item : ItemList_myBook) {

				item.title.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent evnet) {
						try {
							// item.num
							PrintWriter pw = new PrintWriter(
									new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8));
							pw.println("PrintBookData:Loaned:" + item.num.getText());
							pw.flush(); // 책번호에 대한 정보를 달라고 요청

							Thread.sleep(200);
							Parent search = FXMLLoader.load(getClass().getResource("/Gui/LoanedBook_GUI.fxml"));
							AnchorPane.getChildren().add(search);
							/*
							 * primaryStage.setTitle("HelloBooks"); primaryStage.setScene(scene);
							 * primaryStage.show();
							 */
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	public void showregisteredAction() { // 등록한 책 MybooklistField에 보여줘 registeredBook 전체 보여줘

		// 연속클릭 방지를 하지 못함..

		DataModel.ItemList_myBook.clear();
		socket = DataModel.socket;

		PrintWriter pw;
		try {
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
			pw.println("PrintBookList:" + DataModel.ID + ":Registered");
			pw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {

			Thread.sleep(1000);

		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Platform.runLater(() -> {
			for (HBoxCell item : ItemList_myBook) {

				item.title.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent evnet) {
						try {
							// item.num
							PrintWriter pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8));
							pw.println("PrintBookData:Registered:" + item.num.getText());
							pw.flush(); // 책번호에 대한 정보를 달라고 요청

							Thread.sleep(200);
			
							Parent search = FXMLLoader.load(getClass().getResource("/Gui/RegisteredBook_GUI.fxml"));
							AnchorPane.getChildren().add(search);
							/*
							 * Scene scene = new Scene(search); primaryStage.setTitle("HelloBooks");
							 * primaryStage.setScene(scene); primaryStage.show();
							 */
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});

	}

	public void backAction() {
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
