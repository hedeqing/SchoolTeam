package com.example.northuniversity.schoolteam.modules.Team;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.example.northuniversity.schoolteam.R;

public class WebActivity extends AppCompatActivity {

    private WebView webView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        webView = findViewById(R.id.webview);
        Intent intent = getIntent();
        webView.loadUrl(intent.getStringExtra("url"));
    }
}
