package com.example.calculator;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.widget.GridLayout;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.res.ResourcesCompat;

public class ExpressionView extends AppCompatTextView {
    public ExpressionView(Context context) {
        super(context);
        GridLayout.LayoutParams params = new GridLayout.LayoutParams();
        params.rowSpec = GridLayout.spec(0, 1, 1);
        params.columnSpec = GridLayout.spec(0, 3, 1);
        setLayoutParams(params);
        setTextSize(24);
        setTextColor(Color.WHITE);

        GradientDrawable drawable = new GradientDrawable();
        drawable.setColor(ResourcesCompat.getColor(getResources(), R.color.colorInput, null));
        drawable.setStroke(2, Color.BLACK);
        setBackground(drawable);

        setGravity(Gravity.CENTER);
        setEnabled(false);
    }
}

