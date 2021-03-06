package com.dc.wibome.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dc.wibome.R;
import com.dc.wibome.entity.response.Emotion;

import java.util.List;

/**
 * ViewPager 中GridView的适配器
 */
public class EmotionGvAdapter extends BaseAdapter {

    private Context context;
    private List<String> emotionNames;
    private int itemWidth;

    public EmotionGvAdapter(Context context, List<String> emotionNames, int itemWidth) {
        this.context = context;
        this.emotionNames = emotionNames;
        this.itemWidth = itemWidth;
    }

    @Override
    public int getCount() {
        return emotionNames.size() + 1; //最后用于显示删除符号
    }

    @Override
    public String getItem(int position) {
        return emotionNames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView iv = new ImageView(context);
        AbsListView.LayoutParams params = new AbsListView.LayoutParams(itemWidth, itemWidth);
        iv.setPadding(itemWidth/8, itemWidth/8, itemWidth/8, itemWidth/8);//设置这个会让点击大小大于呈现大小
        iv.setLayoutParams(params);

        if(position == getCount() - 1) {   //从0开始，指最后一个值
            iv.setImageResource(R.drawable.emotion_delete_icon);
        } else {
            String emotionName = emotionNames.get(position);
            iv.setImageResource(Emotion.getImgByName(emotionName));  //根据key 呈现对应的图片
        }


        return iv;
    }

}
