package Gui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import Gui.model.DataModel;
import book.Book;
import book.Book.HBoxCell;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Main_Controller extends Base_Controller implements Initializable {
	private ArrayList<Image> ad_images=new ArrayList<>();
	private Image[] ad_arr;
	@FXML
	public Button btn_Left, btn_Right;
	@FXML
	public ImageView iv_ad;
	@FXML
	public Label lb_adExplain;
	@FXML
	public ListView lv_NewBooks, lv_BestSeller;
	private int ad_count;
	private ObservableList<HBoxCell> ItemList_newBook;


	private Image[] ad = {

	};

	private Image[] user = {

	};

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// Base start
		
		super.base();
		ItemList_newBook=DataModel.ItemList_newBook;
		lv_NewBooks.setItems(ItemList_newBook);
		
		File dirFile=new File("src\\Gui\\advertisement");
		File []fileList=dirFile.listFiles();
		
		if(fileList.length>0) {
			for(File tempFile : fileList) {

				if(tempFile.isFile()) {
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

	private class chageImageThread extends Thread{
		@Override
		public void run() {
			if(ad_images.size()!=0) {

				ad_count=0;
				while(DataModel.socket!=null) {
					if(ad_count>=ad_images.size()-1) {
						ad_count=-1;
					}
					iv_ad.setImage(ad_images.get(++ad_count));
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
	public void goLeftAction() {
		// 왼쪽으로 이동하며 다른 adPicture 보여주기  
		
		if(ad_count<=0) {
			ad_count=ad_images.size();
		}
		iv_ad.setImage(ad_images.get(--ad_count));
	
	
	}

	@FXML
	public void goRightAction() {
		// 오른쪽으로 이동하며 다른 adPicture 보여주기
		if(ad_count>=ad_images.size()-1) {
			ad_count=-1;
		}
		iv_ad.setImage(ad_images.get(++ad_count));

	}


}
