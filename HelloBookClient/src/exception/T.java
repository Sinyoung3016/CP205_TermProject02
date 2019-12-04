package exception;

public class T {

	public static void main(String[] args) {
		try {
			System.out.println(FormException.BookFormCheck("ddd¤·"));
		} catch (MyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
