package com.example.contacts.presenters;

import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;

public class NewContactPresenter {
    MVPView view;
    AppDatabase database;

    public interface MVPView extends BaseMVPView {
        void goBackToContactsPage(Contact contact);
        void goToPhotos();
        void takePicture();
        void displayImage(String uri);
        void displayNameError();
        void displayPhoneError();
        void displayEmailError();
    }

    public NewContactPresenter(MVPView view) {
        this.view = view;
        database = view.getContextDatabase();
    }

    public void handleCancelPressed() { view.goBackToContactsPage(null); }

    public void handleSelectPicturePressed() { view.goToPhotos(); }

    public void handleTakePicturePressed() { view.takePicture(); }

    public void handlePictureSelected(String uri) { view.displayImage(uri); }


    public void saveContact(String name, String phone, String email, String uri) {
        new Thread(() -> {
            if (name.equals("") || phone.equals("") || (!email.equals("") && !email.contains("@"))) {
                if (name.equals("")) view.displayNameError();
                if (phone.equals("")) view.displayPhoneError();
                if (!email.equals("") && !email.contains("@")) view.displayEmailError();
                return;
            }

            Contact newContact = new Contact();
            newContact.name = name;
            newContact.phone = phone;
            newContact.email = email;
            newContact.uri = uri;
            newContact.id = (int) database.getContactDao().createContact(newContact);
            view.goBackToContactsPage(newContact);
        }).start();
    }
}
