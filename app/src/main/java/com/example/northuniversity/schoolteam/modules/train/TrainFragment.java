package com.example.northuniversity.schoolteam.modules.train;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.base.BaseFragment;
import com.example.northuniversity.schoolteam.modules.train_fragment.FragmentAdapter;
import com.example.northuniversity.schoolteam.modules.train_fragment.FunFragment;
import com.example.northuniversity.schoolteam.modules.train_fragment.FuzzySearchFragment;
import com.example.northuniversity.schoolteam.modules.train_fragment.ViewAdapter;


import net.lucode.hackware.magicindicator.MagicIndicator;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class TrainFragment extends BaseFragment {
    private boolean isPrepared;
    private TabLayout tabLayout = null;
    private List<View> listViews;
    private List<Fragment> list_fragment;
    private ViewPager viewPager = null;
    private  List<String>  tabName = null;
    private  View funView = null;
    private View fuzzySearchView = null;

    private ViewAdapter vAdapter;                                         //定义以view为切换的adapter
    private FragmentAdapter fAdapter;

    private FunFragment funFragment = null;
    private FuzzySearchFragment fuzzySearchFragment = null;


    private int[] tabImg;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    TextView textView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.activity_train, container, false);
            initView();
            isPrepared = true;
//        实现懒加载
            lazyLoad();
        }
        //缓存的mView需要判断是否已经被加过parent， 如果有parent需要从parent删除，要不然会发生这个mView已经有parent的错误。
        ViewGroup parent = (ViewGroup) mView.getParent();
        if (parent != null) {
            parent.removeView(mView);
        }

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        viewChanage();

    }

    /**
     * 初始化控件
     */
    private void initView() {
        tabLayout = getActivity().findViewById(R.id.tabLayout);
        viewPager = getActivity().findViewById(R.id.viewPager);

        //为tabLayout上的图标赋值
        tabImg = new int[]{R.drawable.ic_back,R.drawable.ic_code};
        Log.i(TAG, "initControls: is evoke");

    }
    private void viewChanage()
    {
        listViews = new ArrayList<>();
        LayoutInflater mInflater = getLayoutInflater();

        fuzzySearchView = mInflater.inflate(R.layout.fragment_search, null);
        funView = mInflater.inflate(R.layout.fragment_fun, null);
        listViews.add(fuzzySearchView);
        listViews.add(funView);

        tabName = new ArrayList<>();
        tabName.add("搜索");
        tabName.add("寻找");

        //设置TabLayout的模式,这里主要是用来显示tab展示的情况的
        //TabLayout.MODE_FIXED          各tab平分整个工具栏,如果不设置，则默认就是这个值
        //TabLayout.MODE_SCROLLABLE     适用于多tab的，也就是有滚动条的，一行显示不下这些tab可以用这个
        //                              当然了，你要是想做点特别的，像知乎里就使用的这种效果
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);

        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText(tabName.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(tabName.get(1)));


        vAdapter = new ViewAdapter(getContext(),listViews,tabName,tabImg);
        viewPager.setAdapter(vAdapter);

        //将tabLayout与viewpager连起来
        tabLayout.setupWithViewPager(viewPager);
        Log.i(TAG, "viewchange: is evoke");
    }

    /**
     * 采用viewpager中切换fragment
     */
    private void fragmentChange()
    {
        list_fragment = new ArrayList<>();

        funFragment = new FunFragment();
        fuzzySearchFragment = new FuzzySearchFragment();


        list_fragment.add(funFragment);
        list_fragment.add(fuzzySearchFragment);

        tabName = new ArrayList<>();
        tabName.add("搜索");
        tabName.add("寻找");

        fAdapter = new FragmentAdapter(getChildFragmentManager(),list_fragment,tabName);
        viewPager.setAdapter(fAdapter);

        //将tabLayout与viewpager连起来
        tabLayout.setupWithViewPager(viewPager);
        Log.i(TAG, "fragmentChange: is evoke");
    }


    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        //填充各控件的数据
        mHasLoadedOnce = true;
    }
    public static TrainFragment newInstance(String param1) {
        TrainFragment fragment = new TrainFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
}
