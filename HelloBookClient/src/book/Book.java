package book;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

public class Book {

	private int book_num; // 등록번호, 등록된 순으로 1씩 증가시킴.
	private String title; // 제목
	private String auther; // 작가
	private String publisher; // 출판사
	private String genre; // 장르
	private String book_condition; // 책 상태, 상 중 하
	private int full_price; // 정가
	private int sale_price; // 판매가
	private int lend_price; // 대여가
	private boolean rental_status; // 대여가능상태(가능하면 true)
	private String Introduction;

	public Book(String[] bookInfo) {
		this.book_num=Integer.parseInt(bookInfo[0]);
		this.title = bookInfo[1];
		this.auther = bookInfo[2];
		this.publisher = bookInfo[3];
		this.genre = bookInfo[4];
		this.book_condition = bookInfo[5];
		this.full_price = Integer.parseInt(bookInfo[6]);
		this.sale_price = Integer.parseInt(bookInfo[7]);
		this.lend_price = Integer.parseInt(bookInfo[8]);
		if (bookInfo[9].equals("1")||bookInfo[9].equals("true")) {// true
			bookInfo[9] = "true";
		} else {
			bookInfo[9] = "false";
		}
		this.rental_status = Boolean.parseBoolean(bookInfo[9]);
		this.Introduction = bookInfo[10];

	}

	public int getBook_num() {
		return book_num;
	}

	public String getTitle() {
		return title;
	}

	public String getAuther() {
		return auther;
	}

	public String getPublisher() {
		return publisher;
	}

	public String getGenre() {
		return genre;
	}

	public String getBook_condition() {
		return book_condition;
	}

	public int getFull_price() {
		return full_price;
	}

	public int getSale_price() {
		return sale_price;
	}

	public int getLend_price() {
		return lend_price;
	}

	public boolean getRental_status() {
		return rental_status;
	}

	public String getIntroduction() {
		return Introduction;
	}

	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		sb.append("{등록번호:" + book_num + "} {제목:" + title + "} {작가:" + auther + "} {출판사:" + publisher
				+  "} {장르:" + genre +"} {책의 상태:" + book_condition + "} {정가:" + full_price +  "} {판매가:" + sale_price +"} {대여가:" + lend_price +"} {대여가능여부:" + rental_status +"}]");

		return new String(sb);
	}

	public String getBookInfoTokens() {
		StringBuilder sb = new StringBuilder("[");
		sb.append("등록번호:" + book_num + ":제목:" + title + ":작가:" + auther + ":출판사:" + publisher
				+  ":장르:" + genre +":책의 상태:" + book_condition + ":정가:" + full_price +  ":판매가:" + sale_price +":대여가:" + lend_price +":대여가능여부:" + rental_status +":소개:"+Introduction+":]");

		return new String(sb);
	}
	
	public HBoxCell getBook() {
		return new HBoxCell(this.book_num, this.title, this.auther);
	}
	
	public static class HBoxCell extends HBox {
        Label num = new Label();
        Button title = new Button();
        Label author = new Label();

        HBoxCell(int book_num, String book_title, String book_author) {
             super();

             num.setText(book_num + "");
             num.setMaxWidth(Double.MAX_VALUE);
             HBox.setHgrow(num, Priority.ALWAYS);

             title.setText(book_title);
             author.setText(book_author);

             this.getChildren().addAll(num, title, author);
        }
   }

}
