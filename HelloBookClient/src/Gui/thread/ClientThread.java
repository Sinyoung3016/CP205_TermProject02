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
       
        public ClientThread(Socket socket,ListView<Text> lv_chat){//������
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
                    	if(!DataModel.is_exist_new_book) {//�����ٰ� ����� ��ϵ� å�� �����ϴٸ� ����ֱ� ����
                    		Platform.runLater(() -> {DataModel.ItemList_newBook.clear();});
                    		DataModel.is_exist_new_book=true;
                    	}
                    	
                    	if(tokens[1].equals("��Ϲ�ȣ")) {//å�� �����ϴ� ��Ȳ
                    		String[] newBookData=new String[11];
                    		newBookData[0]=tokens[2];
                    		
                    		for(int i=1; i<11; i++) {newBookData[i]=tokens[i*2+2];}
                    		
                    		Platform.runLater(() -> { DataModel.addNewBook(new Book(newBookData));});
                    	}
                    	else {//å�� �������� �ʴ� ��Ȳ
                    		Platform.runLater(() -> { DataModel.NoNewBook(tokens[1]);});//��ϵ� å�� �����ϴٰ� ����
                    		DataModel.is_exist_new_book=false;
                    	}
                    	
                    	//NewBook end
                    	
                    }else  if(tokens[0].equals("RemoveNewBook")) {           	

                    	Platform.runLater(() -> { DataModel.removeNewBook(tokens[1]);});
           
                    	
                    	//NewBook end
                    	
                    } else if (tokens[0].equals("AddBookData")) {
 
                    	Platform.runLater(() -> { Alert alert = new Alert(AlertType.INFORMATION);
                    	alert.setTitle("Success");
                    	alert.setHeaderText("���ε� ����!");
                    	alert.setContentText("Ȯ���� �����ּ���");
                    	alert.show();});

                    	//AddBookData end
                    	
    				} else if (tokens[0].equals("PrintBookList")) {
                    	if(tokens[1].equals("��Ϲ�ȣ")) {
                    		String[] newBookData=new String[11];
                    		newBookData[0]=tokens[2];
                    		for(int i=1; i<11; i++) 		{newBookData[i]=tokens[i*2+2];}
                    		Platform.runLater(() -> { DataModel.addMyBookList(new Book(newBookData));});
                    	}
                    	else {
                    		System.out.println("����");
                    		Platform.runLater(() -> { DataModel.NoMyBookList(tokens[1]);});//��ϵ� å�� �����ϴٰ� ����
                    	}
                    
                    	//PrintBookList end
                    	
                    	
    				} else if (tokens[0].equals("PrintBookData")) {//PrintBookData:(Registered,Detail,Loaned):[��Ϲ�ȣ:~~~
    						
    					if(tokens[2].equals("å�� �������� �ʽ��ϴ�.")) {
    						DataModel.detail_book=null;
    					}else {
    						if(tokens[1].equals("Detail")) {
                    			String mergeToken="";
                    			for(int i=3; i<tokens.length; i+=2) 		{mergeToken+=tokens[i]+":";}//tokens[0]��printBookData,tokens[1] �� [��Ϲ�ȣ, �������� ]��
                    			Main_Controller.dataModel.setBookDetail(new Book(mergeToken));
                    			
                    			 if(tokens.length==25) {//�������� �������� �������� ���ȴ��� ���� 25
                    				 DataModel.borrowed_form_who=tokens[24];
                    			}
                    		                  		
                    	}
    						else if(tokens[1].equals("Registered")) {
    	                    	
                    			String mergeToken="";
                    			for(int i=3; i<tokens.length; i+=2)		 {mergeToken+=tokens[i]+":";}
                    			Main_Controller.dataModel.setBookDetail(new Book(mergeToken));
                    	
                    			 if(tokens.length==25) {//���������� �������� �������� ��������� ���� 25
                    				 DataModel.who_borrwed_book=tokens[24];
                    			}
                    		
                    	}
    						else if(tokens[1].equals("Loaned")) {
                        		
                    			String mergeToken="";
                    			for(int i=3; i<tokens.length; i+=2) {mergeToken+=tokens[i]+":";}//0��printBookData, 1�� [��Ϲ�ȣ, �������� ]��
                    			Main_Controller.dataModel.setBookDetail(new Book(mergeToken));
                    			DataModel.who_borrwed_book=tokens[24];
                    		
                    	}
    					}
    						
                  
   
                    } 
                    else if (tokens[0].equals("SearchBookList")) {
                    	if(tokens[1].equals("��Ϲ�ȣ")) {
                    		String[] newBookData=new String[11];
                    		newBookData[0]=tokens[2];
                    		for(int i=1; i<11; i++) 		{newBookData[i]=tokens[i*2+2];}
                    		
                    		Platform.runLater(() -> { DataModel.addSearchBookList(new Book(newBookData));});
                    	}
                    	else 
                    		Platform.runLater(() -> { DataModel.NoSearchBookList(tokens[1]);});//��ϵ� å�� �����ϴٰ� ����
                    	
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
                    	Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();});//�뿩��û�� ���½��ϴ� 	
                    	//BorrowRequest end
    				}
                    
                    else if (tokens[0].equals("ReturnBook")) {
                    	Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();});
                    	//ReturnBook end
    				}
                    else if (tokens[0].equals("PurchaseRequest")) {
                    	Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();}); //���ſ�û�� ���½��ϴ� 	             	
                    	//PurchaseRequest end
    				}else if (tokens[0].equals("ModifyUserData")) {
    					if(tokens[1].equals("ȸ�������� ����Ǿ����ϴ�.")) {
    						Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();}); 
    						DataModel.user=new User(tokens[2]);
    					}else {
    						Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();}); 
    					}
                    	//ModifyUserData end
    				}  else if (tokens[0].equals("RemoveBookData")) {
                    	Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, tokens[1], ButtonType.CLOSE).show();}); //���ſ�û�� ���½��ϴ� 	             	
                    	//RemoveBookData end
    				} 
    				else if (tokens[0].equals("LateInReturn")) {
                    	if(tokens[1].equals("Good")) {//�뿩�������� �ٲ��
                    		DataModel.user.setLend_OK(true);
                    		Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, "å�� �ݳ��Ǿ� �뿩���� ���°� �Ǿ����ϴ�.", ButtonType.CLOSE).show();}); //���ſ�û�� ���½��ϴ� 	         
                    	}else {
                    		DataModel.user.setLend_OK(false);
                    		Platform.runLater(() -> { 	new Alert(Alert.AlertType.INFORMATION, "å�� �ݳ����� �ʾ� �뿩�Ұ� ���°� �Ǿ����ϴ�", ButtonType.CLOSE).show();}); //���ſ�û�� ���½��ϴ� 	         
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
    					Platform.runLater(() -> {DataModel.addChat(("����: "+Message),lv_chat);});
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
    					Platform.runLater(() -> {new Alert(Alert.AlertType.INFORMATION, "������ �������ϴ�.��������˴ϴ�.", ButtonType.CLOSE).show();});
    					Thread.sleep(2000);
    					System.exit(0);
    					//ShutDown end
                    }
                    
                }
                
            }
            
            catch (IOException e) {
            	if(e.getMessage().equals("Connection reset")) {
            		Platform.runLater(() -> {new Alert(Alert.AlertType.INFORMATION, "������ �������ϴ�.��������˴ϴ�.", ButtonType.CLOSE).show();});
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
            	System.out.println("Ŭ���̾�Ʈ ������ ����");
            }
        }
 

}
