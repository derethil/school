package com.example.webbrowser;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;

import java.sql.SQLOutput;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final HistoryList browserHistory = new HistoryList();

        // Main Layout
        LinearLayout mainLayout = new LinearLayout(this);
        LinearLayout topLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);

        // Web Content
        final WebView webView = new WebView(this);
        webView.setWebViewClient(new WebViewClient());

        // Address Bar
        final AppCompatEditText addressBar = new AppCompatEditText(this);
        LinearLayout.LayoutParams addressParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        addressParams.weight = 1;
        addressBar.setLayoutParams(addressParams);

        // Address Bar Buttons
        AppCompatButton backBtn = Button.newButton(this, "B");
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!browserHistory.hasPreviousUrl()) { return; }
                String url = browserHistory.getPreviousUrl();
                webView.loadUrl(url);
                addressBar.setText(url);
            }
        });

        AppCompatButton forwardBtn = Button.newButton(this, "F");
        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!browserHistory.hasNextUrl()) { return; }
                String url = browserHistory.getNextUrl();
                webView.loadUrl(url);
                addressBar.setText(url);
            }
        });

        AppCompatButton goBtn = Button.newButton(this, "G");
        goBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = addressBar.getText().toString();
                browserHistory.addUrl(url);
                webView.loadUrl(url);
            }
        });

        // Main Layout
        topLayout.addView(backBtn);
        topLayout.addView(forwardBtn);
        topLayout.addView(addressBar);
        topLayout.addView(goBtn);
        mainLayout.addView(topLayout);
        mainLayout.addView(webView);
        setContentView(mainLayout);
    }
}