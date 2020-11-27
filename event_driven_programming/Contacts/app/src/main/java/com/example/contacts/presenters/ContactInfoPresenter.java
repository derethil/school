package com.example.contacts.presenters;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;

public class ContactInfoPresenter {
    MVPView view;
    AppDatabase database;
    Contact contact;

    public interface MVPView extends BaseMVPView {
        public void renderContactInfo(Contact contact);
        public int getContactId();
    }

    public ContactInfoPresenter(MVPView view) {
        this.view = view;
        database = view.getContextDatabase();
        loadContactInfo();
    }

    public void loadContactInfo() {
        new Thread(() -> {
            int id = view.getContactId();
            contact = database.getContactDao().getContact(id);
            view.renderContactInfo(contact);
        }).start();
    }
}
