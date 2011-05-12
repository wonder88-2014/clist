package list;

public class ContactUser {
	
	/**
	 * first name
	 */
	private String fn;
	/**
	 * name
	 */
	private String n;
	private String email;
	
	public ContactUser(String fn, String n, String email) {
		super();
		this.fn = fn;
		this.n = n;
		this.email = email;
	}
	public String getFn() {
		return fn;
	}
	public void setFn(String fn) {
		this.fn = fn;
	}
	public String getN() {
		return n;
	}
	public void setN(String n) {
		this.n = n;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "ContactUser [email=" + email + ", fn=" + fn + ", n=" + n + "]";
	}

}
