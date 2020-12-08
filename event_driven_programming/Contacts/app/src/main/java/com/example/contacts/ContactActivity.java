package com.example.contacts;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.contacts.components.ContactCard;
import com.example.contacts.components.ContactName;
import com.example.contacts.components.PhotoView;
import com.example.contacts.models.Contact;
import com.example.contacts.presenters.ContactInfoPresenter;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ContactInfoActivity extends BaseActivity implements ContactInfoPresenter.MVPView{
    FrameLayout mainLayout;
    LinearLayout contactLayout;
    ContactInfoPresenter presenter;
    Contact contact;

    private static final int REQUEST_PHONE_PERMISSIONS = 0;
    private static final int REQUEST_SMS_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new ContactInfoPresenter(this);
        mainLayout = new FrameLayout(this);

        contactLayout = new LinearLayout(this);
        contactLayout.setOrientation(LinearLayout.VERTICAL);

        FloatingActionButton changeContactButton = new FloatingActionButton(this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(0,24,24,0);
        params.gravity = (Gravity.TOP | Gravity.RIGHT);
        changeContactButton.setLayoutParams(params);
        changeContactButton.setImageResource(R.drawable.ic_baseline_more_vert_24);

        PopupMenu popupMenu = new PopupMenu(this, changeContactButton);
        popupMenu.getMenu().add("Edit");
        popupMenu.getMenu().add("Delete");

        changeContactButton.setOnClickListener(view -> {
            popupMenu.show();
        });

        popupMenu.setOnMenuItemClickListener((item) -> {
            if (item.getTitle().toString().equals("Edit")) {
                System.out.println("edit");
            }

            if (item.getTitle().toString().equals("Delete")) {
                presenter.handleDeleteContactPressed(contact);
            }
            return false;
        });

        mainLayout.addView(changeContactButton);

        mainLayout.addView(contactLayout);
        setContentView(mainLayout);
    }

    @Override
    public int getContactId() {
        Intent intent = getIntent();
        return intent.getIntExtra("contactId", -1);
    }

    @Override
    public void goBackToContactsPage(Contact deletedContact, boolean didDelete) {
        Intent intent = new Intent();
        if (didDelete) {
            intent.putExtra("id", (long) deletedContact.id);
            setResult(ContactsActivity.DELETED_RESULT, intent);
        } else {
            setResult(Activity.RESULT_CANCELED, null);
        }
        finish();
    }

    @Override
    public void makeCall(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_PERMISSIONS);
        }
    }

    @Override
    public void sendMessage(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            Intent smsIntent = new Intent(Intent.ACTION_VIEW);
            smsIntent.setData(Uri.parse("sms:" + phoneNumber));
            startActivity(smsIntent);
        } else {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSIONS);
        }

    }

    @Override
    public void sendEmail(String email) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:" + email));
        startActivity(emailIntent);
    }

    @Override
    public void renderContactInfo(Contact contact) {
        this.contact = contact;
        runOnUiThread(() -> {
            PhotoView photoView = new PhotoView(this, 960, contact.uri);
            photoView.addView(new ContactName(this, contact.name));

            ContactCard contactCard = new ContactCard(
                    this,
                    contact,
                    () -> { presenter.handlePhonePressed(contact.phone); },
                    () -> { presenter.handleMessagePressed(contact.phone); },
                    () -> { presenter.handleEmailPressed(contact.email); }
            );

            contactLayout.addView(photoView);
            contactLayout.addView(contactCard);
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PHONE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.handlePhonePressed(contact.phone);
            } else {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Call Failed")
                        .setMessage("This app does not have permission to make phone calls.")
                        .setPositiveButton("Okay", null)
                        .show();
            }
        }

        if (requestCode == REQUEST_SMS_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                presenter.handleMessagePressed(contact.phone);
            } else {
                new MaterialAlertDialogBuilder(this)
                        .setTitle("Message Failed")
                        .setMessage("This app does not have permission to send SMS messages.")
                        .setPositiveButton("Okay", null)
                        .show();
            }
        }

    }
}
