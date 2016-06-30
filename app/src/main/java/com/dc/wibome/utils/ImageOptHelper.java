package com.dc.wibome.utils;

import android.graphics.Bitmap;

import com.dc.wibome.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * ImageLoader第三方库自定义的图片缓存地址，ImageLoader的标准写法
 */
public class ImageOptHelper {

    public static DisplayImageOptions getImgOptions() {
        DisplayImageOptions imgOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc()  //缓存sd卡
                .cacheInMemory()  //缓存内存
                .bitmapConfig(Bitmap.Config.RGB_565) //设置图片解码方式，颜色类型
                .showStubImage(R.mipmap.timeline_image_loading) //加载时显示图片
                .showImageForEmptyUri(R.mipmap.timeline_image_loading)//空地址图片
                .showImageOnFail(R.mipmap.timeline_image_failure) //失败图片
                .build();
        return imgOptions;
    }

    //头像的设置Option  ,头像的缓存信息
    public static DisplayImageOptions getAvatarOptions() {
        DisplayImageOptions	avatarOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc()
                .cacheInMemory()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showStubImage(R.mipmap.avatar_default)
                .showImageForEmptyUri(R.mipmap.avatar_default)
                .showImageOnFail(R.mipmap.avatar_default)
                .displayer(new RoundedBitmapDisplayer(999)) //图片以圆角边的形式进行展现
                .build();
        return avatarOptions;
    }

    //指定圆角半径的方法
    public static DisplayImageOptions getCornerOptions(int cornerRadiusPixels) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheOnDisc()
                .cacheInMemory()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .showStubImage(R.mipmap.timeline_image_loading)
                .showImageForEmptyUri(R.mipmap.timeline_image_loading)
                .showImageOnFail(R.mipmap.timeline_image_loading)
                .displayer(new RoundedBitmapDisplayer(cornerRadiusPixels)).build();
        return options;
    }


}
