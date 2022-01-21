package com.example.contacts.components;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatImageView;

import com.example.contacts.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;

public class TwoActionItem extends LinearLayout {
    TextInputEditText input;
    public TwoActionItem(Context context, String contents, int firstIcon, int secondIcon) {
        super(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.setMargins(48, 24, 48, 24);
        setLayoutParams(params);

        AppCompatImageView phoneIcon = new AppCompatImageView(context);
        phoneIcon.setImageResource(firstIcon);
        phoneIcon.setColorFilter(getResources().getColor(R.color.colorIconColored));

        MaterialTextView phoneNumberView = new MaterialTextView(context);
        phoneNumberView.setText(contents);

        AppCompatImageView messageIcon = new AppCompatImageView(context);
        messageIcon.setImageResource(secondIcon);
        messageIcon.setColorFilter(getResources().getColor(R.color.colorIconColored));

        LinearLayout.LayoutParams messageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        messageParams.gravity = Gravity.RIGHT;
        messageIcon.setLayoutParams(messageParams);

        addView(phoneIcon);
        addView(phoneNumberView);
        addView(messageIcon);

    }
}
