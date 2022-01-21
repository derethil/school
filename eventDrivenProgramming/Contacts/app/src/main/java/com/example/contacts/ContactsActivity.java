package com.example.contacts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

import com.example.contacts.components.ContactListItem;
import com.example.contacts.database.AppDatabase;
import com.example.contacts.models.Contact;
import com.example.contacts.presenters.ContactsPresenter;

public class ContactsActivity extends BaseActivity implements ContactsPresenter.MVPView {
    LinearLayout mainLayout;
    LinearLayout contactsLayout;
    ContactsPresenter presenter;
    private final int CREATE_NEW_CONTACT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new ContactsPresenter(this);

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        contactsLayout = new LinearLayout(this);
        contactsLayout.setOrientation(LinearLayout.VERTICAL);

        ScrollView scrollView = new ScrollView(this);
        scrollView.addView(contactsLayout);

        AppCompatButton newContactButton = new AppCompatButton(this);
        newContactButton.setText("New Contact");
        newContactButton.setOnClickListener(view -> {
            presenter.handleNewContactClick();
        });

        mainLayout.addView(newContactButton);
        mainLayout.addView(scrollView);

        setContentView(mainLayout);
    }

    @Override
    public void renderContact(Contact contact) {
        runOnUiThread(() -> {
            ContactListItem listItem = new ContactListItem(this, contact);

            listItem.setOnClickListener(view -> {
                presenter.handleContactClick(contact.id);
            });

            contactsLayout.addView(listItem);
        });
    }

    @Override
    public void goToNewContactPage() {
        Intent intent = new Intent(this, NewContactActivity.class);
        startActivityForResult(intent, CREATE_NEW_CONTACT);
    }

    @Override
    public void goToContactInfoPage(int id) {
        Intent intent = new Intent(this, ContactInfoActivity.class);
        intent.putExtra("contactId", id);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_NEW_CONTACT && resultCode == Activity.RESULT_OK) {
            Contact newContact = (Contact) data.getSerializableExtra("result");
            presenter.onNewContactCreated(newContact);
        }
    }
}