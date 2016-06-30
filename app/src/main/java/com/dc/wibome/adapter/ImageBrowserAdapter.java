package com.dc.wibome.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dc.wibome.R;
import com.dc.wibome.entity.response.PicUrls;
import com.dc.wibome.utils.DisplayUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;

/**
 * 图片轮播 图片浏览器的适配器
 */
public class ImageBrowserAdapter extends PagerAdapter {

    private Activity context;
    private ArrayList<PicUrls> picUrls;
    private ArrayList<View> picViews;

    private ImageLoader mImageLoader;

    public ImageBrowserAdapter(Activity context, ArrayList<PicUrls> picUrls) {
        this.context = context;
        this.picUrls = picUrls;
        this.mImageLoader = ImageLoader.getInstance();
        initImgs();
    }
    //有多少图片就创建对应的布局
    private void initImgs() {
        picViews = new ArrayList<View>();

        for(int i=0; i<picUrls.size(); i++) {
            // 填充显示图片的页面布局
            View view = View.inflate(context, R.layout.item_image_browser, null);
            picViews.add(view);
        }
    }

    @Override
    public int getCount() {
        //需要无限轮播效果
        if(picUrls.size() > 1) {
            return Integer.MAX_VALUE;
        }
        return picUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        int index = position % picUrls.size(); //无论position多大，取余都会在0-8之间，共9个位置
        View view = picViews.get(index);  //获取选中视图
        final ImageView iv_image_browser = (ImageView) view.findViewById(R.id.iv_image_browser);
        PicUrls picUrl = picUrls.get(index); //集合中获取图片


        //这个不写加载原图时会出现异常
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }

        //判断是否显示原图
        String url = picUrl.isShowOriImag() ? picUrl.getOriginal_pic() : picUrl.getBmiddle_pic();

        //可以自定义图片的宽高,mImageLoader.displayImage();直接返回图片了

        mImageLoader.loadImage(url, new ImageLoadingListener() {

            @Override
            public void onLoadingStarted(String imageUri, View view) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onLoadingFailed(String imageUri, View view,
                                        FailReason failReason) {
                // TODO Auto-generated method stub

            }

            //动态加载图片的宽高
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                //计算原图的比例
                float scale = (float) loadedImage.getHeight() / loadedImage.getWidth();
                //获取整个屏幕的宽度和高度
                int screenWidthPixels = DisplayUtils.getScreenWidthPixels(context);
                int screenHeightPixels = DisplayUtils.getScreenHeightPixels(context);
                //屏幕*比例，获取图片实际的宽度
                int height = (int) (screenWidthPixels * scale);

                if (height < screenHeightPixels) {//不足全屏时，等于屏幕高
                    height = screenHeightPixels;
                }
                //图片宽高设置，及加载资源
                ViewGroup.LayoutParams params = iv_image_browser.getLayoutParams();
                params.height = height;  //ImageView高度自适应
                params.width = screenWidthPixels;//宽度为屏幕宽

                iv_image_browser.setImageBitmap(loadedImage);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                // TODO Auto-generated method stub

            }
        });




        container.addView(view);  //添加到Group中


        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }
    //这个方法不能丢，否则无法更新adapter中的图片 显示中等图片 变为显示原图
    @Override
    public int getItemPosition(Object object) {

        return POSITION_NONE;
    }

    //获取实际的集合图片
    public PicUrls getPic(int position) {
        return picUrls.get(position % picUrls.size());
    }

    //返回当前显示页面的Bitmap，用于保存操作
    public Bitmap getBitmap(int position) {
        Bitmap bitmap = null;
        View view = picViews.get(position % picViews.size());
        //获取当前页面中的imageView对象
        ImageView iv_image_browser = (ImageView) view.findViewById(R.id.iv_image_browser);
        //获取drawable，没法直接获取bitmap
        Drawable drawable = iv_image_browser.getDrawable();
        //如果是bitmap类型的drawable ，就转化对象
        if(drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            bitmap = bd.getBitmap();
        }

        return bitmap;
    }
}
