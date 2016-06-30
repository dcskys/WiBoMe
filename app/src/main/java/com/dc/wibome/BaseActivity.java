package com.dc.wibome;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Toast;

import com.dc.wibome.api.BoreWeiboApi;
import com.dc.wibome.constants.AccessTokenKeeper;
import com.dc.wibome.constants.CommonConstants;
import com.dc.wibome.constants.WeiboConstants;
import com.dc.wibome.utils.ToastUtils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

import java.util.logging.Logger;

/**
 *封装一些常用的方法和对象
 */
public abstract class BaseActivity extends Activity {

//protected，只能被当前类和子类引用
    protected String TAG;
    protected BaseApplication application;
    protected SharedPreferences sp;

    /*protected BoreWeiboApi weiboApi;*/
    protected ImageLoader imageLoader;
    protected Gson gson;

    protected Oauth2AccessToken token;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TAG = this.getClass().getSimpleName();//得到是哪一个activity
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//页面竖屏显示

        application = (BaseApplication) getApplication();

        sp = getSharedPreferences(CommonConstants.SP_NAME, MODE_PRIVATE);//SharedPreferences初始化

       /* //实例化微博api
        token  = AccessTokenKeeper.readAccessToken(this);
        BoreWeiboApi api = new BoreWeiboApi(this, WeiboConstants.APP_KEY,token);
*/
        imageLoader = ImageLoader.getInstance();
        gson = new Gson();                 //基础类中进行初始化

    }

    protected void intent2Activity(Class<? extends Activity> tarActivity) {
        Intent intent = new Intent(this, tarActivity);
        startActivity(intent);
    }

    protected void showToast(String msg) {

        ToastUtils.showToast(this, msg, Toast.LENGTH_SHORT);
    }

    protected void showLog(String msg) {

        com.dc.wibome.utils.Logger.show(TAG, msg);
    }



}
