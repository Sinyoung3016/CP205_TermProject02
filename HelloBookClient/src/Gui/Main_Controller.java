package Gui;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import Gui.thread.ClientThread;
import book.Book.HBoxCell;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Main_Controller extends Base_Controller implements Initializable {

	@FXML
	public Button btn_Left, btn_Right;
	@FXML
	public ImageView iv_ad,iv_BestSeller;
	@FXML
	public Label lb_adExplain;
	@FXML
	public ListView lv_NewBooks;
	
	@FXML
	public Button btn_chat;
	
	public static DataModel dataModel;

	@FXML
	public TextField tf_chat;
	@FXML
	public ListView<Text> lv_chat;
	
	private ArrayList<Image> ad_images = new ArrayList<>();
	private int ad_count;
	private ObservableList<HBoxCell> ItemList_newBook;
	private ObservableList<Text> chatList;


	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
	

		DataModel.detail_book=null;
		if(!DataModel.is_thread_on) {
			dataModel=new DataModel();
			new ClientThread(DataModel.socket,lv_chat).start();
			DataModel.is_thread_on=true;
			try {
				Thread.sleep(100);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		
		tf_chat.setOnKeyPressed(event -> {
			if (event.getCode().equals(KeyCode.ENTER)) {
		        	chatting();
		        }
		    });
		
		// Base start
		super.base();
		// Base end
		
		chatList=DataModel.chatList;
		lv_chat.setItems(chatList);

		lv_chat.scrollTo(chatList.size());
			
		ItemList_newBook = DataModel.ItemList_newBook;
		for (HBoxCell item : ItemList_newBook) {
			item.title.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent evnet) {
					try {
						// item.num
						PrintWriter pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8));
						pw.println("PrintBookData:Detail:" + item.num.getText());
						pw.flush(); // 책번호에 대한 정보를 달라고 요청

						Parent search = FXMLLoader.load(getClass().getResource("/Gui/BookDetail_GUI.fxml"));
						AnchorPane.getChildren().add(search);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}

		lv_NewBooks.setItems(ItemList_newBook);// 이 아래 위치에 Listner를 추가해줘야 잘 작동됌!!!
		
		lv_NewBooks.getItems().addListener(new ListChangeListener() {// 새롭게 추가될때마다 해줌!!
			@Override
			public void onChanged(ListChangeListener.Change change) {
				if (ItemList_newBook.size() != 0) {

					HBoxCell hbc = (HBoxCell) lv_NewBooks.getItems().get(0);
					hbc.title.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent evnet) {
							try {
								// item.num
								PrintWriter pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8));
								pw.println("PrintBookData:Detail:" + hbc.num.getText());
								pw.flush(); // 책번호에 대한 정보를 달라고 요청
					

								Parent search = FXMLLoader.load(getClass().getResource("/Gui/BookDetail_GUI.fxml"));
								AnchorPane.getChildren().add(search);
						
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					});
				}
			}
		});

		File dirFile = new File(".\\image\\advertisement");
		File[] fileList = dirFile.listFiles();

		if (fileList != null) {
			for (File tempFile : fileList) {

				if (tempFile.isFile()) {
					Image image = new Image(tempFile.toURI().toString());
					ad_images.add(image);

				}
			}
			iv_ad.setPreserveRatio(false);
			/*
			 * this.ad=new Image[ad_images.size()]; int count=0; for(Image i:ad_images) {
			 * ad[count++]=i; } System.out.println("??");
			 */
			new chageImageThread().start();

		
		}
	}

	private class chageImageThread extends Thread {
		@Override
		public void run() {
			Timeline tl_table = new Timeline();
			KeyValue kv = new KeyValue(iv_ad.translateXProperty(), 0);
			KeyValue kv1 = new KeyValue(btn_Right.opacityProperty(), 1);
			KeyFrame kf = new KeyFrame(Duration.millis(3000), kv);
			KeyFrame kf1 = new KeyFrame(Duration.millis(3500), kv1);

			tl_table.getKeyFrames().add(kf);
			tl_table.getKeyFrames().add(kf1);

			if (ad_images.size() != 0) {

				ad_count = 0;
				while (DataModel.socket != null) {
					if (ad_count >= ad_images.size() - 1) {
						ad_count = -1;
					}
					iv_ad.setTranslateX(30);
					btn_Right.setOpacity(0);
					iv_ad.setImage(ad_images.get(++ad_count));
					tl_table.play();
					try {
						sleep(4000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		}
	}
	@FXML
	public void chatting() {
		if(tf_chat.getText().length()!=0) {
			PrintWriter pw;
			try {
				pw = new PrintWriter(new OutputStreamWriter(DataModel.socket.getOutputStream(), StandardCharsets.UTF_8));
				pw.println("Chat:" +DataModel.user.getName()+"("+DataModel.ID+"):"+ tf_chat.getText());
				pw.flush(); 
				tf_chat.setText("");
				lv_chat.scrollTo(chatList.size());
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		}
		
	}
	


	@FXML
	public void goLeftAction() {
		// 왼쪽으로 이동하며 다른 adPicture 보여주기
		Timeline tl_table = new Timeline();
		KeyValue kv = new KeyValue(iv_ad.translateXProperty(), 0);
		KeyValue kv1 = new KeyValue(btn_Right.opacityProperty(), 1);
		KeyFrame kf = new KeyFrame(Duration.millis(3000), kv);
		KeyFrame kf1 = new KeyFrame(Duration.millis(3500), kv1);

		tl_table.getKeyFrames().add(kf);
		tl_table.getKeyFrames().add(kf1);

		if (ad_count <= 0) {
			ad_count = ad_images.size();
		}
		iv_ad.setTranslateX(30);
		btn_Right.setOpacity(0);
		iv_ad.setImage(ad_images.get(--ad_count));
		tl_table.play();

	}

	@FXML
	public void goRightAction() {
		// 오른쪽으로 이동하며 다른 adPicture 보여주기
		Timeline tl_table = new Timeline();
		KeyValue kv = new KeyValue(iv_ad.translateXProperty(), 0);
		KeyValue kv1 = new KeyValue(btn_Right.opacityProperty(), 1);
		KeyFrame kf = new KeyFrame(Duration.millis(3000), kv);
		KeyFrame kf1 = new KeyFrame(Duration.millis(3500), kv1);

		tl_table.getKeyFrames().add(kf);
		tl_table.getKeyFrames().add(kf1);

		if (ad_count >= ad_images.size() - 1) {
			ad_count = -1;
		}
		iv_ad.setTranslateX(30);
		btn_Right.setOpacity(0);
		iv_ad.setImage(ad_images.get(++ad_count));
		tl_table.play();
	}

}
