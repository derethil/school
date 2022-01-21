package com.example.madlib;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.lang.reflect.Array;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final LinearLayout layout = new LinearLayout(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        // Adjective
        AppCompatTextView adjectiveView = new AppCompatTextView(this);
        adjectiveView.setText("Enter an adjective:");
        final AppCompatEditText editAdjective = new AppCompatEditText(this);
        editAdjective.setLayoutParams(params);

        // Animal
        AppCompatTextView animalView = new AppCompatTextView(this);
        animalView.setText("Enter an animal:");
        final AppCompatEditText editAnimal = new AppCompatEditText(this);
        editAnimal.setLayoutParams(params);

        // Name
        AppCompatTextView nameView = new AppCompatTextView(this);
        nameView.setText("Enter a name:");
        final AppCompatEditText editName = new AppCompatEditText(this);
        editName.setLayoutParams(params);

        // Location
        AppCompatTextView locationView = new AppCompatTextView(this);
        locationView.setText("Enter a location:");
        final AppCompatEditText editLocation = new AppCompatEditText(this);
        editLocation.setLayoutParams(params);

        // Verb
        AppCompatTextView verbView = new AppCompatTextView(this);
        verbView.setText("Enter a past tense verb:");
        final AppCompatEditText editVerb = new AppCompatEditText(this);
        editVerb.setLayoutParams(params);

        // Phrase View
        final AppCompatTextView storyView = new AppCompatTextView(this);

        // Button
        AppCompatButton button = new AppCompatButton(this);
        button.setText("Generate MadLib");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String adjective = editAdjective.getText().toString();
                String animal = editAnimal.getText().toString();
                String name = editName.getText().toString();
                String location = editLocation.getText().toString();
                String verb = editVerb.getText().toString();

                String storyText = String.format("One day, a %s %s named %s existed. It was walking to %s. It quickly got lost and %s before falling over and dying.",
                        adjective, animal, name, location, verb);
                storyView.setText(storyText);

                layout.removeAllViews();
                layout.addView(storyView);
            }
        });

        layout.addView(adjectiveView);
        layout.addView(editAdjective);
        layout.addView(animalView);
        layout.addView(editAnimal);
        layout.addView(nameView);
        layout.addView(editName);
        layout.addView(locationView);
        layout.addView(editLocation);
        layout.addView(verbView);
        layout.addView(editVerb);

        layout.addView(button);

        layout.setOrientation(LinearLayout.VERTICAL);

        setContentView(layout);
    }
}