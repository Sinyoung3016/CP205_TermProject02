package server;

public enum ServerRequest {
	SIGN_UP("SignUp"),LOG_IN("LogIn"),LOG_OUT("LogOut"),
	MODIFY_USER_DATA("ModifyUserData"),PRINT_USER_DATA("PrintUserData"),
	SEARCH_BOOK("SearchBook"),PRINT_BOOK_LIST("PrintBookList"),
	ADD_BOOK_DATA("AddBookData"),REMOVE_BOOK_DATA("RemoveBookData"),
	PRINT_BOOK_DATA("PrintBookData"),PURCHASE_REQUEST("PurchaseRequest"),PURCHASE_ANSWER("PurchaseAnswer"),
	BORROW_REQUEST("BorrowRequest"), BORROW_ANSWER("BorrowAnswer"),ALERT_OK("AlterOK"),PRINT_NEW_ALTER("printNewAlter"),
	RETURN_BOOK("ReturnBook"),LATE_IN_RETURN("LateInReturn"),CHAT("Chat");
	
	private String request;
	ServerRequest(String request){
		this.request=request;
	}
	public String getRequest() {
		return request;
	}
	
}
