package com.example.northuniversity.schoolteam.modules.Recommend_fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.base.BaseFragment;
import com.example.northuniversity.schoolteam.modules.Recommend.adapter.RecommendContestAdapter;
import com.example.northuniversity.schoolteam.utils.HttpUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class FourthFragment extends BaseFragment {

    private TextView contestNameTv= null;
    private  TextView startTimeTv = null;
    private  TextView endTimeTv = null;

    private  String result;
    private List<String> nameList = new ArrayList<>();
    private List<String> contentList = new ArrayList<>();
    private List<String> startTimeList = new ArrayList<>();
    private List<String> endTimeList = new ArrayList<>();
    private List<String> fileList = new ArrayList<>();
    private XRecyclerView recyclerView = null;
    private RecommendContestAdapter recommendContestadpater = null;
    private List<String> dataList = new ArrayList<>();



    private  String status;

    private boolean mHasLoadedOnce;
    private boolean isPrepared;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView == null){
             mView = inflater.inflate(R.layout.fragment_fourth, container, false);
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


        // 可以设置是否开启加载更多/下拉刷新
        recyclerView.setLoadingMoreEnabled(true);
        //  XRecyclerView的使用，和RecyclerView几乎一致
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        // 可以设置加载更多的样式，很多种
        recyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        // 如果设置上这个，下拉刷新的时候会显示上次刷新的时间
        recyclerView.getDefaultRefreshHeaderView() // get default refresh header view
                .setRefreshTimeVisible(true);  // make refresh time visible,false means hiding
        show_recommend("Senior");
        recommendContestadpater = new RecommendContestAdapter(nameList, contentList, startTimeList, endTimeList, fileList, getActivity());
        recyclerView.setAdapter(recommendContestadpater);
        // 添加刷新和加载更多的监听
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                // 为了看效果，加了一个等待效果，正式的时候直接写mRecyclerView.refreshComplete();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.refreshComplete();
                    }
                }, 2000);
            }
            @Override
            public void onLoadMore() {
                // 为了看效果，加了一个等待效果，正式的时候直接写mRecyclerView.loadMoreComplete();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recyclerView.loadMoreComplete();
                    }
                }, 2000);
            }
        });
    }
    private List<String> testList() {
        List<String> mytest = new ArrayList<>();
        for(int i = 0 ; i < 10; i++) {
            mytest.add("test" + System.currentTimeMillis());
        }
        mytest.add("test"+ System.currentTimeMillis());
        return mytest;
    }

    public void initView(){
        recyclerView = getActivity().findViewById(R.id.fourth_rv);
    }

    public static FourthFragment newInstance(String param1) {
        FourthFragment fragment = new FourthFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        //填充各控件的数据
        mHasLoadedOnce = true;

    }
    private void show_recommend(final String education) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://10.0.2.2:8000/show_recommend/";
                String params = "education=" + education;
                Log.d(TAG, "run: education = " + education);
                result = HttpUtils.sendPostRequest(url, params);

                Message message = new Message();
                message.what = 2;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();

    }

    Handler handler = new Handler() {
        public void handleMessage(final Message message) {
            List<String> name = new ArrayList<>();
            List<String> startTime = new ArrayList<>();
            List<String> endTime = new ArrayList<>();
            List<String> content = new ArrayList<>();
            List<String> file = new ArrayList<>();
            if (message.what == 2) {
                result = message.getData().getString("result");

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        nameList.add(jsonObject.getString("pk"));
                        JSONObject jsonObject1 = jsonObject.getJSONObject("fields");
                        startTimeList.add(jsonObject1.getString("sign_up_time"));
                        endTimeList.add(jsonObject1.getString("deadline"));
                        contentList.add(jsonObject1.getString("context"));
                        fileList.add(jsonObject1.getString("file"));
                    }
                    nameList = name;
                    startTimeList = startTime;
                    endTimeList = endTime;
                    contentList = content;
                    fileList = file;
                    Log.d(ContentValues.TAG, "run: nameList" + nameList);
                    Log.d(ContentValues.TAG, "run: startTimeList" + startTimeList);
                    Log.d(ContentValues.TAG, "run: endTimeList" + endTimeList);
                    Log.d(ContentValues.TAG, "run: contentList" + contentList);
                    Log.d(ContentValues.TAG, "run: fileList" + fileList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    };
}
