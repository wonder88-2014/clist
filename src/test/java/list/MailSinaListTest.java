package list;

import java.util.List;

import list.mails.MailSinaContactList;

import org.junit.Assert;
import org.junit.Test;

public class MailSinaListTest {

	BaseContactList bcl = new MailSinaContactList("contactuser", "testtest", "sina.com");


	@Test
	public void test163GetContactList2() {
			List<ContactUser> userList = bcl.getContactUser();
			Assert.assertTrue(userList.size() > 0);
			System.out.println("MailListTest.testGetContactList2()" + userList);
	}
}
