package com.example.trees.views;

import android.content.Context;
import android.text.Editable;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

public class LabelledInput extends LinearLayout {
    private AppCompatEditText input;
    public LabelledInput(Context context, String labelText, String defaultValue) {
        super(context);
        setOrientation(LinearLayout.VERTICAL);

        AppCompatTextView label = new AppCompatTextView(context);
        label.setText(labelText);
        this.input = new AppCompatEditText(context);
        this.input.setText(defaultValue);

        addView(label);
        addView(input);
    }

    public Editable getText() {
        return input.getText();
    }

}
