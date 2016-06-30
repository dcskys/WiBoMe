package com.dc.wibome.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dc.wibome.BaseApplication;
import com.dc.wibome.BaseFragment;
import com.dc.wibome.R;
import com.dc.wibome.activity.UserInfoActivity;
import com.dc.wibome.adapter.UserItemAdapter;
import com.dc.wibome.api.BoreWeiboApi;
import com.dc.wibome.constants.AccessTokenKeeper;
import com.dc.wibome.constants.WeiboConstants;
import com.dc.wibome.entity.response.User;
import com.dc.wibome.entity.response.UserItem;
import com.dc.wibome.utils.Logger;
import com.dc.wibome.utils.TitleBuilder;
import com.dc.wibome.widget.WrapHeightListView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的中心
 */
public class UserFragment extends BaseFragment {

    private LinearLayout ll_userinfo;

    private ImageView iv_avatar;
    private TextView tv_subhead;
    private TextView tv_caption;

    private TextView tv_status_count;
    private TextView tv_follow_count;
    private TextView tv_fans_count;

    private WrapHeightListView lv_user_items;

    private User user;
    private View view;

    private UserItemAdapter adapter;
    private List<UserItem> userItems;   //列表数据

    private BoreWeiboApi weiboApi;
    private Oauth2AccessToken mAccessToken;
    private ImageLoader imageLoader;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_user, null);

        //实例化微博api
        mAccessToken  = AccessTokenKeeper.readAccessToken(activity);
        weiboApi = new BoreWeiboApi(activity, WeiboConstants.APP_KEY,mAccessToken);
        imageLoader = ImageLoader.getInstance();

        initView();

        setItem(); //添加列表数据

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // show/hide方法不会走生命周期  ，这个方法不会执行
        System.out.println("user frag onStart()");
    }

    //所以通过隐藏 监听的变化 来实现  true 表示隐藏
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if(!hidden) {

            weiboApi.usersShow(mAccessToken.getUid(), "", new RequestListener() {
                @Override
                public void onComplete(String s) {
                    //服务器获取当前返回的json数据
                    //将获取的用户信息存放在全局变量中
                    BaseApplication application = (BaseApplication) activity.getApplication();
                    application.currentUser = user = new Gson().fromJson(s, User.class);
                    setUserInfo();//用户信息呈现

                }

                @Override
                public void onWeiboException(WeiboException e) {
                    Logger.show("REQUEST onIOException", e.toString());
                }
            });

        }


    }

    private void setUserInfo() {

        tv_subhead.setText(user.getName());
        tv_caption.setText("简介:" + user.getDescription());
        imageLoader.displayImage(user.getAvatar_large(), iv_avatar);
        tv_status_count.setText("" + user.getStatuses_count());
        tv_follow_count.setText("" + user.getFriends_count());
        tv_fans_count.setText("" + user.getFollowers_count());

    }

    private void initView() {
        // 标题栏
        new TitleBuilder(view)
                .setTitleText("我")
                .build();

        // 用户信息总布局
        ll_userinfo = (LinearLayout) view.findViewById(R.id.ll_userinfo);
         //点击查看用户信息
        ll_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, UserInfoActivity.class);
                startActivity(intent);
            }
        });
        //用户头像
        iv_avatar = (ImageView) view.findViewById(R.id.iv_avatar);
        tv_subhead = (TextView) view.findViewById(R.id.tv_subhead);
        tv_caption = (TextView) view.findViewById(R.id.tv_caption);

        // 互动信息栏: 微博数、关注数、粉丝数
        tv_status_count = (TextView) view.findViewById(R.id.tv_status_count);
        tv_follow_count = (TextView) view.findViewById(R.id.tv_follow_count);
        tv_fans_count = (TextView) view.findViewById(R.id.tv_fans_count);

        // 设置栏列表，呈现列表数据
        lv_user_items = (WrapHeightListView) view.findViewById(R.id.lv_user_items);
        userItems = new ArrayList<UserItem>();
        adapter = new UserItemAdapter(activity, userItems);
        lv_user_items.setAdapter(adapter);
    }



    private void setItem() {
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_1, "新的朋友", ""));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_2, "微博等级", "Lv99"));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_3, "编辑资料", ""));
        userItems.add(new UserItem(true, R.mipmap.push_icon_app_small_4, "我的相册", "(188)"));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_5, "我的点评", ""));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_4, "我的赞", "(9999)"));
        userItems.add(new UserItem(true, R.mipmap.push_icon_app_small_3, "微博支付", ""));
        userItems.add(new UserItem(false, R.mipmap.push_icon_app_small_2, "微博运动", "步数、卡路里、跑步轨迹"));
        userItems.add(new UserItem(true, R.mipmap.push_icon_app_small_1, "更多", "收藏、名片"));
        adapter.notifyDataSetChanged();



    }


}
