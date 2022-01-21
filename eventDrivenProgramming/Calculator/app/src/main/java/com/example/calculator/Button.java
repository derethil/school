package com.example.calculator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.res.ResourcesCompat;

public class Button extends AppCompatButton {
    public Button(Context context, ButtonData data, View.OnClickListener onClick) {
        super(context);
        setText(data.getButtonText());

        setOnTouchListener((view, motionEvent) -> {
            int color = data.getType() == ButtonData.ButtonType.INPUT ? R.color.colorInput : R.color.colorOperator;
            int resourceColor = getResources().getColor(color, null);
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(getResources().getColor(color, null));
                drawable.setStroke(2, Color.BLACK);
                setBackground(drawable);
                view.performClick();
                onClick.onClick(view);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                GradientDrawable drawable = new GradientDrawable();
                drawable.setColor(resourceColor);
                drawable.setStroke(2, Color.BLACK);
                setBackground(drawable);
            }
            return true;
        });

        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(data.getRow(), 1, 1);
        params.columnSpec = GridLayout.spec(data.getColumn(), data.getSize(), 1);

        GradientDrawable drawable = new GradientDrawable();
        int color = data.getType() == ButtonData.ButtonType.INPUT ? R.color.colorInput : R.color.colorOperator;
        drawable.setColor(ResourcesCompat.getColor(getResources(), color, null));
        drawable.setStroke(2, Color.BLACK);

        setTextSize(32);
        setTextColor(Color.WHITE);
        setBackground(drawable);
        setLayoutParams(params);
    }
}
