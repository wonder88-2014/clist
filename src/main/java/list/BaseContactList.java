package list;

import java.util.List;

import junit.framework.Assert;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;

public abstract class BaseContactList {
	
	public BaseContactList(String userName, String password, String mailServer) {
		super();
		this.userName = userName;
		this.password = password;
		this.mailServer = mailServer;
	}

	private String userName;
	private String password;
	private String mailServer;

	/**
	 * 获取联系人
	 * @param userName
	 * @param password
	 * @param mailServer
	 * @return
	 */
	public abstract List<ContactUser> getContactUser();

	/**
	 * 
	 * @param client
	 */
	public void printCookie(HttpClient client) {

		Assert.assertNotNull("httpclient is null",client);
		
		Cookie[] cookies = client.getState().getCookies();
		System.out.println("BaseContactList.printCookie()" + cookies.length);
		for (Cookie cookie : cookies) {
			System.out.println(cookie);
		}
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMailServer() {
		return mailServer;
	}

	public void setMailServer(String mailServer) {
		this.mailServer = mailServer;
	}
	
	public String getEmail() {
		return userName+"@"+mailServer;
	}
}


