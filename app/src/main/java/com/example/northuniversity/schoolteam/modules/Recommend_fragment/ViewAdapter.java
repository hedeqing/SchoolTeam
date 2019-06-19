package com.example.northuniversity.schoolteam.modules.Recommend_fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class ViewAdapter extends PagerAdapter {
    public List<View> list_view;
    private List<String> list_Title;                              //tab名的列表
    private int[] tabImg;
    private Context context;

    public ViewAdapter(Context context,List<View> list_view,List<String> list_Title,int[] tabImg) {
        this.list_view = list_view;
        this.list_Title = list_Title;
        this.tabImg = tabImg;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list_view.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(list_view.get(position), 0);
        return list_view.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(list_view.get(position));
    }

    /**
     * 此方法是给tablayout中的tab赋值的，就是显示名称,并且给其添加icon的图标
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {

        //这段被注的代码，是只显示文字，不显示图标
        //return  list_Title.get(position % list_Title.size());

        Drawable dImage = context.getResources().getDrawable(tabImg[position]);
        dImage.setBounds(0, 0, dImage.getIntrinsicWidth(), dImage.getIntrinsicHeight());
        //这里前面加的空格就是为图片显示
        SpannableString sp = new SpannableString("  "+ list_Title.get(position));
        ImageSpan imageSpan = new ImageSpan(dImage, ImageSpan.ALIGN_BOTTOM);
        sp.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return  sp;
    }

}
