package com.dc.wibome.api;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.dc.wibome.constants.URLs;
import com.dc.wibome.constants.WeiboConstants;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
import com.sina.weibo.sdk.net.WeiboParameters;
import com.sina.weibo.sdk.openapi.AbsOpenAPI;

/**
 * 一般应用中网络使用volley网络框架，这里直接使用微博api就可以了
 */
public class BoreWeiboApi extends AbsOpenAPI {


    //因为不在主线程中new, 所以实例化Handler时，要声明Looper.getMainLooper()，表明在主线程
    private Handler mainLooperHandler = new Handler(Looper.getMainLooper());


    /**
     * 构造方法，使用各个 API 接口提供的服务前必须先获取 Token。
     *
     * @param context
     * @param appKey
     * @param accessToken
     */
    public BoreWeiboApi(Context context, String appKey, Oauth2AccessToken accessToken) {
        super(context, appKey, accessToken);
    }


    //为了在主线程中调用
    public void requestInMainLooper(String url, WeiboParameters params,
                                    String httpMethod, final RequestListener listener) {
        //调用下面继承自父类的方法requestAsync
        requestAsync(url, params, httpMethod, new RequestListener() {
            @Override
            public void onComplete(final String s) {
                //post 是hander的一种用法，把事件传递到主线程中
                mainLooperHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onComplete(s);
                    }
                });
            }

            @Override
            public void onWeiboException(final WeiboException e) {

                mainLooperHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onWeiboException(e);
                    }
                });
            }
        });


    }


    /** @param url        请求的地址
     * @param params     请求的参数
     * @param httpMethod 请求方法
     * @param listener   请求后的回调接口
     * 为了在主线程调用，重新写了一个方法
     * */
    @Override
    protected void requestAsync(String url, WeiboParameters params, String httpMethod, RequestListener listener) {
        //异步线程需要通过hander来更新ui,重新写了一个方法
        super.requestAsync(url, params, httpMethod, listener);
    }




    /**
     *获取用户信息(uid和screen_name二选一)
     *
     * @param uid
     *            根据用户ID获取用户信息
     * @param screen_name
     *            需要查询的用户昵称。
     * @param listener
     */
    public void usersShow(String uid, String screen_name, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(WeiboConstants.APP_KEY);
        if(!TextUtils.isEmpty(uid)) {
            params.add("uid", uid);
        } else if(!TextUtils.isEmpty(screen_name)) {
            params.add("screen_name", screen_name);
        }
        requestInMainLooper(URLs.usersShow, params, HTTPMETHOD_GET, listener);
    }


    /**
     * 获取某个用户自己的最新发表的微博列表(uid和screen_name二选一)
     *
     * @param uid
     *            需要查询的用户ID。
     * @param screen_name
     *            需要查询的用户昵称。
     * @param page
     *            返回结果的页码。(单页返回的记录条数，默认为20。)
     * @param listener
     */
    public void statusesUser_timeline(long uid, String screen_name, long page, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(WeiboConstants.APP_KEY);
        if(uid > 0) {
            params.add("uid", uid);
        } else if(!TextUtils.isEmpty(screen_name)) {
            params.add("screen_name", screen_name);
        }
        params.add("page", page);
        requestInMainLooper(URLs.statusesUser_timeline, params , HTTPMETHOD_GET, listener);
    }




    //官方文档得到的，用来查看关注用户的最新微博,外界获取微博信息就通过这个方法
    /**
     * 获取当前登录用户及其所关注用户的最新微博
     *
     * @param page
     *            返回结果的页码。(单页返回的记录条数，默认为20。)
     * @param listener
     */
    public void statusesHome_timeline(long page, RequestListener listener) {
        //请求的参数是封装在这个类中的 ,这里需传入appkey
        WeiboParameters parameters = new WeiboParameters(WeiboConstants.APP_KEY);
        parameters.add("page", page); //返回结果的页码
        //使用自己定义的方法，传递到主线程中
        requestInMainLooper(URLs.statusesHome_timeline, parameters, HTTPMETHOD_GET, listener);
    }


    /**
     * 根据微博ID返回某条微博的评论列表
     *
     * @param id
     *            需要查询的微博ID。
     * @param page
     *            返回结果的页码。(单页返回的记录条数，默认为50。)
     * @param listener
     */
    public void commentsShow(long id, long page, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(WeiboConstants.APP_KEY);
        params.add("id", id);
        params.add("page", page);
        requestInMainLooper(URLs.commentsShow, params , HTTPMETHOD_GET, listener);
    }



    /**
     * 对一条微博进行评论
     *
     * @param id
     *            需要评论的微博ID。
     * @param comment
     *            评论内容
     * @param listener
     */
    public void commentsCreate(long id, String comment, RequestListener listener) {
        WeiboParameters params = new WeiboParameters(WeiboConstants.APP_KEY);
        params.add("id", id);
        params.add("comment", comment);
        requestInMainLooper(URLs.commentsCreate, params , HTTPMETHOD_POST, listener);
    }


    /**
     * 发布或转发一条微博
     *
     * context
     * @param status
     *            要发布的微博文本内容。
     * @param imgFilePath
     *            要上传的图片文件路径(为空则代表发布无图微博)。
     * @param retweetedStatsId
     *            要转发的微博ID(<=0时为原创微博)。
     * @param listener
     */
    public void statusesSend(String status, String imgFilePath, long retweetedStatsId, RequestListener listener) {
        String url;
        WeiboParameters params = new WeiboParameters(WeiboConstants.APP_KEY);
        params.add("status", status);

        if(retweetedStatsId > 0) {  //有转发内容
            // 如果是转发微博,设置被转发者的id
            params.add("id", retweetedStatsId);
            url = URLs.statusesRepost;
        } else if(!TextUtils.isEmpty(imgFilePath)) {
            // 如果有图片,则调用upload接口且设置图片路径
            params.add("pic", imgFilePath);
            url = URLs.statusesUpload;
        } else {
            // 如果无图片,则调用update接口
            url = URLs.statusesUpdate;
        }
        requestInMainLooper(url, params,HTTPMETHOD_POST, listener);
    }









}
