package com.example.northuniversity.schoolteam.modules.Recommend_fragment.Inside;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import com.example.northuniversity.schoolteam.R;
public class ConcreteRecommendContest extends AppCompatActivity {
        private Toolbar toolbar = null;
        private String title ;
        private  String file ;
        private TextView showTv= null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_recommend_contest);
        toolbar =   findViewById(R.id.concrete_contest_toolbar);
        showTv  = findViewById(R.id.show_contest_tv);
        setSupportActionBar(toolbar);
        toolbar.setTitle("比赛详情");//设置标题
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用mToolbar.setNavigationIcion(null);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        Intent intent = getIntent();
        title = intent.getStringExtra("content");
        showTv.setText(title);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();//返回
            }
        });

    }
}
