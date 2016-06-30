package com.dc.wibome.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.dc.wibome.BaseActivity;
import com.dc.wibome.R;
import com.dc.wibome.constants.AccessTokenKeeper;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;

/*
* 欢迎界面，判断用户登录，和延时跳转
* */
public class SplashActivity extends BaseActivity {


    private static final int WHAT_INTENT2LOGIN = 1;
    private static final int WHAT_INTENT2MAIN = 2;   //区分的标识符
    private static final long SPLASH_DUR_TIME = 2000; //延时1秒

    private Oauth2AccessToken accessToken;

    private Handler handler = new Handler() {
       //接收传递过来的信息
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {  //what相当于收件人的地址
                case WHAT_INTENT2LOGIN:
                    intent2Activity(LoginActivity.class);
                    finish();
                    break;
                case WHAT_INTENT2MAIN:
                    intent2Activity(MainActivity.class);
                    finish();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        //从 SharedPreferences 读取 Token 信息
        accessToken = AccessTokenKeeper.readAccessToken(this);
        if(accessToken.isSessionValid()) { //对象有效，跳转到主界面，使用hander延时跳转
            handler.sendEmptyMessageDelayed(WHAT_INTENT2MAIN, SPLASH_DUR_TIME);
        } else {
            handler.sendEmptyMessageDelayed(WHAT_INTENT2LOGIN, SPLASH_DUR_TIME);
        }
    }
}
