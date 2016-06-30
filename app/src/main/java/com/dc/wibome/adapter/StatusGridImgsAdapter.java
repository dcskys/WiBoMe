package com.dc.wibome.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.dc.wibome.R;
import com.dc.wibome.entity.response.PicUrls;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;

/**
 * GridView的适配器，全部用来呈现微博图片
 */
public class StatusGridImgsAdapter extends BaseAdapter {


    private Context context;
    private ArrayList<PicUrls> datas;
    private ImageLoader imageLoader;


    //构造方法
    public StatusGridImgsAdapter(Context context, ArrayList<PicUrls> datas) {
        this.context = context;
        this.datas = datas;
        imageLoader = ImageLoader.getInstance(); //初始化
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public PicUrls getItem(int i) {

        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {

        return i;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder;   //优化机制

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_grid_image, null);
            holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        GridView gv = (GridView) viewGroup;//获取该适配器
        int horizontalSpacing = gv.getHorizontalSpacing();//获得gv之间的水平间距
        int numColumns = gv.getNumColumns();//获取列数
        //每一个宽度 = （总宽度 - 所有的水平间距-左右两边的内边距）/列数
        int itemWidth = (gv.getWidth() - (numColumns-1) * horizontalSpacing
                - gv.getPaddingLeft() - gv.getPaddingRight()) / numColumns;

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(itemWidth, itemWidth);
        holder.iv_image.setLayoutParams(params); //设置每一个图片都是相同宽高的

        PicUrls urls = getItem(position); //获取指定的图片集，
        //加载图片到ImageView
        imageLoader.displayImage(urls.getThumbnail_pic(), holder.iv_image);

        return convertView;
    }

    public static class ViewHolder {
        public ImageView iv_image;
    }
}
