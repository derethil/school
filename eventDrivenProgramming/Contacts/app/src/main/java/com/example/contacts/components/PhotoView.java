package com.example.contacts.components;

import android.content.Context;
import android.net.Uri;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.contacts.R;

public class PhotoSelector extends FrameLayout {
    AppCompatImageView imageView;
    String imageUri;
    public PhotoSelector(Context context, int height) {
        this(context, height, "");
    }

    public PhotoSelector(Context context, int height, String imageUri) {
        super(context);
        this.imageUri = imageUri;

        setBackgroundColor(getResources().getColor(R.color.colorDarkBackground, null));



        imageView = new AppCompatImageView(context);
        setImageUri(imageUri);

        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        imageView.setLayoutParams(params);

        addView(imageView);
    };

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
        if (imageUri.equals("")) {
            imageView.setImageResource(R.drawable.ic_baseline_person_240);
        } else {
            imageView.setImageURI(Uri.parse(imageUri));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
    }

    public String getImageUri() {
        return imageUri;
    }
}
