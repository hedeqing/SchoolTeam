package com.example.northuniversity.schoolteam.modules.Recommend.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.modules.Recommend_fragment.Inside.ConcreteRecommendContest;
import com.example.northuniversity.schoolteam.utils.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class RecommendContestAdapter extends RecyclerView.Adapter<RecommendContestAdapter.RecyclerHolder> {
    private List<String> team_id = new ArrayList<>();
    private List<String> contentList = new ArrayList<>();
    private List<String> startTimeList = new ArrayList<>();
    private List<String> endTimeList = new ArrayList<>();
    private List<String> fileList = new ArrayList<>();
    private String result;
    private List<String> theNameLists = new ArrayList<>();

    private Context mContext;
    private Intent intent;


    public RecommendContestAdapter(List<String> team_id, List<String> contentList, List<String> startTimeList, List<String> endTimeList, List<String> fileList, Context mContext) {
        this.team_id = team_id;
        this.contentList = contentList;
        this.startTimeList = startTimeList;
        this.endTimeList = endTimeList;
        this.fileList = fileList;
        this.mContext = mContext;
    }

    public void addtData(List<String> nameList, List<String> startTimeList, List<String> endTimeList, List<String> contentList, List<String> fileList) {
        if (null != nameList | null != nameList | null != startTimeList | null != endTimeList | null != contentList | null != fileList) {
            this.team_id.addAll(nameList);
            this.contentList.addAll(contentList);
            this.endTimeList.addAll(endTimeList);
            this.startTimeList.addAll(startTimeList);
            this.fileList.addAll(fileList);
            notifyDataSetChanged();
        }
    }

    public void setData(List<String> nameList, List<String> startTimeList, List<String> endTimeList, List<String> contentList, List<String> fileList) {
        if (null != nameList | null != nameList | null != startTimeList | null != endTimeList | null != contentList | null != fileList) {
            this.team_id.clear();
            this.team_id.addAll(nameList);

            this.fileList.clear();
            this.fileList.addAll(fileList);

            this.contentList.clear();
            this.contentList.addAll(contentList);

            this.startTimeList.getClass();
            this.startTimeList.addAll(startTimeList);

            this.endTimeList.clear();
            this.endTimeList.addAll(endTimeList);

            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.card_item_recommend, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerHolder holder, final int i) {
        holder.contestNameTv.setText(team_id.get(i));
        holder.startTimeTv.setText(startTimeList.get(i));
        holder.endTimeTv.setText(endTimeList.get(i));
        get_Name(team_id);
        Log.d(TAG, "RecyclerHolder: thenamelist" + theNameLists);
        holder.contestCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(mContext, ConcreteRecommendContest.class);
                intent.putExtra("content", contentList.get(i));
                intent.putExtra("file", fileList.get(i));
                mContext.startActivity(intent);
            }
        });
        Log.d(TAG, "onBindViewHolder: name" + team_id.get(i));
    }

    @Override
    public int getItemCount() {
        return team_id.size();
    }


    class RecyclerHolder extends RecyclerView.ViewHolder {
        private TextView contestNameTv = null;
        private TextView startTimeTv = null;
        private TextView endTimeTv = null;
        private TextView joinTv = null;
        private CardView contestCardView = null;

        //在这里实现view的添加，其实在别的地方能全部添加，但是需要添加具体一边以后能实现监控
        private RecyclerHolder(View itemView) {
            super(itemView);
            contestNameTv = itemView.findViewById(R.id.contest_name_tv);
            startTimeTv = itemView.findViewById(R.id.start_time);
            endTimeTv = itemView.findViewById(R.id.end_time_tv);
            contestCardView = itemView.findViewById(R.id.card_view);
        }

    }

    public void get_Name(final List<String> user_id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url = "http://192.168.137.1:8000/get_name_by_id/";
                String params = "user_id=" + user_id;
                result = HttpUtils.sendPostRequest(url, params);
                Message message = new Message();
                message.what = 12;
                Bundle bundle = new Bundle();
                bundle.putString("result", result);
                ;
                message.setData(bundle);
                handler.sendMessage(message);
            }
        }).start();
    }

    android.os.Handler handler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            List<String> theNameList = new ArrayList<>();
            if (msg.what == 12) {
                result = msg.getData().getString("result");

                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int j = 0; j < jsonArray.length(); j++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(j);
                        JSONObject jsonObject1 = jsonObject.getJSONObject("fields");
                        theNameList.add(jsonObject1.getString("usernmae"));
                    }
                    theNameLists = theNameList;
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        }
    };
}
