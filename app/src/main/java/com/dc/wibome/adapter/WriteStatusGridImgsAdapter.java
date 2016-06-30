package com.dc.wibome.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dc.wibome.R;

import java.util.ArrayList;

/**
 * 发送微博页面添加图片的适配器
 */
public class WriteStatusGridImgsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Uri> datas;  //本地图片集合
    private GridView gv;

    public WriteStatusGridImgsAdapter(Context context, ArrayList<Uri> datas, GridView gv) {
        this.context = context;
        this.datas = datas;
        this.gv = gv;
    }


    @Override
    public int getCount() {  //加一意味着末尾多出来一个，用于显示x图片

        return datas.size() > 0 ? datas.size() + 1 : 0;
    }



    @Override
    public Uri getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;  //优化机制
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_grid_image, null);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            holder.iv_delete_image = (ImageView) convertView.findViewById(R.id.iv_delete_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //高度自适应，这里是获得图片的宽度是自适应的
        int horizontalSpacing = gv.getHorizontalSpacing();
        int width = (gv.getWidth() - horizontalSpacing * 2
                - gv.getPaddingLeft() - gv.getPaddingRight()) / 3;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, width);
        holder.iv_image.setLayoutParams(params);


        if(position < getCount() - 1) {  //正常的item 显示图片
            // set data
            Uri item = getItem(position);

            holder.iv_image.setImageURI(item);//呈现指定的图片

            holder.iv_delete_image.setVisibility(View.VISIBLE); //显示图片右上角的x点击事件
            holder.iv_delete_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    datas.remove(position); //集合中移除图片uri
                    notifyDataSetChanged();
                }
            });
        } else {  //最后一个位置显示 一个+ 号
            //设置可变的背景图片
            holder.iv_image.setImageResource(R.drawable.compose_pic_add_more);
            holder.iv_delete_image.setVisibility(View.GONE);
        }

        return convertView;
    }


    public static class ViewHolder {
        public ImageView iv_image;
        public ImageView iv_delete_image;
    }
}
