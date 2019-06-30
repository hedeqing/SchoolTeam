package com.example.northuniversity.schoolteam.modules.Team.Inside_activity;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.northuniversity.schoolteam.R;

import org.w3c.dom.Text;

public class ConcreteActivity extends AppCompatActivity {

    private Toolbar toolbar  = null ;
    private TextView descriptionTv = null;
    private TextView menberQuantityTv = null;
    private  TextView timeTv = null;
    private  TextView locationTv = null;
    private  TextView fareTv = null;
    private Button joinBtn = null;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete);

        descriptionTv = findViewById(R.id.description_team);
        menberQuantityTv = findViewById(R.id.menber_quantity);
        timeTv = findViewById(R.id.time_team);
        locationTv = findViewById(R.id.location_team);
        fareTv = findViewById(R.id.fare_team);

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Intent intent = getIntent();
        descriptionTv.setText(intent.getStringExtra("title"));
        menberQuantityTv.setText(intent.getStringExtra("menber_quantity"));
        timeTv.setText(intent.getStringExtra("time"));
        locationTv.setText(intent.getStringExtra("location"));
        fareTv.setText(intent.getStringExtra("fare"));

        toolbar =   findViewById(R.id.concrete_toolbar);
        setSupportActionBar(toolbar);
        ActionBar  actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用mToolbar.setNavigationIcion(null);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
