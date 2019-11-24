package server;

public enum ServerRequest {
	SIGN_UP("SignUp"),SIGN_OUT("SingOut"),LOG_IN("LogIn"),LOG_OUT("LogOut"),
	MODIFY_USER_DATA("ModifyUserData"),PRINT_USER_DATA("PrintUserData"),
	SEARCH_BOOK("SearchBook"),PRINT_BOOK_LIST("PrintBookList"),
	ADD_BOOK_DATA("AddBookData"),MODIFY_BOOK_DATA("ModifyBookData"),DELETE_BOOK_DATA("DeleteBookData"),
	PRINT_BOOK_DATA("PrintBookData"),PURCHASE_BOOK("PurchaseBook"),RENTAL_BOOK("RentalBook");
	
	private String request;
	ServerRequest(String request){
		this.request=request;
	}
	public String getRequest() {
		return request;
	}
	
}
