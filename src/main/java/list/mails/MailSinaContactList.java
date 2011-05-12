package list.mails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import list.BaseContactList;
import list.ContactUser;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;

public class MailSinaContactList extends BaseContactList {
	
	public MailSinaContactList(String userName, String password, String mailServer) {
		super(userName, password, mailServer);
	}

	@Override
	public List<ContactUser> getContactUser() {
		List<ContactUser> retv = null;
		HttpClient client = new HttpClient();
		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9) Gecko/2008052906 Firefox/3.0"));
		client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);
		
		PostMethod pm = new PostMethod("http://login.sina.com.cn/sso/prelogin.php?entry=freemail&callback=sinaSSOController.preloginCallBack&user=contactuser%40sina.com&client=ssologin.js(v1.3.12)&_=1305009643671");
		pm.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		
		try {
			client.executeMethod(pm);
			System.out.println("MailSinaContactList.getContactList()" + new String(pm.getResponseBody()));
			pm.releaseConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		PostMethod pm2 = new PostMethod("http://login.sina.com.cn/sso/login.php?client=ssologin.js(v1.3.12)&service=sso&client=ssologin.js%28v1.3.12%29&entry=freemail&encoding=UTF-8&gateway=1&savestate=0&from=&useticket=0&username=contactuser%40sina.com&servertime=1305012508&nonce=26XUB9&pwencode=wsse&password=17af87282caeb44041665ffbbdf12debfbfd76dd&callback=parent.sinaSSOController.loginCallBack&returntype=IFRAME&setdomain=1");
		pm2.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		headers.add(new Header("Content-Type", "application/x-www-form-urlencoded"));
		try {
			client.executeMethod(pm2);
			System.out.println("MailSinaContactList.getContactList()" + new String(pm2.getResponseBody()));
			pm.releaseConnection();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		this.printCookie(client);
		
		return retv;
	}

}
