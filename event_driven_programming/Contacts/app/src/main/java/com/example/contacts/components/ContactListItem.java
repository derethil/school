package com.example.contacts.components;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.contacts.models.Contact;

public class ContactListItem extends LinearLayout {
    public ContactListItem(Context context, Contact contact) {
        super(context);

        AppCompatTextView contactNameView = new AppCompatTextView(context);
        contactNameView.setText(contact.name);
        contactNameView.setTextSize(24);

        LayoutParams params = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(48,24,24,24);
        contactNameView.setLayoutParams(params);

        addView(contactNameView);
    }
}
