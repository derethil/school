package com.example.trees;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.example.trees.views.LabelledInput;

import java.util.ArrayList;
import java.util.Arrays;

public class ConstraintsInputActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        LabelledInput minAngle = new LabelledInput(this, "Min Angle", "-50");
        LabelledInput maxAngle = new LabelledInput(this, "Max Angle", "50");
        LabelledInput minLength = new LabelledInput(this, "Min Length", "200");
        LabelledInput maxLength = new LabelledInput(this, "Max Length", "200");
        LabelledInput minTrunkWidth = new LabelledInput(this, "Min Trunk Width", "50");
        LabelledInput maxTrunkWidth = new LabelledInput(this, "Max Trunk Width", "50");

        new ArrayList<>(Arrays.asList(minAngle, maxAngle, minLength, maxLength, minTrunkWidth, maxTrunkWidth)).forEach(mainLayout::addView);

        AppCompatButton button = new AppCompatButton(this);
        button.setText("Draw Tree");
        button.setOnClickListener(view -> {
            Intent intent = new Intent(this, DrawTreeActivity.class);
            intent.putExtra("minAngle", minAngle.getText().toString());
            intent.putExtra("maxAngle", maxAngle.getText().toString());
            intent.putExtra("minLength", minLength.getText().toString());
            intent.putExtra("maxLength", maxLength.getText().toString());
            intent.putExtra("minTrunkWidth", minTrunkWidth.getText().toString());
            intent.putExtra("maxTrunkWidth", maxTrunkWidth.getText().toString());
            startActivity(intent);
        });

        mainLayout.addView(button);
        setContentView(mainLayout);
    }
}