package com.example.contacts.presenters;

import android.content.Intent;
import android.net.Uri;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;

public class ContactInfoPresenter {
    MVPView view;
    AppDatabase database;
    Contact contact;

    public interface MVPView extends BaseMVPView {
        void renderContactInfo(Contact contact);
        int getContactId();
        void goBackToContactsPage(Contact contact, boolean didDelete);
        void makeCall(String phoneNumber);
        void sendMessage(String phoneNumber);
        void sendEmail(String email);
    }

    public void handleEditContactPressed() {
    }

    public void handleDeleteContactPressed(Contact contactToDelete) {
        new Thread(() -> {
            database.getContactDao().deleteContact(contactToDelete);
            view.goBackToContactsPage(contactToDelete,true);
        }).start();
    }

    public void handlePhonePressed(String phoneNumber) { view.makeCall(phoneNumber); }

    public void handleMessagePressed(String phoneNumber) { view.sendMessage(phoneNumber); }

    public void handleEmailPressed(String email) { view.sendEmail(email); }

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
