package com.example.contacts.components;

import android.content.Context;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatTextView;

import com.example.contacts.models.Contact;

public class ContactInfo extends LinearLayout {
    public ContactInfo(Context context, Contact contact) {
        super(context);

        setOrientation(VERTICAL);

        LinearLayout.LayoutParams nameparams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        nameparams.setMargins(48, 48,48,48);

        AppCompatTextView nameView = new AppCompatTextView(context);
        nameView.setText(contact.name);
        nameView.setTextSize(32);
        nameView.setLayoutParams(nameparams);
        nameView.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams infoParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        infoParams.setMargins(48, 24,24,24);

        AppCompatTextView phoneView = new AppCompatTextView(context);
        phoneView.setText("Call: " + contact.phone);
        phoneView.setTextSize(24);
        phoneView.setLayoutParams(infoParams);

        AppCompatTextView emailView = new AppCompatTextView(context);
        emailView.setText("Email: " + contact.email);
        emailView.setTextSize(24);
        emailView.setLayoutParams(infoParams);

        addView(nameView);
        addView(phoneView);
        addView(emailView);
    }
}
