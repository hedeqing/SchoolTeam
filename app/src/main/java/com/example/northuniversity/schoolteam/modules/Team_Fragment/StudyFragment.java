package com.example.northuniversity.schoolteam.modules.Team_Fragment;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.base.BaseFragment;

import com.example.northuniversity.schoolteam.modules.Team.tools.RecommendTeamAdapter;
import com.example.northuniversity.schoolteam.utils.HttpUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.youth.banner.Banner;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

public class StudyFragment extends BaseFragment {
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private XRecyclerView mRecyclerView;
    private RecommendTeamAdapter mAdapter;

    private TextView titleTv = null;
    private SearchView headeSv = null;
    private Button sendBtn = null;

    public Banner banner = null;
    public ImageView gameView = null;
    public ImageView studyView = null;
    public ImageView contestiew = null;
    public ImageView travalView = null;
    private String result;

    private List<String> team_id = new ArrayList<>();
    private List<String> menber_id = new ArrayList<>();
    private List<String> menber_quantity = new ArrayList<>();
    private List<String> category = new ArrayList<>();
    private List<String> description = new ArrayList<>();
    private List<String> team_picture = new ArrayList<>();
    private List<String> start_time = new ArrayList<>();
    private List<String> end_time = new ArrayList<>();
    private List<String> location = new ArrayList<>();
    private List<String> fare = new ArrayList<>();
    private List<String> favor_quantity = new ArrayList<>();
    private  List<String> capture = new ArrayList<>();



    public List<Integer> images = new ArrayList<>();

    // 存储数据
    private List<String> dataList = new ArrayList<>();

    private ScheduledExecutorService scheduledExecutorService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mView == null) {
            // 需要inflate一个布局文件 填充Fragment
            mView = inflater.inflate(R.layout.fragment_study, container, false);


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
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        show_team("study");
        mRecyclerView = getActivity().findViewById(R.id.study_xrv);
        //  XRecyclerView的使用，和RecyclerView几乎一致
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        // 可以设置是否开启加载更多/下拉刷新
        mRecyclerView.setLoadingMoreEnabled(true);
        // 可以设置加载更多的样式，很多种
        mRecyclerView.setLoadingMoreProgressStyle(ProgressStyle.Pacman);
        // 如果设置上这个，下拉刷新的时候会显示上次刷新的时间
        mRecyclerView.getDefaultRefreshHeaderView() // get default refresh header view
                .setRefreshTimeVisible(true);  // make refresh time visible,false means hiding

        Log.d(ContentValues.TAG, "111: idList" + team_id);
        Log.d(ContentValues.TAG, "111: startTime" + start_time);
        Log.d(ContentValues.TAG, "111: endTime" + end_time);
        Log.d(ContentValues.TAG, "111: category" + category);
        mAdapter = new RecommendTeamAdapter(team_id, menber_id, menber_quantity, category, description, team_picture, start_time, end_time, location, fare, favor_quantity,capture, getContext());

//        mAdapter.addtData(team_id, menber_id, menber_quantity, category, description, team_picture, start_time, end_time, location, fare, favor_quantity);

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.notifyDataSetChanged();


        // 添加刷新和加载更多的监听
        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                // 为了看效果，加了一个等待效果，正式的时候直接写mRecyclerView.refreshComplete();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(team_id, menber_id, menber_quantity, category, description, team_picture, start_time, end_time, location, fare, favor_quantity, capture);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.refreshComplete();
                    }
                }, 2000);
            }

            @Override
            public void onLoadMore() {
                // 为了看效果，加了一个等待效果，正式的时候直接写mRecyclerView.loadMoreComplete();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.setData(team_id, menber_id, menber_quantity, category, description, team_picture, start_time, end_time, location, fare, favor_quantity, capture);

                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.loadMoreComplete();
                    }
                }, 2000);
            }
        });



    }



    private void show_team(final String category) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String param = "category="+category;
                String url = "http://192.168.137.1:8000/get_team_by_category/";
                result = HttpUtils.sendPostRequest(url, param);
                Message message = new Message();
                message.what = 6;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();

    }

    Handler handler = new Handler() {
        public void handleMessage(final Message message) {
            List<String> team_i = new ArrayList<>();
            List<String> menber_i = new ArrayList<>();
            List<String> menber_q = new ArrayList<>();
            List<String> category_i = new ArrayList<>();
            List<String> description_i = new ArrayList<>();
            List<String> team_picture_i = new ArrayList<>();
            List<String> start_time_i = new ArrayList<>();
            List<String> end_time_i = new ArrayList<>();
            List<String> location_i = new ArrayList<>();
            List<String> fare_i = new ArrayList<>();
            List<String> favor_quantity_i = new ArrayList<>();

            List<String> capture_i = new ArrayList<>();
            if (message.what == 6) {
                result = message.getData().getString("result");

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        team_i.add(jsonObject.getString("pk"));
                        JSONObject jsonObject1 = jsonObject.getJSONObject("fields");
                        menber_i.add(jsonObject1.getString("menber_id"));
                        menber_q.add(jsonObject1.getString("menber_quantity"));
                        category_i.add(jsonObject1.getString("category"));
                        description_i.add(jsonObject1.getString("description"));
                        team_picture_i.add(jsonObject1.getString("team_picture"));
                        start_time_i.add(jsonObject1.getString("start_time"));
                        end_time_i.add(jsonObject1.getString("end_time"));
                        location_i.add(jsonObject1.getString("location"));
                        fare_i.add(jsonObject1.getString("fare"));
                        favor_quantity_i.add(jsonObject1.getString("favor_quantity"));

                        capture_i.add(jsonObject1.getString("team_id"));
                    }
                    team_id = team_i;
                    menber_id = menber_i;
                    menber_quantity = menber_q;
                    category = category_i;
                    description = description_i;
                    team_picture = team_picture_i;
                    start_time = start_time_i;
                    end_time = end_time_i;
                    location = location_i;
                    fare = fare_i;
                    favor_quantity = favor_quantity_i;
                    capture = capture_i;

                    Log.d(ContentValues.TAG, "run: idList" + team_id);
                    Log.d(ContentValues.TAG, "run: startTime" + start_time);
                    Log.d(ContentValues.TAG, "run: endTime" + end_time);
                    Log.d(ContentValues.TAG, "run: category" + category);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    };

    private List<String> testList() {
        List<String> mytest = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            mytest.add("test" + System.currentTimeMillis());
        }
        mytest.add("test" + System.currentTimeMillis());
        return mytest;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }



    @Override
    public void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            return;
        }
        //填充各控件的数据
//        mHasLoadedOnce = true;
    }

    public static StudyFragment newInstance(String param1) {
        StudyFragment fragment = new StudyFragment();
        Bundle args = new Bundle();
        args.putString("agrs1", param1);
        fragment.setArguments(args);
        return fragment;
    }

}
