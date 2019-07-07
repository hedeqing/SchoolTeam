package com.example.northuniversity.schoolteam.modules.Team;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;

import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.modules.Team.tools.RecommendTeamAdapter;
import com.example.northuniversity.schoolteam.utils.HttpUtils;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;
import static org.litepal.LitePalApplication.getContext;

public class SearchActivity extends AppCompatActivity {

    private Toolbar toolbar = null;
    private SearchView searchView = null;
    private XRecyclerView mRecyclerView = null;
    private String TAG = "SearchActivity";
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
    private List<String> capture = new ArrayList<>();
    private RecommendTeamAdapter recommendTeamAdapter = null;
    private String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
        toolbar = findViewById(R.id.search_toolbar);
        mRecyclerView = findViewById(R.id.search_xrv);
        searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(true);
        /**
         * 默认情况下是没提交搜索的按钮，所以用户必须在键盘上按下"enter"键来提交搜索.你可以同过setSubmitButtonEnabled(
         * true)来添加一个提交按钮（"submit" button)
         * 设置true后，右边会出现一个箭头按钮。如果用户没有输入，就不会触发提交（submit）事件
         */
        searchView.setSubmitButtonEnabled(true);
        /**
         * 初始是否已经是展开的状态
         * 写上此句后searchView初始展开的，也就是是可以点击输入的状态，如果不写，那么就需要点击下放大镜，才能展开出现输入框
         */
        searchView.onActionViewExpanded();
        // 设置search view的背景色
        searchView.setBackgroundColor(0x22ff00ff);
        /**
         * 默认情况下, search widget是"iconified“的，只是用一个图标 来表示它(一个放大镜),
         * 当用户按下它的时候才显示search box . 你可以调用setIconifiedByDefault(false)让search
         * box默认都被显示。 你也可以调用setIconified()让它以iconified“的形式显示。
         */
        searchView.setIconifiedByDefault(true);

        setSupportActionBar(toolbar);
        toolbar.setTitle("比赛详情");//设置标题
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
        getSupportActionBar().setHomeButtonEnabled(true);//设置返回键可用，如果某个页面想隐藏掉返回键比如首页，可以调用mToolbar.setNavigationIcion(null);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "onQueryTextSubmit: s" + s);
                string = s;
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "onQueryTextSubmit = " + s);
                getSearchresult(s);
                return true;
            }
        });

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

        recommendTeamAdapter = new RecommendTeamAdapter(team_id, menber_id, menber_quantity, category, description, team_picture, start_time, end_time, location, fare, favor_quantity, capture, getContext());

        recommendTeamAdapter.setData(team_id,menber_id,menber_quantity,category,description,team_picture,start_time,end_time,location,fare,favor_quantity,capture);
//        mAdapter.addtData(team_id, menber_id, menber_quantity, category, description, team_picture, start_time, end_time, location, fare, favor_quantity);
//
        mRecyclerView.setAdapter(recommendTeamAdapter);

        recommendTeamAdapter.notifyDataSetChanged();

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                // 为了看效果，加了一个等待效果，正式的时候直接写mRecyclerView.refreshComplete();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recommendTeamAdapter.setData(team_id, menber_id, menber_quantity, category, description, team_picture, start_time, end_time, location, fare, favor_quantity,capture);
                        recommendTeamAdapter.notifyDataSetChanged();
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
                        recommendTeamAdapter.setData(team_id, menber_id, menber_quantity, category, description, team_picture, start_time, end_time, location, fare, favor_quantity,capture);
                        recommendTeamAdapter.notifyDataSetChanged();
                        mRecyclerView.loadMoreComplete();
                    }
                }, 2000);
            }
        });
        recommendTeamAdapter.notifyDataSetChanged();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        recommendTeamAdapter.notifyDataSetChanged();
    }

    public void getSearchresult(final String text) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String params = "text=" + text;
                String url = "http://192.168.137.1:8000/search/";
                result = HttpUtils.sendPostRequest(url, params);
                Log.d(TAG, "run: 11111111" + result);
                Message message = new Message();
                message.what = 16;
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
            if (message.what == 16) {
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
                        capture_i.add(jsonObject1.getString("team_id"));
                        favor_quantity_i.add(jsonObject1.getString("favor_quantity"));
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

}
