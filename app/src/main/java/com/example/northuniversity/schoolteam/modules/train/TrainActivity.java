package com.example.northuniversity.schoolteam.modules.train;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DialogTitle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.widget.TableLayout;

import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.modules.train_fragment.FragmentAdapter;
import com.example.northuniversity.schoolteam.modules.train_fragment.FunFragment;
import com.example.northuniversity.schoolteam.modules.train_fragment.FuzzySearchFragment;
import com.example.northuniversity.schoolteam.modules.train_fragment.ViewAdapter;

import java.util.ArrayList;
import java.util.List;


public class TrainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_train);
    }
}
