package com.example.northuniversity.schoolteam.modules.Team.tools;

import android.content.Context;
import android.media.Image;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.northuniversity.schoolteam.R;
import com.stx.xhb.androidx.XBanner;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RecyclerHolder> {
    private List<String> dataList = new ArrayList<>();
    private Context mContext;

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

        List<View> view = new ArrayList<>();
        public ImageView imageView = null;
        public  List<Integer> images = new ArrayList<>();

        //在这里实现view的添加，其实在别的地方能全部添加，但是需要添加具体一边以后能实现监控
        private RecyclerHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.name);
            imageView = itemView.findViewById(R.id.imageview);
            imageView.setOnClickListener(this);
            banner = itemView.findViewById(R.id.banner);
           initBanner(banner);
           banner.setOnBannerListener(this);

        }

        @Override
        public void onClick(View v) {

            Toast.makeText(mContext,"你点击了imageview",Toast.LENGTH_SHORT).show();
        }
        public  void initBanner(Banner banner){
            images.add(R.drawable.a);
            images.add(R.drawable.b);
            images.add(R.drawable.c);
            images.add(R.drawable.d);
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
    }

}
