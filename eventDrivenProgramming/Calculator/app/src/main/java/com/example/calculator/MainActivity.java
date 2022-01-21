package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.GridLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<ButtonData> buttonData = new ArrayList<ButtonData>() {
        {
            add(new ButtonData("C", 0, 3, 1, ButtonData.ButtonType.CLEAR));
            add(new ButtonData("/", 1, 3, 1, ButtonData.ButtonType.OPERATOR));
            add(new ButtonData("*", 2, 3, 1, ButtonData.ButtonType.OPERATOR));
            add(new ButtonData("-", 3, 3, 1, ButtonData.ButtonType.OPERATOR));
            add(new ButtonData("+", 4, 3, 1, ButtonData.ButtonType.OPERATOR));
            add(new ButtonData("7", 1, 0, 1));
            add(new ButtonData("8", 1, 1, 1));
            add(new ButtonData("9", 1, 2, 1));
            add(new ButtonData("4", 2, 0, 1));
            add(new ButtonData("5", 2, 1, 1));
            add(new ButtonData("6", 2, 2, 1));
            add(new ButtonData("1", 3, 0, 1));
            add(new ButtonData("2", 3, 1, 1));
            add(new ButtonData("3", 3, 2, 1));
            add(new ButtonData("0", 4, 0, 2));
            add(new ButtonData(".", 4, 2, 1));
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createLayout();
        createExpressionDisplay();
        createButtons();
    }

    private void createLayout() {
        GridLayout mainLayout = new GridLayout(this);
        mainLayout.setColumnCount(4);
        mainLayout.setId(R.id.mainLayout);
        setContentView(mainLayout);
    }

    private void createExpressionDisplay() {
        ExpressionView expressionView = new ExpressionView(this);
        expressionView.setId(R.id.expressionDisplay);
        GridLayout mainLayout = findViewById(R.id.mainLayout);
        mainLayout.addView(expressionView);
    }

    private void createButtons() {
        GridLayout mainLayout = findViewById(R.id.mainLayout);
        ExpressionView expressionView = findViewById(R.id.expressionDisplay);
        buttonData.add(new ButtonData("=", 6, 0, 4, ButtonData.ButtonType.EVALUATE));
        buttonData.forEach(data -> {
            Button button = new Button(
                    this,
                    data,
                    (view) -> {
                        if (expressionView.getText().toString().contains(getResources().getString(R.string.error))) {
                            expressionView.setText("");
                        }

                        if (data.getType() == ButtonData.ButtonType.EVALUATE) {
                            try {
                                DecimalFormat df = new DecimalFormat("###.###");
                                String value = df.format(ParseInput.evaluate(expressionView.getText().toString()));
                                expressionView.setText(value.equals("NaN") ? getResources().getString(R.string.error) : value);
                            } catch (Exception e) {
                                expressionView.setText(R.string.error);
                            }

                        } else if (data.getType() == ButtonData.ButtonType.CLEAR) {
                            expressionView.setText("");

                        } else if (data.getType() == ButtonData.ButtonType.OPERATOR){
                            expressionView.setText(expressionView.getText().toString() + " " + data.getButtonText() + " ");

                        } else {
                            expressionView.setText(expressionView.getText().toString() + data.getButtonText());
                        }
                    }
            );

            mainLayout.addView(button);
        });
    }
}