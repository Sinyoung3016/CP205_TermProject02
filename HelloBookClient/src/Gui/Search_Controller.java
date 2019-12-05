package Gui;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import book.Book.HBoxCell;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Search_Controller extends Base_Controller implements Initializable {

	ObservableList<String> genre_list = FXCollections.observableArrayList("선택안함", "인문/사회", "수학/과학", "공학", "역사", "소설",
			"동화", "기타");
	ObservableList<String> book_condition_list = FXCollections.observableArrayList("선택안함", "상", "중", "하");
	ObservableList<String> rentalAvailable_list = FXCollections.observableArrayList("선택안함", "가능", "불가능");

	String genre;
	String book_condition;
	String rentalAvailable;

	@FXML
	public ChoiceBox<String> cb_genre, cb_BookCondition, cb_RentalStatus, cb_About;
	@FXML
	public ListView<HBoxCell> lv_BookList;
	@FXML
	public Button btn_DetailSearch;
	@FXML
	public TextField tf_Title, tf_Author, tf_Publisher;
	
	@FXML
	public AnchorPane AnchorPane;

	private ObservableList<HBoxCell> ItemList_searchBook;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (DataModel.ItemList_searchBook != null) {
			DataModel.ItemList_searchBook.clear();
		}
		// Base start
		super.base();
		// Base end

		cb_genre.setValue("선택안함");
		genre = "선택안함";
		cb_genre.setItems(genre_list);

		cb_BookCondition.setValue("선택안함");
		book_condition = "선택안함";
		cb_BookCondition.setItems(book_condition_list);

		cb_RentalStatus.setValue("선택안함");
		rentalAvailable = "선택안함";
		
		cb_RentalStatus.setItems(rentalAvailable_list);

		cb_genre.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() { // 여기 공부 !!!

			// if the item of the list is changed
			public void changed(ObservableValue ov, Number value, Number new_value) {
				genre = genre_list.get(new_value.intValue()).toString();
			}
		});

		cb_BookCondition.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() { // 여기 공부
																												// !!!

			// if the item of the list is changed
			public void changed(ObservableValue ov, Number value, Number new_value) {
				book_condition = book_condition_list.get(new_value.intValue()).toString();
			}
		});

		cb_RentalStatus.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() { // 여기 공부
																												// !!!

			// if the item of the list is changed
			public void changed(ObservableValue ov, Number value, Number new_value) {
				rentalAvailable = rentalAvailable_list.get(new_value.intValue()).toString();
			}
		});

		

		ItemList_searchBook = DataModel.ItemList_searchBook;
		lv_BookList.setItems(ItemList_searchBook);
		
		Platform.runLater(() -> {
			for (HBoxCell item : ItemList_searchBook) {

				item.title.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent evnet) {
						try {
							// item.num
							PrintWriter pw = new PrintWriter(
									new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8));
							pw.println("PrintBookData:Detail:" + item.num.getText());
							pw.flush(); // 책번호에 대한 정보를 달라고 요청

				
							Stage primaryStage = (Stage) btn_LogOut.getScene().getWindow();
							Parent search = FXMLLoader.load(getClass().getResource("/Gui/BookDetail_GUI.fxml"));
							AnchorPane.getChildren().add(search);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				});
			}
		});
	}

	@FXML
	public void DetailSearchAction() {

		PrintWriter pw;
		try {
			pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8),
					true);
			ArrayList<String> ms = new ArrayList<>();// 0-제목, 1-작가, 2-출판사, 3-장르, 4-대여가능성
			if (tf_Title.getText().contains("-") || tf_Author.getText().contains("-")
					|| tf_Publisher.getText().contains("-")) {
				new Alert(Alert.AlertType.WARNING, "특수문자'-'를 사용하실 수 없습니다.", ButtonType.CLOSE).show();
			} else if (tf_Title.getText().contains(":") || tf_Author.getText().contains(":")
					|| tf_Publisher.getText().contains(":")) {
				new Alert(Alert.AlertType.WARNING, "특수문자':'를 사용하실 수 없습니다.", ButtonType.CLOSE).show();
			} else {

				if (tf_Title.getText().length() != 0)
					ms.add("Title-" + tf_Title.getText());
				if (tf_Author.getText().length() != 0)
					ms.add("Auther-" + tf_Author.getText());
				if (tf_Publisher.getText().length() != 0)
					ms.add("Publisher-" + tf_Publisher.getText());
				if (!genre.equals("선택안함"))
					ms.add("Genre-" + genre);// 나중에 바꿔야 함
				if (!book_condition.equals("선택안함"))
					ms.add("book_condition-" + book_condition);// 나중에 바꿔야 함

				if (!rentalAvailable.equals("선택안함")) {
					if (rentalAvailable.equals("가능")) {
						ms.add("Rental_Status-1");
					} else {
						ms.add("Rental_Status-0");// 불가능
					}
				}

				String request = "SearchBook:";
				for (int i = 0; i < ms.size(); i++) {
					if (i == 0) {
						request += ms.get(i);
					} else {
						request += ":" + ms.get(i);
					}

				}

				if (DataModel.ItemList_searchBook != null) {
					DataModel.ItemList_searchBook.clear();
				}
				pw.println(request);
				pw.flush();

				btn_DetailSearch.setDisable(true);

				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

				Platform.runLater(() -> {
					for (HBoxCell item : ItemList_searchBook) {

						item.title.setOnAction(new EventHandler<ActionEvent>() {
							@Override
							public void handle(ActionEvent evnet) {
								try {
									// item.num
									PrintWriter pw = new PrintWriter(
											new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8));
									pw.println("PrintBookData:Detail:" + item.num.getText());
									pw.flush(); // 책번호에 대한 정보를 달라고 요청
				
									Parent search = FXMLLoader.load(getClass().getResource("/Gui/BookDetail_GUI.fxml"));
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

				btn_DetailSearch.setDisable(false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
