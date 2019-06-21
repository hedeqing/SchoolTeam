package com.example.northuniversity.schoolteam.modules.Team.tools;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.northuniversity.schoolteam.R;
import com.example.northuniversity.schoolteam.modules.Team.Inside_activity.ClassificationActivity;
import com.example.northuniversity.schoolteam.modules.Team.Inside_activity.ReleaseActivity;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RecyclerHolder> {
    private List<String> dataList = new ArrayList<>();
    private Context mContext;
    private onMeanCallBack meanCallBack;

    public interface onMeanCallBack {
        //是在Activity中，执行接口时执行的方法，可获得传递的值
        void isDisMess(Intent intent);
    }
    public void setOnMeanCallBack(onMeanCallBack m) {
        this.meanCallBack = m;
    }

    public MyAdapter(Context mContext, List<String> dataList) {
        this.mContext = mContext;
        this.dataList = dataList;
    }

    public void addtData(List<String> dataList) {
        if (null != dataList) {
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public void setData(List<String> dataList) {
        if (null != dataList) {
            this.dataList.clear();
            this.dataList.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_xrecycler, parent, false);
        return new RecyclerHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerHolder holder, int position) {
//        holder.textView.setText(dataList.get(position));

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    class RecyclerHolder extends RecyclerView.ViewHolder implements View.OnClickListener, OnBannerListener {
        public TextView textView;
        public Banner banner = null;
        public ImageView gameView = null;
        public ImageView studyView = null;
        public ImageView contestiew = null;
        public ImageView travalView = null;


        List<View> view = new ArrayList<>();

        public  List<Integer> images = new ArrayList<>();

        //在这里实现view的添加，其实在别的地方能全部添加，但是需要添加具体一边以后能实现监控
        private RecyclerHolder(View itemView) {
            super(itemView);
            gameView = itemView.findViewById(R.id.iv_game);
            travalView = itemView.findViewById(R.id.iv_traval);
            contestiew = itemView.findViewById(R.id.iv_contest);
            studyView = itemView.findViewById(R.id.iv_study);


            gameView.setOnClickListener(this);
            travalView.setOnClickListener(this);
            contestiew.setOnClickListener(this);
            studyView.setOnClickListener(this);

            banner = itemView.findViewById(R.id.banner);
           initBanner(banner);
           banner.setOnBannerListener(this);
        }
        public  void initBanner(Banner banner){
            images.add(R.drawable.bluecup);
            images.add(R.drawable.liudingbei);
            images.add(R.drawable.jianmo);
            images.add(R.drawable.acm);
            banner = itemView.findViewById(R.id.banner);
            banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            //设置图片集合
            banner.setImages(images);
            //设置轮播时间
            banner.setDelayTime(2000);
            //banner设置方法全部调用完毕时最后调用
            banner.start();
        }

        @Override
        public void OnBannerClick(int position) {
            Toast.makeText(mContext,"你点击了："+position,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ClassificationActivity.class);

            switch (v.getId()){

                case R.id.iv_game:
                    intent.putExtra("page",0);
                    meanCallBack.isDisMess(intent);
                    break;
                case R.id.iv_study:

                    intent.putExtra("page",1);
                    meanCallBack.isDisMess(intent);
                    break;
                case R.id.iv_contest:
                    intent.putExtra("page",3);
                    meanCallBack.isDisMess(intent);
                    break;
                case R.id.iv_traval:
                    intent.putExtra("page",2);
                    meanCallBack.isDisMess(intent);
                    break;
                    default:
                        break;
            }
        }
    }

}
