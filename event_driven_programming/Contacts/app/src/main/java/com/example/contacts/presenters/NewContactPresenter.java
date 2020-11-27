package com.example.contacts.presenters;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;

public class NewContactPresenter {
    MVPView view;
    AppDatabase database;

    public interface MVPView extends BaseMVPView {
        public void goBackToContactsPage(Contact contact);
    }

    public NewContactPresenter(MVPView view) {
        this.view = view;
        database = view.getContextDatabase();
    }

    public void saveContact(String name, String phone, String email) {
        new Thread(() -> {
            Contact newContact = new Contact();
            newContact.name = name;
            newContact.phone = phone;
            newContact.email = email;
            newContact.id = (int) database.getContactDao().createContact(newContact);
            view.goBackToContactsPage(newContact);
        }).start();
    }
}
