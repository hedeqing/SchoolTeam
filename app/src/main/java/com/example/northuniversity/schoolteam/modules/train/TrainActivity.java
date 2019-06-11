package com.example.northuniversity.schoolteam.modules.train;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;

import com.example.northuniversity.schoolteam.R;


public class TrainActivity extends AppCompatActivity {
    private TableLayout tableLayout = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_train);
    }
}
