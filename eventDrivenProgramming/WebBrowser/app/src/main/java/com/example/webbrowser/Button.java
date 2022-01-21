package com.example.webbrowser;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.appcompat.widget.AppCompatButton;

public class Button {
    public static AppCompatButton newButton(Context context, String text) {
        AppCompatButton newButton = new AppCompatButton(context);
        newButton.setText(text);

        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        buttonParams.height = 120;
        buttonParams.width = 120;
        newButton.setLayoutParams(buttonParams);

        return newButton;
    }
}
