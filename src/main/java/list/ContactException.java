package list;

public class ContactException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public ContactException(String message, Throwable cause){
		super(message, cause);
	}
	
	public ContactException(String message){
		super(message);
	}
}
