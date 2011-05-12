package com.qh.contact.google;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import list.BaseContactList;
import list.ContactUser;

import com.google.gdata.client.Query;
import com.google.gdata.client.contacts.ContactsService;
import com.google.gdata.data.contacts.ContactEntry;
import com.google.gdata.data.contacts.ContactFeed;
import com.google.gdata.data.extensions.Email;
import com.google.gdata.util.AuthenticationException;

public class MailGoogleContactList extends BaseContactList {


	public MailGoogleContactList(String userName, String password, String mailServer) {
		super(userName, password, mailServer);
	}

    private String getUsername(String email) {
        return email.split("@")[0];
    }

	@Override
	public List<ContactUser> getContactUser() {
		
		ContactsService service = new ContactsService("contactlist");
        try {
            service.setUserCredentials(getEmail(), getPassword());
        } catch (AuthenticationException e) {
        	e.printStackTrace();
        }
        try {
            URL feedUrl = new URL("http://www.google.com/m8/feeds/contacts/" + getEmail() + "/full");
            Query query = new Query(feedUrl);
            query.setMaxResults(Integer.MAX_VALUE);
            ContactFeed resultFeed = service.query(query, ContactFeed.class);
            List<ContactUser> contacts = new ArrayList<ContactUser>();
            for (ContactEntry entry : resultFeed.getEntries()) {
                for (Email email : entry.getEmailAddresses()) {
                    String address = email.getAddress();
                    String name = null;
                    if (entry.hasName()) {
                        name = entry.getName().getFullName().getValue();
                    } else {
                        name = getUsername(address);
                    }
                    contacts.add(new ContactUser(name, name, address));
                }
            }
            return contacts;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
	}
}
