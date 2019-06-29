package com.example.northuniversity.schoolteam;

import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.northuniversity.schoolteam.modules.Recommend.RecommedFragment;
import com.example.northuniversity.schoolteam.modules.Message.MessageFragment;
import com.example.northuniversity.schoolteam.modules.Person.PersonFragment;
import com.example.northuniversity.schoolteam.modules.Team.TeamFragment;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements BottomNavigationBar.OnTabSelectedListener {
    private ArrayList<Fragment> fragments;
    private BottomNavigationBar bottomNavigationBar;
    private Fragment messageFragment;
    private TeamFragment teamFragment;
    private RecommedFragment recommendFragment;
    private PersonFragment personFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    int lastSelectedPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigationBar = findViewById(R.id.bottom_navigation_bar);
        init();

    }
    private void init() {
        //要先设计模式后再添加图标！
        //设置按钮模式  MODE_FIXED表示固定   MODE_SHIFTING表示转移
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_SHIFTING);
        //设置背景风格
        // BACKGROUND_STYLE_STATIC表示静态的
        //BACKGROUND_STYLE_RIPPLE表示涟漪的，也就是可以变化的 ，跟随setActiveColor里面的颜色变化
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);
        //添加并设置图标、图标的颜色和文字
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_team, "组队")).setActiveColor(R.color.red)
                .addItem(new BottomNavigationItem(R.drawable.ic_message, "消息")).setActiveColor(R.color.colorPrimary)
                .addItem(new BottomNavigationItem(R.drawable.ic_recommended, "推荐")).setActiveColor(R.color.red)
                .addItem(new BottomNavigationItem(R.drawable.ic_person, "个人")).setActiveColor(R.color.colorPrimary)
                .setFirstSelectedPosition(lastSelectedPosition )
                .initialise();

        bottomNavigationBar.setTabSelectedListener(this);
        setDefaultFragment();
    }

    //设置初始界面
    private void setDefaultFragment() {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.layFrame, TeamFragment.newInstance("消息"));
        transaction.commit();
    }

    @Override
    public void onTabSelected(int position) {
        fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        switch (position) {
            case 0:
                if (teamFragment == null) {
                    teamFragment = TeamFragment.newInstance("组队");
                }
                transaction.replace(R.id.layFrame, teamFragment);
                break;
            case 1:

                if (messageFragment == null) {
                    messageFragment = MessageFragment.newInstance("消息");
                }
                transaction.replace(R.id.layFrame, messageFragment);
                break;
            case 2:
                if (recommendFragment == null) {
                    recommendFragment = RecommedFragment.newInstance("推荐");
                }
                transaction.replace(R.id.layFrame, recommendFragment);
                break;
            case 3:
                if (personFragment == null) {
                    personFragment = PersonFragment.newInstance("个人");
                }
                transaction.replace(R.id.layFrame, personFragment);
                break;
            default:
                break;
        }
        // 事务提交
        transaction.commit();
    }

    @Override
    public void onTabUnselected(int position) {
        Log.d("dongtaiFragment", "onTabUnselected() called with: " + "position = [" + position + "]");
    }

    @Override
    public void onTabReselected(int position) {

    }
}