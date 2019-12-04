package Gui;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import book.Book;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LoanedBook_Controller extends Base_Controller implements Initializable {

	public Button btn_Back,  btn_Comfirm, btn_Return, btn_LateinReturn;
	public Label lb_WhoBorrorwed, lb_Title, lb_Author, lb_Publisher, lb_BookCondition, lb_FullPrice, lb_SalePrice,
			lb_LendPrice, lb_Introduction;
	private Book book;
	@FXML
	public AnchorPane AnchorPane;
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		this.book = DataModel.book_for_loaned;
		super.base();
		// Base end

		
		lb_WhoBorrorwed.setText(DataModel.who_borrow_book);
		lb_Title.setText(book.getTitle());
		lb_Author.setText(book.getAuther());
		lb_Publisher.setText(book.getPublisher());
		lb_BookCondition.setText(book.getBook_condition());
		lb_FullPrice.setText(book.getFull_price()+"");
		lb_SalePrice.setText(book.getSale_price()+"");
		lb_LendPrice.setText(book.getLend_price()+"");
		lb_Introduction.setText(book.getIntroduction());
		
		
	}

	public void backAction() { //전 화면으로 
		AnchorPane root=(AnchorPane) AnchorPane.getScene().getRoot();
		root.getChildren().remove(AnchorPane);
	}

	public void comfirmAction() {
		AnchorPane root=(AnchorPane) AnchorPane.getScene().getRoot();
		root.getChildren().remove(AnchorPane);
	}

	public void returnAction() {		
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "이 책을 반납처리 하시겠습니까?", ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.YES) {
			try {
				PrintWriter pw=new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));
				pw.println("ReturnBook:"+DataModel.ID+":"+this.book.getBook_num()+":"+this.book.getTitle());//ReturnBook:요청자:책번호,책제목
				pw.flush();
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
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

	public void lateinreturnAction() { 
		Alert alert = new Alert(Alert.AlertType.WARNING, "대여자가 대여불가 상태가 됩니다. \n대여자를 대여불가 상태로 만드시겠습니까?",ButtonType.YES, ButtonType.NO);
		Optional<ButtonType> result = alert.showAndWait();
		if (result.get() == ButtonType.YES) {
			// 빌린 사람의 lb_Rent를 false로
			try {
			PrintWriter pw=new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(),StandardCharsets.UTF_8));
			pw.println("LateInReturn:"+lb_WhoBorrorwed.getText());//LateInReturn:대여불가로 바뀔 ID
			pw.flush();
			new Alert(Alert.AlertType.INFORMATION, "대여불가로 만들었습니다.", ButtonType.OK).show();;
			}catch(IOException e) {
				e.printStackTrace();
			}
			
		}
	}
}
