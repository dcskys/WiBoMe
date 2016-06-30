package com.dc.wibome;

import android.app.Application;
import android.content.Context;

import com.dc.wibome.entity.response.User;
import com.dc.wibome.utils.ImageOptHelper;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 第三方控件初始化一般在这里操作
 */
public class BaseApplication extends Application{

    public User currentUser; //全局的用户信息

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化 图片缓存框架
        initImageLoader(this);
    }


    // 初始化图片处理
    private void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this); 不知道配置信息，可以使用这个默认信息
        // 初始化Universal-ImageLoader这个第三方库
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)   //线程池加载的数量
                .discCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的uri用MD5加密
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .defaultDisplayImageOptions(ImageOptHelper.getImgOptions()) //自定义图片缓存路径
                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);   //正式加载运行的配置
    }

}
