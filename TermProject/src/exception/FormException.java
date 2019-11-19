package exception;

import java.util.regex.Pattern;

public class FormException {
	
	public static boolean IDFormCheck(String id) throws MyException {//¾ÆÀÌµð´Â ¿À·ÎÁö ¿µ¾î¿Í ¼ýÀÚ·Î¸¸
		if(Pattern.matches("^[a-zA-Z0-9]*$", id)&&id.length()!=0) {
			return true;//Çü½Ä¿¡ ¸Â´Ù¸é
		}
		throw new MyException("ID does not match format");
	}
	
	public static boolean passwordFormCheck(String id) throws MyException{//ºñ¹Ð¹øÈ£´Â ¿µ¾î, ¼ýÀÚ ,(!@#$%^&*())ÀÇ Æ¯¼ö¹®ÀÚ°¡ Çã¿ë.
		if(Pattern.matches("^[a-zA-Z0-9!@#$%^&*()]*$", id)&&id.length()!=0)
			return true;
		
		throw new MyException("Password does not match format");
	}
	
	public static boolean NameFormCheck(String id) throws MyException{//ÀÌ¸§Àº ÇÑ±Û°ú ¿µ¾î¸¸ °¡´É.
		if(Pattern.matches("^[a-zA-Z°¡-ÆR]*$", id)&&id.length()!=0)
			return true;

		throw new MyException("Name does not match format");
	}
	
	public static boolean phoneFormCheck(String id) throws MyException {//ÇÚµåÆù¹øÈ£ Çü½ÄÀº 000-000(0)-0000
		if(Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", id)&&id.length()!=0)
			return true;
		
		throw new MyException("Phone does not match format");
	}

}
