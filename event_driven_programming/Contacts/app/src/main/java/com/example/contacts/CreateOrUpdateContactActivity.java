package com.example.contacts;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;

import com.example.contacts.components.MaterialInput;
import com.example.contacts.components.PhotoView;
import com.example.contacts.models.Contact;
import com.example.contacts.presenters.NewContactPresenter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewContactActivity extends BaseActivity implements NewContactPresenter.MVPView {
    NewContactPresenter presenter;
    PhotoView photoView;
    MaterialInput nameInput;
    MaterialInput phoneInput;
    MaterialInput emailInput;
    String currentPhotoPath = "";

    private static final int PICK_IMAGE = 1;
    private static final int TAKE_IMAGE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        presenter = new NewContactPresenter(this);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        photoView = new PhotoView(this, 640);

        AppCompatImageView iconView = new AppCompatImageView(this);
        iconView.setImageResource(R.drawable.ic_baseline_camera_alt_24);
        FrameLayout.LayoutParams paramsIcon = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsIcon.setMargins(0,0,24,24);
        paramsIcon.gravity = (Gravity.END | Gravity.BOTTOM);
        iconView.setLayoutParams(paramsIcon);
        photoView.addView(iconView);

        photoView.setOnClickListener((view) -> {
            new MaterialAlertDialogBuilder(this)
                    .setTitle("Choose Image")
                    .setItems(new CharSequence[]{"From Camera", "From Photos"}, (photoView, i) -> {
                        if (i == 0) {
                            presenter.handleTakePicturePressed();
                        } else {
                            presenter.handleSelectPicturePressed();
                        }
                    })
                    .show();
        });

        nameInput = new MaterialInput(this, "Name", R.drawable.ic_baseline_person_24);
        phoneInput = new MaterialInput(this, "Phone", R.drawable.ic_baseline_local_phone_24);
        emailInput = new MaterialInput(this, "Email", R.drawable.ic_baseline_email_24);

        MaterialButton saveButton = new MaterialButton(this);
        saveButton.setText("Save");
        saveButton.setOnClickListener(view -> {
            presenter.saveContact(
                    nameInput.getText().toString(),
                    phoneInput.getText().toString(),
                    emailInput.getText().toString(),
                    photoView.getImageUri()
            );
        });

        MaterialButton cancelButton = new MaterialButton(this, null, R.attr.borderlessButtonStyle);
        cancelButton.setText("Cancel");
        cancelButton.setOnClickListener((view) -> {
            presenter.handleCancelPressed();
        });

        LinearLayout buttonsLayout = new LinearLayout(this);
        buttonsLayout.setGravity(Gravity.RIGHT);
        buttonsLayout.setPadding(48,0,48,0);
        buttonsLayout.addView(cancelButton);
        buttonsLayout.addView(saveButton);

        mainLayout.addView(photoView);
        mainLayout.addView(nameInput);
        mainLayout.addView(phoneInput);
        mainLayout.addView(emailInput);
        mainLayout.addView(buttonsLayout);

        setContentView(mainLayout);
    }

    @Override
    public void goBackToContactsPage(Contact newContact) {
        if (newContact == null) { setResult(Activity.RESULT_CANCELED, null);
        } else {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("result", newContact);
            setResult(Activity.RESULT_OK, resultIntent);
        }
        finish();
    }

    @Override
    public void goToPhotos() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void takePicture() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jgp";

        File imageFile = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), imageFileName);
        currentPhotoPath = imageFile.getAbsolutePath();

        Uri photoUri = FileProvider.getUriForFile(this, "com.example.contacts.fileprovider", imageFile);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
        startActivityForResult(intent, TAKE_IMAGE);
    }

    @Override
    public void displayImage(String uri) {
        photoView.setImageUri(uri);
    }

    @Override
    public void displayNameError() {
        runOnUiThread(() -> {
            Snackbar.make(nameInput, "Name cannot be blank", Snackbar.LENGTH_SHORT).show();
            nameInput.setErrorEnabled(true);
            nameInput.setError("Name cannot be blank");
        });
    }

    @Override
    public void displayPhoneError() {
        runOnUiThread(() -> {
            Snackbar.make(phoneInput, "Phone number cannot be blank", Snackbar.LENGTH_SHORT).show();
            phoneInput.setErrorEnabled(true);
            phoneInput.setError("Phone number cannot be blank");
        });
    }

    @Override
    public void displayEmailError() {
        runOnUiThread(() -> {
            Snackbar.make(emailInput, "Email is not valid", Snackbar.LENGTH_SHORT).show();
            emailInput.setErrorEnabled(true);
            emailInput.setError("Email is not valid");
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == Activity.RESULT_OK) {
            String uri = data.getData().toString();
            presenter.handlePictureSelected(uri);
        }

        if (requestCode == TAKE_IMAGE && resultCode == Activity.RESULT_OK) {
            presenter.handlePictureSelected(currentPhotoPath);
        }
    }
}
