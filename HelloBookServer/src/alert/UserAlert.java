package alert;

public class UserAlert {
	private String Requester_ID;
	private int Book_Number;
	private String Book_Title;
	private String Requested_ID;
	private String Request_Status;
	private boolean is_Treatmented;
	
	public UserAlert(String Requester_ID,String Book_Number, String Book_Title,String Requested_ID,String Request_Status,String is_Treatmented) {
		this.Requester_ID=Requester_ID;
		this.Book_Number=Integer.parseInt(Book_Number);
		this.Book_Title=Book_Title;
		this.Requested_ID=Requested_ID;
		this.Request_Status=Request_Status;
		if(is_Treatmented.equals("1")) {
			this.is_Treatmented=true;
		}else {
			this.is_Treatmented=false;
		}
		
	}
	
	public UserAlert(String[] token) {
		this.Requester_ID=token[0];
		this.Book_Number=Integer.parseInt(token[1]);
		this.Book_Title=token[2];
		this.Requested_ID=token[3];
		this.Request_Status=token[4];
		if(token[5].equals("1")||token[5].equals("true")) {
			this.is_Treatmented=true;
		}else {
			this.is_Treatmented=false;
		}
	}
	
	
	public String getRequester_ID() {
		return Requester_ID;
	}
	public int getBook_Number() {
		return Book_Number;
	}
	public String getBook_Title() {
		return Book_Title;
	}
	public String getRequested_ID() {
		return Requested_ID;
	}
	public String getRequest_Status() {
		return Request_Status;
	}
	public boolean is_Treatmented() {
		return is_Treatmented;
	}
	
	public String getToken() {
		String token=this.Requester_ID+":"+this.Book_Number+":"+this.Book_Title+":"+this.Requested_ID+":"+this.Request_Status+":"+this.is_Treatmented;
		return token;
		
 	}
	
	
}