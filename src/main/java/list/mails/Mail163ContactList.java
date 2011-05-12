package list.mails;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import list.BaseContactList;
import list.ContactUser;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class Mail163ContactList extends BaseContactList {

	public Mail163ContactList(String userName, String password, String mailServer) {
		super(userName, password, mailServer);
	}

	public String getContactList() {
		HttpClient client = new HttpClient();

		List<Header> headers = new ArrayList<Header>();
		headers.add(new Header("User-Agent",
				"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9) Gecko/2008052906 Firefox/3.0"));
		client.getHostConfiguration().getParams().setParameter("http.default-headers", headers);

		String getListURL = login(client, headers);

		/**
		 * 获取列表
		 */
		PostMethod gm = new PostMethod(getListURL);
		RequestEntity requestEntity;
		try {
			requestEntity = new StringRequestEntity(
					"<?xml version=\"1.0\"?><object><array name=\"items\"><object><string name=\"func\">pab:searchContacts</string><object name=\"var\"><array name=\"order\"><object><string name=\"field\">FN</string><boolean name=\"ignoreCase\">true</boolean></object></array></object></object><object><string name=\"func\">user:getSignatures</string></object><object><string name=\"func\">pab:getAllGroups</string></object></array></object>",
					"application/xml", "UTF-8");
			gm.setRequestEntity(requestEntity);
			gm.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
			client.executeMethod(gm);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		StringBuffer sb = new StringBuffer();
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(gm.getResponseBodyAsStream()));
			String read = "";
			while ((read = br.readLine()) != null) {
				sb.append(read);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	/**
	 * 登录
	 * @param client
	 * @param headers
	 * @return
	 */
	private String login(HttpClient client, List<Header> headers) {
		/**
		 * 获取认证cookie
		 */
		PostMethod post = new PostMethod(getAuthURL(getUserName(), getPassword(), getMailServer()));
		post.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		try {
			client.executeMethod(post);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		post.releaseConnection();

		/**
		 * 获取sid
		 */
		PostMethod postRe = new PostMethod(getRedURL(getUserName()));
		postRe.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
		try {
			client.executeMethod(postRe);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		postRe.releaseConnection();
		String sid = getSid(postRe);

		headers.add(new Header("Accept", "text/javascript"));
		headers.add(new Header("Content-Type", "application/xml; charset=UTF-8"));
		String getListURL = getActiveMailServer(postRe) + "js3/s?func=global:sequential&sid=" + sid;
		return getListURL;
	}

	private String getActiveMailServer(PostMethod postRe) {
		Header h = postRe.getResponseHeader("Location");
		String loc = h.getValue();
		Pattern p = Pattern.compile("(.*.mail.163.com/).*");
		Matcher m = p.matcher(loc);
		String baseUrl = "";
		if (m.find()) {
			baseUrl = m.group(1);
		}
		return baseUrl;
	}

	public List<ContactUser> getContactUser() {
		JSONObject obj = JSONObject.fromObject(this.getContactList());
		JSONArray ja = obj.getJSONArray("var").getJSONObject(0).getJSONArray("var");
		List<ContactUser> userList = new ArrayList<ContactUser>();
		for (int i = 0; i < ja.size(); i++) {
			JSONObject contact = ja.getJSONObject(i);
			if (contact.containsKey("id")) {
				ContactUser cu = new ContactUser(contact.getString("FN"), contact.containsKey("N") ? contact
						.getString("N") : "", contact.getString("EMAIL;PREF"));
				userList.add(cu);
			} else
				continue;
		}
		return userList;
	}

	private String getSid(PostMethod postRe) {
		Header h = postRe.getResponseHeader("Location");
		String loc = h.getValue();
		Pattern p = Pattern.compile(".*sid=(.*)");
		Matcher m = p.matcher(loc);
		String sid = "";
		if (m.find()) {
			sid = m.group(1);
		}
		return sid;
	}

	private String getRedURL(String userName) {
		StringBuffer redURL = new StringBuffer(
				"http://entry.mail.163.com/coremail/fcg/ntesdoor2?lightweight=1&verifycookie=1&language=-1&from=web&style=-1")
				.append("&username=" + userName);
		return redURL.toString();
	}

	private String getAuthURL(String userName, String password, String mailServer) {
		StringBuffer authURL = new StringBuffer(
				"http://reg.163.com/login.jsp?type=1&product=mail163&url=http://entry.mail.163.com/coremail/fcg/ntesdoor2?lightweight%3D1%26verifycookie%3D1%26language%3D-1%26from%3Dweb%26style%3D-1&url2=http%3A%2F%2Femail.163.com%2Ferrorpage%2Ferr_163.htm&rmbUser=on")
				.append("&username=" + userName + "@" + mailServer).append("&user=" + userName).append(
						"&password=" + password);
		return authURL.toString();
	}

}
