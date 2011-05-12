package list;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.qh.contact.google.MailGoogleContactList;

public class MailGoogleTest {

	BaseContactList bcl = new MailGoogleContactList("thoughtmiracle", "qihe1982", "gmail.com");

	@Test
	public void test163GetContactList2() {
			List<ContactUser> userList = bcl.getContactUser();
			Assert.assertTrue(userList.size() > 0);
			System.out.println("MailListTest.testGetContactList2()" + userList);
	}
}
