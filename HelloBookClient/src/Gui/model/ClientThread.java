package Gui.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import book.Book;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;

public class ClientThread  extends Thread{
        Socket socket = null;

        public ClientThread(Socket socket){//생성자
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
            	BufferedReader br=null;
            	
            	br=DataModel.br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            	DataModel.ItemList_newBook=FXCollections.observableArrayList();
             	DataModel.ItemList_myBook=FXCollections.observableArrayList();
                while(true) {
                    String msg = br.readLine();

                    String[] tokens=msg.split(":");
                    if(tokens[0].equals("NewBook")) {
                
                    	String[] newBookData=new String[11];
                    	newBookData[0]=tokens[2];
                    	for(int i=1; i<11; i++) {
                    		newBookData[i]=tokens[i*2+2];
                    	}
                    	Platform.runLater(() -> { DataModel.addNewBook(new Book(newBookData));});

                    	
                    }
                    else if (tokens[0].equals("AddBookData")) {
 
                    	Platform.runLater(() -> { Alert alert = new Alert(AlertType.INFORMATION);
                    	alert.setTitle("Success");
                    	alert.setHeaderText("업로드 성공!");
                    	alert.setContentText("확인을 눌러주세요");
                    	alert.show();});

    				}
                    else if (tokens[0].equals("PrintBookList")) {
                    	if(tokens[1].equals("[등록번호")) {
                    		String[] newBookData=new String[11];
                    		newBookData[0]=tokens[2];
                    		for(int i=1; i<11; i++) {
                    			newBookData[i]=tokens[i*2+2];
                    		}
                    		Platform.runLater(() -> { DataModel.addMyBookList(new Book(newBookData).toString());});
                    	}
                    	else {
                    		Platform.runLater(() -> { DataModel.addMyBookList(tokens[1]);});
                    	}
    				}
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (Exception e) {
            	
            }
        }

}
