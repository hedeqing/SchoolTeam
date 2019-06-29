package com.example.northuniversity.schoolteam.modules.Team.tools;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.modules.Team.Inside_activity.ConcreteActivity;
import com.shehuan.niv.NiceImageView;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import static android.support.constraint.Constraints.TAG;

public class RecommendTeamAdapter extends RecyclerView.Adapter<RecommendTeamAdapter.RecyclerHolder> {

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
    private Context mContext;

    public RecommendTeamAdapter(List<String> team_id, List<String> menber_id, List<String> menber_quantity, List<String> category, List<String> description, List<String> team_picture, List<String> start_time, List<String> end_time, List<String> location, List<String> fare, List<String> favor_quantity, Context mContext) {
        this.team_id = team_id;
        this.menber_id = menber_id;
        this.menber_quantity = menber_quantity;
        this.category = category;
        this.description = description;
        this.team_picture = team_picture;
        this.start_time = start_time;
        this.end_time = end_time;
        this.location = location;
        this.fare = fare;
        this.favor_quantity = favor_quantity;
        this.mContext = mContext;
    }
    public void addtData(List<String> team_id, List<String> menber_id, List<String> menber_quantity, List<String> category, List<String> description, List<String> team_picture, List<String> start_time, List<String> end_time, List<String> location, List<String> fare, List<String> favor_quantity) {
        if (null != team_id) {
            this.category.addAll(category);
            this.description.addAll(description);
            this.end_time.addAll(end_time);
            this.start_time.addAll(start_time);
            this.fare.addAll(fare);
            this.location.addAll(location);
            this.favor_quantity.addAll(favor_quantity);
            this.menber_id .addAll(menber_id);
            this.team_id.addAll(team_id);
            this.team_picture.addAll(team_picture);
            this.menber_quantity.addAll(menber_quantity);
            notifyDataSetChanged();
        }
    }

    public void setData(List<String> team_id, List<String> menber_id, List<String> menber_quantity, List<String> category, List<String> description, List<String> team_picture, List<String> start_time, List<String> end_time, List<String> location, List<String> fare, List<String> favor_quantity) {
        if (null != team_id) {
            this.category.clear();
            this.description.clear();
            this.end_time.clear();
            this.menber_quantity.clear();
            this.team_picture.clear();
            this.menber_id.clear();
            this.favor_quantity.clear();
            this.location.clear();
            this.fare.clear();
            this.start_time.clear();
            this.team_id.clear();

            this.category.addAll(category);
            this.description.addAll(description);
            this.end_time.addAll(end_time);
            this.start_time.addAll(start_time);
            this.fare.addAll(fare);
            this.location.addAll(location);
            this.favor_quantity.addAll(favor_quantity);
            this.menber_id .addAll(menber_id);
            this.team_id.addAll(team_id);
            this.team_picture.addAll(team_picture);
            this.menber_quantity.addAll(menber_quantity);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_xrecycler, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: team_id"+team_id.get(position));
        Log.d(TAG, "onBindViewHolder: description"+description.get(position));
        Log.d(TAG, "onBindViewHolder: location"+location.get(position));
        holder.nameTv.setText(team_id.get(position));
        holder.titleTv.setText(description.get(position));
        holder.locationTv.setText(location.get(position));
        holder.timeTv.setText(start_time.get(position)+"到"+end_time.get(position));
        holder.cardViewTeam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ConcreteActivity.class);
                intent.putExtra("title",description.get(position));
                intent.putExtra("menber_quantity",menber_quantity.get(position));
                intent.putExtra("menber_id",menber_id.get(position));
                intent.putExtra("location",location.get(position));
                intent.putExtra("time",start_time.get(position)+"-"+end_time.get(position));
                intent.putExtra("fare",fare.get(position));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return team_id.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder  {
        private  TextView nameTv = null;
        private  TextView  titleTv = null;
        private  TextView locationTv = null;
        private TextView timeTv = null;
        private CardView cardViewTeam = null;

        //在这里实现view的添加，其实在别的地方能全部添加，但是需要添加具体一边以后能实现监控
        private RecyclerHolder(View itemView) {
            super(itemView);
            nameTv = itemView.findViewById(R.id.name_tv);
            titleTv = itemView.findViewById(R.id.title_tv);
            locationTv = itemView.findViewById(R.id.location_tv);
            timeTv = itemView.findViewById(R.id.time_tv);
            cardViewTeam = itemView.findViewById(R.id.card_view_team);
        }
    }

}
