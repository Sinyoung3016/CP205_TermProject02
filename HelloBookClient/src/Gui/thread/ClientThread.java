package Gui.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

import Gui.Base_Controller;
import Gui.Main_Controller;
import Gui.model.DataModel;
import alert.UserAlert;
import alter.UserAlter;
import book.Book;
import javafx.application.Platform;
import javafx.beans.binding.DoubleBinding;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.text.Text;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import user.User;

public class ClientThread  extends Thread{
        Socket socket = null;
        ListView<Text> lv_chat;
       
        public ClientThread(Socket socket,ListView<Text> lv_chat){//생성자
            this.socket = socket;
            this.lv_chat=lv_chat;
            
            DataModel.ItemList_newBook=FXCollections.observableArrayList();
         	DataModel.ItemList_myBook=FXCollections.observableArrayList();
         	DataModel.ItemList_searchBook=FXCollections.observableArrayList();
         	DataModel.ItemList_alter=FXCollections.observableArrayList();
         	DataModel.chatList=FXCollections.observableArrayList();
        }
        @Override
        public void run() {
            try {

            	BufferedReader br= new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            	
             	
                while(true) {
                    String msg = br.readLine();
                    String[] tokens=msg.split(":");
               
                    if(tokens[0].equals("NewBook")) {           	
                    	if(!DataModel.is_exist_new_book) {//없었다가 생기면 등록된 책이 없습니다를 비워주기 위해
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
                    	else {
                    		System.out.println("여기");
                    		Platform.runLater(() -> { DataModel.NoMyBookList(tokens[1]);});//등록된 책이 없습니다가 저장
                    	}
                    
                    	//PrintBookList end
                    	
                    	
    				} else if (tokens[0].equals("PrintBookData")) {//PrintBookData:(Registered,Detail,Loaned):[등록번호:~~~
    						
    					if(tokens[2].equals("책이 존재하지 않습니다.")) {
    						DataModel.detail_book=null;
    					}else {
    						if(tokens[1].equals("Detail")) {
                    			String mergeToken="";
                    			for(int i=3; i<tokens.length; i+=2) 		{mergeToken+=tokens[i]+":";}//tokens[0]은printBookData,tokens[1] 은 [등록번호, 마지막은 ]임
                    			Main_Controller.dataModel.setBookDetail(new Book(mergeToken));
                    			
                    			 if(tokens.length==25) {//빌렸으면 마지막에 누구에게 빌렸는지 떠서 25
                    				 DataModel.borrowed_form_who=tokens[24];
                    			}
                    		                  		
                    	}
    						else if(tokens[1].equals("Registered")) {
    	                    	
                    			String mergeToken="";
                    			for(int i=3; i<tokens.length; i+=2)		 {mergeToken+=tokens[i]+":";}
                    			Main_Controller.dataModel.setBookDetail(new Book(mergeToken));
                    	
                    			 if(tokens.length==25) {//빌려줬으면 마지막에 누구에게 빌려줬는지 떠서 25
                    				 DataModel.who_borrwed_book=tokens[24];
                    			}
                    		
                    	}
    						else if(tokens[1].equals("Loaned")) {
                        		
                    			String mergeToken="";
                    			for(int i=3; i<tokens.length; i+=2) {mergeToken+=tokens[i]+":";}//0은printBookData, 1은 [등록번호, 마지막은 ]임
                    			Main_Controller.dataModel.setBookDetail(new Book(mergeToken));
                    			DataModel.who_borrwed_book=tokens[24];
                    		
                    	}
    					}
    						
                  
   
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
                    	
                    	//lateInReturn end
    				}else if (tokens[0].equals("Notice")) {
    					String message="";
            			for(int i=1; i<tokens.length; i++) {
            				if(i==tokens.length-1) {
            					message+=tokens[i];
            				}else {
            				message+=tokens[i]+":";
            				}
            			}
            			String Message=message;
    					Platform.runLater(() -> {DataModel.addChat(("서버: "+Message),lv_chat);});
    					//Notice end
                    }else if (tokens[0].equals("Chat")) {
                    	String message="";
                    	String id=tokens[1];
            			for(int i=2; i<tokens.length; i++) {
            				if(i==tokens.length-1) {
            					message+=tokens[i];
            				}else {
            				message+=tokens[i]+":";
            				}
   
            			}
            			String Message=message;
            			Platform.runLater(() -> {DataModel.addChat((id+": "+Message),lv_chat);});
            			//Chat end
                    }
                    else if (tokens[0].equals("ShutDown")) {
    					Platform.runLater(() -> {new Alert(Alert.AlertType.INFORMATION, "서버가 닫혔습니다.강제종료됩니다.", ButtonType.CLOSE).show();});
    					Thread.sleep(2000);
    					System.exit(0);
    					//ShutDown end
                    }
                    
                }
                
            }
            
            catch (IOException e) {
            	if(e.getMessage().equals("Connection reset")) {
            		Platform.runLater(() -> {new Alert(Alert.AlertType.INFORMATION, "서버가 닫혔습니다.강제종료됩니다.", ButtonType.CLOSE).show();});
            		try {
						Thread.sleep(2000);
					} catch (InterruptedException e1) {
					}
					System.exit(0);
            	}
            	e.printStackTrace();
            } catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				//DataModel.is_thread_on=false;
            	System.out.println("클라이언트 쓰레드 종료");
            }
        }
 

}
