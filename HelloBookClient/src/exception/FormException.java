package exception;

import java.util.regex.Pattern;

public class FormException {
	
	public static boolean IDFormCheck(String id) throws MyException {//¾ÆÀÌµğ´Â ¿À·ÎÁö ¿µ¾î¿Í ¼ıÀÚ·Î¸¸
		if(Pattern.matches("^[a-zA-Z0-9]{1,20}$", id)&&id.length()!=0) {
			return true;//Çü½Ä¿¡ ¸Â´Ù¸é
		}
		throw new MyException("¿µ¾î¿Í ¼ıÀÚ¸¸ »ç¿ëÇÏ¿© 20±ÛÀÚ ÀÌÇÏ·Î ÀÛ¼ºÇÏ¼¼¿ä.");
	}
	
	public static boolean passwordFormCheck(String pw) throws MyException{//ºñ¹Ğ¹øÈ£´Â ¿µ¾î, ¼ıÀÚ ,(!@#$%^&*())ÀÇ Æ¯¼ö¹®ÀÚ°¡ Çã¿ë.
		if(Pattern.matches("^[a-zA-Z0-9!@#$%^&*()]{1,20}$", pw)&&pw.length()!=0)
			return true;
		
		throw new MyException("¿µ¾î, ¼ıÀÚ, Æ¯¼ö¹®ÀÚ¸¸ »ç¿ëÇÏ¿© 20±ÛÀÚ ÀÌÇÏ·Î ÀÛ¼ºÇÏ¼¼¿ä.");
	}
	
	public static boolean NameFormCheck(String name) throws MyException{//ÀÌ¸§Àº ÇÑ±Û°ú ¿µ¾î¸¸ °¡´É.
		if(Pattern.matches("^[a-zA-Z°¡-ÆR]{1,20}$", name)&&name.length()!=0)
			return true;

		throw new MyException("ÇÑ±Û°ú ¿µ¾î¸¸ »ç¿ëÇÏ¿© 20±ÛÀÚ ÀÌÇÏ·Î ÀÛ¼ºÇÏ¼¼¿ä.");
	}
	
	public static boolean phoneFormCheck(String phone) throws MyException {//ÇÚµåÆù¹øÈ£ Çü½ÄÀº 010-000(0)-0000
		if(Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", phone)&&phone.length()!=0)
			return true;
		
		throw new MyException("¹øÈ£¸¦ È®ÀÎÇØÁÖ¼¼¿ä.");
	}
	public static boolean emailFormCheck(String email) throws MyException {//emailÀº ¿µ¾î¿Í ¼ıÀÚ¸¸
		if(Pattern.matches("^[a-zA-Z0-9]{1,20}$", email)&&email.length()!=0)
			return true;
		
		throw new MyException("ÀÌ¸ŞÀÏ ÁÖ¼Ò¸¦ È®ÀÎÇØÁÖ¼¼¿ä.");
	}
	public static boolean AddressFormCheck(String name) throws MyException{//ÁÖ¼ÒÀº ÇÑ±Û°ú ¿µ¾î,¼ıÀÚ °¡´É.
		if(Pattern.matches("^[a-zA-Z°¡-ÆR0-9\\s]{1,100}$", name)&&name.length()!=0)
			return true;

		throw new MyException("ÇÑ±Û°ú ¿µ¾î,¼ıÀÚ¸¸ »ç¿ëÇÏ¿© 100±ÛÀÚ ÀÌÇÏ·Î ÀÛ¼ºÇÏ¼¼¿ä.");
	}
	
	public static boolean BookFormCheck(String name) throws MyException{//Á¦¸ñÀº ÇÑ±Û°ú ¿µ¾î¸¸ °¡´É.
		if(Pattern.matches("^[a-zA-Z°¡-ÆR0-9!@#$%^&*()\\s]{1,30}$", name)&&name.length()!=0) 
			return true;

		throw new MyException("ÇÑ±Û°ú ¿µ¾î, ¼ıÀÚ¿Í Æ¯¼ö¹®ÀÚ¸¸ »ç¿ëÇÏ¿© 30±ÛÀÚ ÀÌÇÏ·Î ÀÛ¼ºÇÏ¼¼¿ä.");
	}
	public static boolean BookIntroduceFormCheck(String name) throws MyException{//¼Ò°³ ÇÑ±Û°ú ¿µ¾î¸¸ °¡´É.
		if(Pattern.matches("^[a-zA-Z°¡-ÆR0-9!@#$%^&*()\\s]{1,100}$", name)&&name.length()!=0) 
			return true;

		throw new MyException("ÇÑ±Û°ú ¿µ¾î, ¼ıÀÚ¿Í Æ¯¼ö¹®ÀÚ¸¸ »ç¿ëÇÏ¿© 100±ÛÀÚ ÀÌÇÏ·Î ÀÛ¼ºÇÏ¼¼¿ä.");
	}
}
