package Gui.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import Gui.Base_Controller;
import alter.UserAlter;
import book.Book;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import user.User;

public class ClientThread  extends Thread{
        Socket socket = null;

        public ClientThread(Socket socket){//생성자
            this.socket = socket;
        }
        @Override
        public void run() {
            try {
            	BufferedReader br=null;
            	
            	//br=DataModel.br = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            	br= new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            	DataModel.ItemList_newBook=FXCollections.observableArrayList();
             	DataModel.ItemList_myBook=FXCollections.observableArrayList();
             	DataModel.ItemList_searchBook=FXCollections.observableArrayList();
             	DataModel.ItemList_alter=FXCollections.observableArrayList();
             	
                while(true) {
                    String msg = br.readLine();
                    String[] tokens=msg.split(":");
                    if(tokens[0].equals("NewBook")) {           	
                    	if(!DataModel.is_exist_new_book) {//없다가 생기면 등록된 책이 없습니다를 비워주기 위해
                    		Platform.runLater(() -> {DataModel.ItemList_newBook.clear();});
                    		DataModel.is_exist_new_book=true;
                    	}
                    	
                    	if(tokens[1].equals("등록번호")) {//책이 존재하는 상황
                    		String[] newBookData=new String[11];
                    		newBookData[0]=tokens[2];
                    		
                    		for(int i=1; i<11; i++) {newBookData[i]=tokens[i*2+2];}
                    		
                    		Platform.runLater(() -> { DataModel.addNewBook(new Book(newBookData));});
                    	}
                    	else {//책이 존재하지 않는 상황
                    		Platform.runLater(() -> { DataModel.NoNewBook(tokens[1]);});//등록된 책이 없음니다가 저장
                    		DataModel.is_exist_new_book=false;
                    	}
                    	
                    	//NewBook end
                    	
                    }else  if(tokens[0].equals("RemoveNewBook")) {           	

                    		
                    	Platform.runLater(() -> { DataModel.removeNewBook(tokens[1]);});
           
                    	
                    	//NewBook end
                    	
                    } else if (tokens[0].equals("AddBookData")) {
 
                    	Platform.runLater(() -> { Alert alert = new Alert(AlertType.INFORMATION);
                    	alert.setTitle("Success");
                    	alert.setHeaderText("업로드 성공!");
                    	alert.setContentText("확인을 눌러주세요");
                    	alert.show();});

                    	//AddBookData end
                    	
    				} else if (tokens[0].equals("PrintBookList")) {
                    	if(tokens[1].equals("등록번호")) {
                    		String[] newBookData=new String[11];
                    		newBookData[0]=tokens[2];
                    		for(int i=1; i<11; i++) 		{newBookData[i]=tokens[i*2+2];}
                    		Platform.runLater(() -> { DataModel.addMyBookList(new Book(newBookData));});
                    	}
                    	else 
                    		Platform.runLater(() -> { DataModel.NoMyBookList(tokens[1]);});//등록된 책이 없습니다가 저장
                    	
                    	//PrintBookList end
    				} else if (tokens[0].equals("PrintBookData")) {//PrintBookData:(Registered,Detail,Loaned):[등록번호:~~~
    							
                    	if(tokens[1].equals("Detail")) {
                    		if(tokens[2].equals("책이 존재하지 않습니다.")) 
                    			DataModel.book_for_detail=null;
                    		else {
                    			String mergeToken="";
                    			for(int i=3; i<tokens.length; i+=2) 		{mergeToken+=tokens[i]+":";}//tokens[0]은printBookData,tokens[1] 은 [등록번호, 마지막은 ]임
                    			DataModel.book_for_detail=new Book(mergeToken);
                    			System.out.println(tokens.length);
                    			System.out.println(tokens[tokens.length-1]);
                    			 if(tokens.length==25) {//빌렸으면 마지막에 누구에게 빌렸는지 떠서 25
                    				 DataModel.borrowed_form_who=tokens[24];
                    			}
                    		}                    		
                    	}
                    	else if(tokens[1].equals("Registered")) {
                    		if(tokens[2].equals("책이 존재하지 않습니다.")) 
                    			DataModel.book_for_registered=null;
                    		else {
                    			String mergeToken="";
                    			for(int i=3; i<tokens.length; i+=2)		 {mergeToken+=tokens[i]+":";}
                    			DataModel.book_for_registered=new Book(mergeToken);
                    	
                    			 if(tokens.length==25) {//빌려줬으면 마지막에 누구에게 빌려줬는지 떠서 25
                    				 DataModel.who_borrwed_book=tokens[24];
                    			}
                    		}
                    	}
                    	else if(tokens[1].equals("Loaned")) {
                    		if(tokens[2].equals("책이 존재하지 않습니다.")) //책이 없을때
                    			DataModel.book_for_loaned=null;
                    		else {
                    			String mergeToken="";
                    			for(int i=3; i<tokens.length; i+=2) {mergeToken+=tokens[i]+":";}//0은printBookData, 1은 [등록번호, 마지막은 ]임
                    			DataModel.book_for_loaned=new Book(mergeToken);
                    			DataModel.who_borrwed_book=tokens[24];
                    		}
                    	}

                    	//PrintBookData end
                    } 
                    else if (tokens[0].equals("SearchBookList")) {
                    	if(tokens[1].equals("등록번호")) {
                    		String[] newBookData=new String[11];
                    		newBookData[0]=tokens[2];
                    		for(int i=1; i<11; i++) 		{newBookData[i]=tokens[i*2+2];}
                    		
                    		Platform.runLater(() -> { DataModel.addSearchBookList(new Book(newBookData));});
                    	}
                    	else 
                    		Platform.runLater(() -> { DataModel.NoSearchBookList(tokens[1]);});//등록된 책이 없습니다가 저장
                    	
                    	//SearchBookList end
    				} 

                    else if (tokens[0].equals("Alter")) {
                    	String[] alterData=new String[6];
                    	alterData[0]=tokens[1];
                		for(int i=1; i<6; i++) 		{alterData[i]=tokens[i+1];}
                		
                    	UserAlter user_alter=new UserAlter(alterData);
                    	Platform.runLater(() -> { DataModel.addAlter(user_alter);});
                    	
                    	//Alter end
    				}
                    
                    
                    else if (tokens[0].equals("BorrowRequest")) {
                    	Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();});//대여요청을 보냈습니다 	
                    	//BorrowRequest end
    				}
                    
                    else if (tokens[0].equals("ReturnBook")) {
                    	Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();});
                    	//ReturnBook end
    				}
                    else if (tokens[0].equals("PurchaseRequest")) {
                    	Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();}); //구매요청을 보냈습니다 	             	
                    	//PurchaseRequest end
    				}else if (tokens[0].equals("ModifyUserData")) {
    					if(tokens[1].equals("회원정보가 변경되었습니다.")) {
    						Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();}); 
    						DataModel.user=new User(tokens[2]);
    					}else {
    						Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();}); 
    					}
                    	//ModifyUserData end
    				}  else if (tokens[0].equals("RemoveBookData")) {
                    	Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();}); //구매요청을 보냈습니다 	             	
                    	//RemoveBookData end
    				} 
    				else if (tokens[0].equals("LateInReturn")) {
                    	if(tokens[1].equals("Good")) {//대여가능으로 바뀌면
                    		DataModel.user.setLend_OK(true);
                    		Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, "책이 반납되어 대여가능 상태가 되었습니다.", ButtonType.CLOSE).show();}); //구매요청을 보냈습니다 	         
                    	}else {
                    		DataModel.user.setLend_OK(false);
                    		Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, "책을 반납하지 않아 대여불가 상태가 되었습니다", ButtonType.CLOSE).show();}); //구매요청을 보냈습니다 	         
                    	}
                    	
                    	//lateInReturn e d
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
