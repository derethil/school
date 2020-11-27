package com.example.contacts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ActionMenuView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import com.example.contacts.models.Contact;
import com.example.contacts.presenters.NewContactPresenter;

public class NewContactActivity extends BaseActivity implements NewContactPresenter.MVPView {
    NewContactPresenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new NewContactPresenter(this);

        AppCompatTextView nameLabel = new AppCompatTextView(this);
        nameLabel.setText("Name");
        AppCompatEditText editName = new AppCompatEditText(this);

        AppCompatTextView phoneLabel = new AppCompatTextView(this);
        phoneLabel.setText("Phone");
        AppCompatEditText editPhone = new AppCompatEditText(this);

        AppCompatTextView emailLabel = new AppCompatTextView(this);
        emailLabel.setText("Email");
        AppCompatEditText editEmail = new AppCompatEditText(this);

        AppCompatButton saveButton = new AppCompatButton(this);
        saveButton.setOnClickListener(view -> {
            presenter.saveContact(editName.getText().toString(), editPhone.getText().toString(), editEmail.getText().toString());
        });
        saveButton.setText("Save");

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.addView(nameLabel);
        mainLayout.addView(editName);
        mainLayout.addView(phoneLabel);
        mainLayout.addView(editPhone);
        mainLayout.addView(emailLabel);
        mainLayout.addView(editEmail);
        mainLayout.addView(saveButton);

        setContentView(mainLayout);
    }

    @Override
    public void goBackToContactsPage(Contact newContact) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("result", newContact);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
