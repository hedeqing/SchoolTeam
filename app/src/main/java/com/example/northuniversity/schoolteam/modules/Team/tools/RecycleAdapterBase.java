package com.example.northuniversity.schoolteam.modules.Team.tools;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class RecycleAdapterBase<T, A extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<A> {
    public List<T> mList = new ArrayList<>();
    public onItemClickListener<T> onClickListener;

    @Override
    public int getItemCount() {
        if (mList == null)
            return -1;
        return mList.size();
    }


    /**
     * @param list
     */
    public void appentToList(List<T> list) {
        mList.addAll(list);
        notifyDataSetChanged();
        list = null;
    }

    public void appentToList(T t) {
        mList.add(t);
        notifyDataSetChanged();
    }

    public void appentToListFirst(T t) {
        mList.add(0, t);
        notifyDataSetChanged();
    }

    public void appentToListFirst(List<T> list) {
        mList.addAll(0, list);
        notifyDataSetChanged();
        list = null;
    }

    /**
     * @param list
     */
    public void changeList(List<T> list) {
        mList = list == null ? new ArrayList<T>() : list;
        notifyDataSetChanged();
    }

    /**
     * 清空列表
     */
    public void clearList() {
        mList.clear();
        notifyDataSetChanged();
    }

    /**
     * 删除列表中某一项
     */
    public void removeObject(T t) {
        mList.remove(t);
        notifyDataSetChanged();
        t = null;
    }

    public void updateObject(T t) {
        mList.set(mList.indexOf(t), t);
        notifyDataSetChanged();
    }

    public void setOnClickListener(onItemClickListener<T> onClickListener) {

        this.onClickListener = onClickListener;
    }

    public interface onItemClickListener<T> {

        public void onItemClick(int position, T object);
    }

    protected T getObject(int position) {

        if (mList != null && mList.size() > position) {
            return mList.get(position);
        }
        return null;
    }
}
