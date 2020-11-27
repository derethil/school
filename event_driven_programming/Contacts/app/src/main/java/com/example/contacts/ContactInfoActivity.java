package com.example.contacts;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.contacts.components.ContactInfo;
import com.example.contacts.models.Contact;
import com.example.contacts.presenters.ContactInfoPresenter;

public class ContactInfoActivity extends BaseActivity implements ContactInfoPresenter.MVPView{
    LinearLayout mainLayout;
    ContactInfoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new ContactInfoPresenter(this);
        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        setContentView(mainLayout);
    }

    @Override
    public int getContactId() {
        Intent intent = getIntent();
        return intent.getIntExtra("contactId", -1);
    }

    @Override
    public void renderContactInfo(Contact contact) {
        runOnUiThread(() -> {
            ContactInfo contactInfo = new ContactInfo(this, contact);
            mainLayout.addView(contactInfo);
        });
    }
}
