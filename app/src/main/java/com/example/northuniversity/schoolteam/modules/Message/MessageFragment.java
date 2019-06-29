package com.example.northuniversity.schoolteam.modules.Message;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.base.BaseFragment;

public class MessageFragment extends BaseFragment {
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    TextView textView;
    private Toolbar messageTb= null;
    private  TextView chatTv = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.fragment_meaasge, container, false);
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
        messageTb = getActivity().findViewById(R.id.message_toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(messageTb);
        chatTv = getActivity().findViewById(R.id.chat_tv);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(false);//设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用mToolbar.setNavigationIcion(null);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        chatTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ChatActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
        chatTv = getActivity().findViewById(R.id.chat_tv);
    }

    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        //填充各控件的数据
        mHasLoadedOnce = true;
    }
    public static Fragment newInstance(String param1) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
}
