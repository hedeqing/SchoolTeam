package com.example.northuniversity.schoolteam.modules.Person;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.example.northuniversity.schoolteam.R;

public class PersonMessageActivity extends AppCompatActivity {

    private Toolbar toolbar = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_message);
        toolbar =   findViewById(R.id.release_toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用mToolbar.setNavigationIcion(null);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
