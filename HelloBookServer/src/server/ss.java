package server;

import database.DB_USER;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ss extends Application {
	public void start( Stage primaryStage) throws Exception {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/server/dwq.fxml"));
			Scene scene = new Scene(root);
			primaryStage.setTitle("HelloBookServer");
			primaryStage.setOnCloseRequest(ActionEvent->{DB_USER.allUserLogOut();
			System.exit(0);
			}
			);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		launch(args);
	}
}
