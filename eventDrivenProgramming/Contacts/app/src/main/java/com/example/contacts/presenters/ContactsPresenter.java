package com.example.contacts.presenters;


import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;

import java.util.ArrayList;

public class ContactsPresenter {
    MVPView view;
    AppDatabase database;
    ArrayList<Contact> contacts = new ArrayList<>();

    public interface MVPView extends BaseMVPView {
        public void renderContact(Contact contact);
        public void goToNewContactPage();
        public void goToContactInfoPage(int id);
    }

    public ContactsPresenter(MVPView view) {
        this.view = view;
        database = view.getContextDatabase();
        loadContacts();
    }

    public void handleNewContactClick() {
        new Thread(() -> {
            view.goToNewContactPage();
        }).start();
    }

    public void handleContactClick(int id) {
        new Thread(() -> {
            view.goToContactInfoPage(id);
        }).start();
    }

    public void loadContacts() {
        new Thread(() -> {
            contacts = (ArrayList<Contact>) database.getContactDao().getContacts();
            contacts.forEach(contact -> view.renderContact(contact));
        }).start();
    }

    public void onNewContactCreated(Contact contact) {
        contacts.add(contact);
        view.renderContact(contact);
    }
}
