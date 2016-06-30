package com.dc.wibome.adapter;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

/**
 * 表情面板ViewPager的适配器，这里使用fragment 就要使用FragmentPagerAdapter
 */
public class EmotionPagerAdapter extends PagerAdapter {

    private List<GridView> gvs;

    public EmotionPagerAdapter(List<GridView> gvs) {
        this.gvs = gvs;
    }

    @Override
    public int getCount() {
        return gvs.size();
    }

    //view是否来自于对象，固定写法
    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    //销毁一个页面
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
       // ((ViewPager) container).removeView(gvs.get(position));
        container.removeView(gvs.get(position));
    }

    //实例化一个页面
    @Override
    public Object instantiateItem(ViewGroup container, int position) {

       // ((ViewPager) container).addView(gvs.get(position));
        container.addView(gvs.get(position));
        return gvs.get(position);
    }

}

