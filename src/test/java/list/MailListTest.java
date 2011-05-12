package list;

import java.util.List;

import list.mails.Mail163ContactList;

import org.junit.Assert;
import org.junit.Test;

public class MailListTest {

	BaseContactList bcl = new Mail163ContactList("contactuser", "testtest", "163.com");

	@Test
	public void test163GetContactList2() {
			List<ContactUser> userList = bcl.getContactUser();
			Assert.assertTrue(userList.size() > 0);
			System.out.println("MailListTest.testGetContactList2()" + userList);
	}
}
