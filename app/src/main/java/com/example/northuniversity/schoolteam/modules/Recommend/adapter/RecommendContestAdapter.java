package com.example.northuniversity.schoolteam.modules.Recommend.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.example.northuniversity.schoolteam.modules.Team.Inside_activity.ConcreteActivity;
import com.example.northuniversity.schoolteam.modules.Team.tools.RecommendTeamAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class RecommendContestAdapter extends RecyclerView.Adapter<RecommendContestAdapter.RecyclerHolder>{
    private List<String> nameList = new ArrayList<>();
    private List<String> contentList = new ArrayList<>();
    private List<String> startTimeList = new ArrayList<>();
    private List<String> endTimeList = new ArrayList<>();
    private List<String> fileList = new ArrayList<>();

    private Context mContext;
    private  Intent intent;



    public RecommendContestAdapter(List<String> nameList, List<String> contentList, List<String> startTimeList, List<String> endTimeList, List<String> fileList, Context mContext) {
        this.nameList = nameList;
        this.contentList = contentList;
        this.startTimeList = startTimeList;
        this.endTimeList = endTimeList;
        this.fileList = fileList;
        this.mContext = mContext;
    }

    public void addtData(List<String> nameList,List<String> startTimeList,List<String> endTimeList,List<String> contentList,List<String> fileList) {
        if (null != nameList|null != nameList|null != startTimeList|null != endTimeList|null != contentList|null != fileList) {
            this.nameList.addAll(nameList);
            this.contentList.addAll(contentList);
            this.endTimeList.addAll(endTimeList);
            this.startTimeList.addAll(startTimeList);
            this.fileList.addAll(fileList);
            notifyDataSetChanged();
        }
    }

    public void setData(List<String> nameList,List<String> startTimeList,List<String> endTimeList,List<String> contentList,List<String> fileList) {
        if (null != nameList|null != nameList|null != startTimeList|null != endTimeList|null != contentList|null != fileList) {
            this.nameList.clear();
            this.nameList.addAll(nameList);

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
                holder.contestNameTv.setText(nameList.get(i));
                holder.startTimeTv.setText(startTimeList.get(i));
                holder.endTimeTv.setText(endTimeList.get(i));

                holder.contestCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        intent = new Intent(mContext, ConcreteRecommendContest.class);
                        intent.putExtra("content",contentList.get(i));
                        intent.putExtra("file",fileList.get(i));
                        mContext.startActivity(intent);
                    }
                });
        Log.d(TAG, "onBindViewHolder: name"+nameList.get(i));
    }

    @Override
    public int getItemCount() {
        return nameList.size();
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
}
