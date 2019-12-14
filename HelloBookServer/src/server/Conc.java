package server;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.stage.Popup;

public class Conc {

	@FXML
	Button b;
	Popup pu = new Popup();

	
	public void ac() throws IOException {
		pu.getContent().add(FXMLLoader.load(getClass().getResource("ServerManage_GUI.fxml")));
		pu.setAutoHide(true);
		pu.show(b.getScene().getWindow());
		Thread t=new Thread(()->{System.out.println("1");});
		t.start();
	}
}
