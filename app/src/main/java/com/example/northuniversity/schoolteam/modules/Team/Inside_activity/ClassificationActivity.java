package com.example.northuniversity.schoolteam.modules.Team.Inside_activity;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.modules.Recommend_fragment.FirstFragment;
import com.example.northuniversity.schoolteam.modules.Recommend_fragment.FourthFragment;
import com.example.northuniversity.schoolteam.modules.Recommend_fragment.FragmentAdapter;
import com.example.northuniversity.schoolteam.modules.Recommend_fragment.SecondFragment;
import com.example.northuniversity.schoolteam.modules.Recommend_fragment.ThirdFragment;
import com.example.northuniversity.schoolteam.modules.Recommend_fragment.ViewAdapter;
import com.example.northuniversity.schoolteam.modules.Team_Fragment.ContestFragment;
import com.example.northuniversity.schoolteam.modules.Team_Fragment.GameFragment;
import com.example.northuniversity.schoolteam.modules.Team_Fragment.StudyFragment;
import com.example.northuniversity.schoolteam.modules.Team_Fragment.TravelFragment;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;
import static org.litepal.LitePalApplication.getContext;

public class ClassificationActivity extends AppCompatActivity {

    private TabLayout tabLayout = null;
    private List<View> listViews;
    private List<Fragment> list_fragment;
    private ViewPager viewPager = null;
    private List<String> tabName = null;
    private ViewAdapter vAdapter;                                         //定义以view为切换的adapter
    private FragmentAdapter fAdapter;
    private GameFragment gameFragment = null;
    private StudyFragment studyFragment = null;
    private TravelFragment travelFragment = null;
    private ContestFragment contestFragment = null;


    private View gameView = null;
    private View studyView = null;
    private View travalView = null;
    private View contestView = null;

    private int[] tabImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classification);
        initView();
        fragmentChange();
        //根据传递的参数设置界面
        Intent intent = getIntent();
        int page = intent.getIntExtra("page", 0);
        viewPager.setCurrentItem(page);
    }

    /**
     * 初始化控件
     */
    private void initView() {
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);

        //为tabLayout上的图标赋值
        tabImg = new int[]{R.drawable.ic_game, R.drawable.ic_study, R.drawable.ic_traval, R.drawable.ic_contest};
        Log.i(TAG, "initControls: is evoke");

    }

    //    private void viewChanage()
//    {
//        listViews = new ArrayList<>();
//        LayoutInflater mInflater = getLayoutInflater();
//
//        gameView = mInflater.inflate(R.layout.fragment_game, null);
//        studyView = mInflater.inflate(R.layout.fragment_study, null);
//        travalView = mInflater.inflate(R.layout.fragment_traval, null);
//        contestView = mInflater.inflate(R.layout.fragment_contest, null);
//        listViews.add(gameView);
//        listViews.add(studyView);
//        listViews.add(travalView);
//        listViews.add(contestView);
//
//
//        tabName = new ArrayList<>();
//        tabName.add("游戏");
//        tabName.add("学习");
//        tabName.add("出行");
//        tabName.add("比赛");
//
//
//        //设置TabLayout的模式,这里主要是用来显示tab展示的情况的
//        //TabLayout.MODE_FIXED          各tab平分整个工具栏,如果不设置，则默认就是这个值
//        //TabLayout.MODE_SCROLLABLE     适用于多tab的，也就是有滚动条的，一行显示不下这些tab可以用这个
//        //                              当然了，你要是想做点特别的，像知乎里就使用的这种效果
//        tabLayout.setTabMode(TabLayout.MODE_FIXED);
//
//        //设置tablayout距离上下左右的距离
//        //tab_title.setPadding(20,20,20,20);
//
//        //为TabLayout添加tab名称
//        tabLayout.addTab(tabLayout.newTab().setText(tabName.get(0)));
//        tabLayout.addTab(tabLayout.newTab().setText(tabName.get(1)));
//        tabLayout.addTab(tabLayout.newTab().setText(tabName.get(2)));
//        tabLayout.addTab(tabLayout.newTab().setText(tabName.get(3)));
//
//
//        vAdapter = new ViewAdapter(getContext(),listViews,tabName,tabImg);
//        viewPager.setAdapter(vAdapter);
//
//        //将tabLayout与viewpager连起来
//        tabLayout.setupWithViewPager(viewPager);
//
//
//        Log.i(TAG, "viewchange: is evoke");
//
//    }
    private void fragmentChange() {
        list_fragment = new ArrayList<>();
        gameFragment = GameFragment.newInstance("       游戏      ");
        studyFragment = StudyFragment.newInstance("       学习      ");
        travelFragment = TravelFragment.newInstance("       出行     ");
        contestFragment = ContestFragment.newInstance("       比赛      ");

        list_fragment.add(gameFragment);
        list_fragment.add(studyFragment);
        list_fragment.add(travelFragment);
        list_fragment.add(contestFragment);

        tabName = new ArrayList<>();
        tabName.add("       游戏      ");
        tabName.add("       学习      ");
        tabName.add("       出行     ");
        tabName.add("       比赛      ");

        fAdapter = new FragmentAdapter(getSupportFragmentManager(), list_fragment, tabName);

        viewPager.setAdapter(fAdapter);

        //将tabLayout与viewpager连起来
        tabLayout.setupWithViewPager(viewPager);
        Log.i(TAG, "fragmentChange: is evoke");
    }


}
