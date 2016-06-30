package com.dc.wibome.activity;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.dc.wibome.BaseActivity;
import com.dc.wibome.R;
import com.dc.wibome.adapter.StatusAdapter;
import com.dc.wibome.api.BoreWeiboApi;
import com.dc.wibome.constants.AccessTokenKeeper;
import com.dc.wibome.constants.WeiboConstants;
import com.dc.wibome.entity.response.Status;
import com.dc.wibome.entity.response.StatusTimeLineResponse;
import com.dc.wibome.entity.response.User;
import com.dc.wibome.utils.ImageOptHelper;
import com.dc.wibome.utils.TitleBuilder;
import com.dc.wibome.widget.Pull2RefreshListView;
import com.dc.wibome.widget.UnderlineIndicatorView;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 呈现用户详细信息的
 */
public class UserInfoActivity extends BaseActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    // 标题栏
    private View title;
    private ImageView titlebar_iv_left;
    private TextView titlebar_tv;

    // headerView - 用户信息
    private View user_info_head;
    private ImageView iv_avatar;
    private TextView tv_name;
    private TextView tv_follows;
    private TextView tv_fans;
    private TextView tv_sign;

    // shadow_tab - 顶部悬浮的菜单栏
    private View shadow_user_info_tab;
    private RadioGroup shadow_rg_user_info;
    private UnderlineIndicatorView shadow_uliv_user_info;
    private View user_info_tab;
    private RadioGroup rg_user_info;
    private UnderlineIndicatorView uliv_user_info;

    // headerView - 添加至列表中作为header的菜单栏
    private ImageView iv_user_info_head;
    private Pull2RefreshListView plv_user_info;
    private View footView;

    // 用户相关信息
    private boolean isCurrentUser; //判断是个人还是他人的详细信息
    private User user;
    private String userName;

    // 个人微博列表
    private List<Status> statuses = new ArrayList<Status>();
    private StatusAdapter statusAdapter;

    private long curPage = 1;
    // 背景图片最小高度
    private int minImageHeight = -1;
    // 背景图片最大高度
    private int maxImageHeight = -1;

    private int curScrollY;

    private Oauth2AccessToken token;
    private BoreWeiboApi weiboApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_info);

        //实例化微博api
        token  = AccessTokenKeeper.readAccessToken(this);
        weiboApi = new BoreWeiboApi(this, WeiboConstants.APP_KEY,token);

        //传递过来该用户的昵称
        userName = getIntent().getStringExtra("userName");
        //如果传递过来为空，就说明是本人访问这个界面
        if(TextUtils.isEmpty(userName)) {
            isCurrentUser = true;
            user = application.currentUser;//获取全局的用户信息
        }

        initView();

        loadData(); //加载数据
    }



    private void loadData() {

        if(isCurrentUser) {
            // 如果是当前授权用户,直接设置信息
            setUserInfo();
        } else {
            // 如果是查看他人,调用获取用户信息接口
            loadUserInfo();
        }

        // 加载用户所属微博列表 ，第一次加载
        loadStatuses(1);
    }

    //查看他人  。api限制，未授权，所以无法查看他人信息，得不到json数据，都为0
    private void loadUserInfo() {
        weiboApi.usersShow("", userName, new RequestListener() {
            @Override
            public void onComplete(String s) {
                // 获取用户信息并设置
                user = new Gson().fromJson(s, User.class);
                setUserInfo();
            }

            @Override
            public void onWeiboException(WeiboException e) {
                showLog("status comments = " + e.toString());
                showToast("未授权api限制无法呈现" + e.toString());
            }
        });


    }


    ////设置当前用户
    private void setUserInfo() {

        if(user == null) {
            return;
        }
        //获取BaseApplication中用户  ，用户头文件头像等
        tv_name.setText(user.getName());
        titlebar_tv.setText(user.getName());

        imageLoader.displayImage(user.getAvatar_large(), new ImageViewAware(iv_avatar),
                ImageOptHelper.getAvatarOptions());

        tv_follows.setText("关注 " + user.getFriends_count());
        tv_fans.setText("粉丝 " + user.getFollowers_count());
        tv_sign.setText("简介:" + user.getDescription());
    }

    private void initView() {

        title = new TitleBuilder(this)
                .setTitleBgRes(R.drawable.userinfo_navigationbar_background)
                .setLeftImage(R.drawable.navigationbar_back_sel)
                .setLeftOnClickListener(this)
                .build();

        // 获取头部标题栏信息,需要时进行修改
        titlebar_iv_left = (ImageView) title.findViewById(R.id.titlebar_iv_left);
        titlebar_tv = (TextView) title.findViewById(R.id.titlebar_tv);

        initInfoHead();
        initTab();
        initListView();

    }



    // 初始化用户信息
    private void initInfoHead() {
        //用户的背景图片
        iv_user_info_head = (ImageView) findViewById(R.id.iv_user_info_head);
         //头部headView总布局，高为184dp
        user_info_head = View.inflate(this, R.layout.user_info_head, null);

        iv_avatar = (ImageView) user_info_head.findViewById(R.id.iv_avatar);
        tv_name = (TextView) user_info_head.findViewById(R.id.tv_name);
        tv_follows = (TextView) user_info_head.findViewById(R.id.tv_follows);
        tv_fans = (TextView) user_info_head.findViewById(R.id.tv_fans);
        tv_sign = (TextView) user_info_head.findViewById(R.id.tv_sign);
    }

    // 初始化菜单栏
    private void initTab() {
        // 悬浮显示的菜单栏 总布局
        shadow_user_info_tab = findViewById(R.id.user_info_tab);

        shadow_rg_user_info = (RadioGroup) findViewById(R.id.rg_user_info);
        shadow_uliv_user_info = (UnderlineIndicatorView) findViewById(R.id.uliv_user_info);

        shadow_rg_user_info.setOnCheckedChangeListener(this);//自定义的动画控件
        shadow_uliv_user_info.setCurrentItemWithoutAnim(1);  //使用无动画样式

        // 添加到列表中的菜单栏   和上面布局一样  主页微博相册管理
        user_info_tab = View.inflate(this, R.layout.user_info_tab, null);
        rg_user_info = (RadioGroup) user_info_tab.findViewById(R.id.rg_user_info);
        uliv_user_info = (UnderlineIndicatorView) user_info_tab.findViewById(R.id.uliv_user_info);

        rg_user_info.setOnCheckedChangeListener(this);
        uliv_user_info.setCurrentItemWithoutAnim(1);  //使用无动画样式
    }

    //初始化列表
    @SuppressLint("NewApi")
    private void initListView() {

        plv_user_info = (Pull2RefreshListView) findViewById(R.id.plv_user_info);
        initLoadingLayout();//设置list样式

        footView = View.inflate(this, R.layout.footview_loading, null);
        final ListView lv = plv_user_info.getRefreshableView();

        statusAdapter = new StatusAdapter(this, statuses);
        plv_user_info.setAdapter(statusAdapter);

        //添加头文件
        lv.addHeaderView(user_info_head);
        lv.addHeaderView(user_info_tab);

        //下拉刷新
        plv_user_info.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                loadStatuses(1);
            }
        });

        //加载到底部，上拉
        plv_user_info.setOnLastItemVisibleListener(
                new PullToRefreshBase.OnLastItemVisibleListener() {

                    @Override
                    public void onLastItemVisible() {

                        loadStatuses(curPage + 1);
                    }
                });


        //自带的滚动监听，只是针对列表部分
        plv_user_info.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            //列表滚动事件
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //列表滚动时，图片跟随滚动
                iv_user_info_head.layout(0,
                        user_info_head.getTop(),
                        iv_user_info_head.getWidth(),
                        user_info_head.getTop() + iv_user_info_head.getHeight());

                if (user_info_head.getBottom() < title.getBottom()) {  //菜单栏移动到顶部时
                    shadow_user_info_tab.setVisibility(View.VISIBLE); //显示悬浮菜单栏
                    title.setBackgroundResource(R.drawable.navigationbar_background); //修改头部标题栏
                    titlebar_iv_left.setImageResource(R.drawable.navigationbar_back_sel);
                    titlebar_tv.setVisibility(View.VISIBLE);
                } else {
                    shadow_user_info_tab.setVisibility(View.GONE);
                    title.setBackgroundResource(R.drawable.userinfo_navigationbar_background);
                    titlebar_iv_left.setImageResource(R.drawable.userinfo_navigationbar_back_sel);
                    titlebar_tv.setVisibility(View.GONE);
                }
            }
        });

        //自定义的接口，对整个控件进行滚动监听，包括下拉刷新的部分
        plv_user_info.setOnPlvScrollListener(new Pull2RefreshListView.OnPlvScrollListener() {
            @Override
            public void onScrollChanged(int l, int t, int oldl, int oldt) {
                int scrollY = curScrollY = t; //y轴滚动位置

                if (minImageHeight == -1) {
                    minImageHeight = iv_user_info_head.getHeight();//获取背景图片的宽度
                }

                if (maxImageHeight == -1) {
                    Rect rect = iv_user_info_head.getDrawable().getBounds();//获取整个图片的边界值
                    maxImageHeight = rect.bottom - rect.top; //图片最大高度
                }
                //layout（左，上，右，下） 方法可以重新规定控件的大小位置
                if (minImageHeight - scrollY < maxImageHeight) {//所以用-号。
                    //滚动时小于最大高度，控件宽度一直在增加
                    iv_user_info_head.layout(0, 0, iv_user_info_head.getWidth(),
                            minImageHeight - scrollY);
                    //概念理解，向上滚动其实列表是向下滚动的，item不停地在增加
                    //向下滚动时，scrollY其实不断向负数滚动。0.-50.-100
                } else {
                    //图片完全显示时，图片跟随列表一起滚动  上 滚动距离-图片差值 ，下 滚动距离-图片差值+整个控件的高度
                    iv_user_info_head.layout(0,
                            -scrollY - (maxImageHeight - minImageHeight),
                            iv_user_info_head.getWidth(),
                            -scrollY - (maxImageHeight - minImageHeight) + iv_user_info_head.getHeight());
                }
            }
        });

        // ，修复下拉刷新中一点小问题，可以不用管
        iv_user_info_head.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom,
                                       int oldLeft, int oldTop, int oldRight, int oldBottom) {
                //当前滚动状态和实际状态不一样时，保持原有状态
                if (curScrollY == bottom - oldBottom) {
                    iv_user_info_head.layout(0, 0,
                            iv_user_info_head.getWidth(),
                            oldBottom);
                }
            }
        });


    }

      //接口升级后，对未授权本应用的uid，将无法获取其个人简介、认证原因、粉丝数、关注数、微博数及最近一条微博内容。
    private void loadStatuses(final long requestPage){

        //user application全局获取的登陆用户，或获取他人的 ，//userName传递过来该用户的昵称
        weiboApi.statusesUser_timeline(user == null ? -1 : user.getId(), userName, requestPage, new RequestListener() {
            @Override
            public void onComplete(String s) {
                showLog("用户信息= " + s);

                if (requestPage == 1) {
                    statuses.clear();
                }

                curPage = requestPage;   //更新一下当前页

                addStatus(gson.fromJson(s, StatusTimeLineResponse.class));
                plv_user_info.onRefreshComplete();//下拉刷新已完成
            }

            @Override
            public void onWeiboException(WeiboException e) {
                showLog("未授权 = " + e.toString());
            }
        });


    }

    private void addStatus(StatusTimeLineResponse response) {
      //列表不存在就添加到列表中
        for(Status status : response.getStatuses()) {
            if(!statuses.contains(status)) {
                statuses.add(status);
            }
        }

        statusAdapter.notifyDataSetChanged();

        if(curPage < response.getTotal_number()) { //当前页数小于总页数，加载底部布局
            addFootView(plv_user_info, footView);
        } else {
            removeFootView(plv_user_info, footView);
        }

    }

    private void addFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if(lv.getFooterViewsCount() == 1) {
            lv.addFooterView(footView);
        }
    }

    private void removeFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if(lv.getFooterViewsCount() > 1) {
            lv.removeFooterView(footView);
        }
    }

    //对list 设置样式 ，pulltoRrfresh
    private void initLoadingLayout() {

           // 获取下拉刷新的头部分
        ILoadingLayout loadingLayout = plv_user_info.getLoadingLayoutProxy();
        //拖动刷新
        loadingLayout.setPullLabel("");
        //刷新时文字
        loadingLayout.setRefreshingLabel("");
        //释放时文字内容
        loadingLayout.setReleaseLabel("");
        //设置加载图片，可以放一个透明的图片
        loadingLayout.setLoadingDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_iv_left:
                finish();
                break;
            default:
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {


        // 同步悬浮和列表中的标题栏状态
        syncRadioButton(group, checkedId);

        //这种写法代码太多
//		switch (checkedId) {
//		case R.id.rb_info:
//			if(shadow_user_info_tab.getVisibility() == View.VISIBLE) {
//				rb_info.setChecked(true);
//				uliv_user_info.setCurrentItemWithoutAnim(0);
//
//				shadow_rb_info.setChecked(true);
//				shadow_uliv_user_info.setCurrentItem(0);
//			} else {
//				rb_info.setChecked(true);
//				uliv_user_info.setCurrentItem(0);
//
//				shadow_rb_info.setChecked(true);
//				shadow_uliv_user_info.setCurrentItemWithoutAnim(0);
//			}
//			break;
//		case R.id.rb_status:
//			if(shadow_user_info_tab.getVisibility() == View.VISIBLE) {
//				rb_status.setChecked(true);
//				uliv_user_info.setCurrentItemWithoutAnim(1);
//
//				shadow_rb_status.setChecked(true);
//				shadow_uliv_user_info.setCurrentItem(1);
//			} else {
//				rb_status.setChecked(true);
//				uliv_user_info.setCurrentItem(1);
//
//				shadow_rb_status.setChecked(true);
//				shadow_uliv_user_info.setCurrentItemWithoutAnim(1);
//			}
//			break;
//		case R.id.rb_photos:
//			if(shadow_user_info_tab.getVisibility() == View.VISIBLE) {
//				rb_photos.setChecked(true);
//				uliv_user_info.setCurrentItemWithoutAnim(2);
//
//				shadow_rb_photos.setChecked(true);
//				shadow_uliv_user_info.setCurrentItem(2);
//			} else {
//				rb_photos.setChecked(true);
//				uliv_user_info.setCurrentItem(2);
//
//				shadow_rb_photos.setChecked(true);
//				shadow_uliv_user_info.setCurrentItemWithoutAnim(2);
//			}
//			break;
//		case R.id.rb_manager:
//			if(shadow_user_info_tab.getVisibility() == View.VISIBLE) {
//				rb_manager.setChecked(true);
//				uliv_user_info.setCurrentItemWithoutAnim(3);
//
//				shadow_rb_manager.setChecked(true);
//				shadow_uliv_user_info.setCurrentItem(3);
//			} else {
//				rb_manager.setChecked(true);
//				uliv_user_info.setCurrentItem(3);
//
//				shadow_rb_manager.setChecked(true);
//				shadow_uliv_user_info.setCurrentItemWithoutAnim(3);
//			}
//			break;
//
//		default:
//			break;
//		}

    }

    //底部菜单栏和悬浮栏同步方法
    private void syncRadioButton(RadioGroup group, int checkedId) {

        int index = group.indexOfChild(group.findViewById(checkedId));//获取RadioButton选中的控件的位置

        if(shadow_user_info_tab.getVisibility() == View.VISIBLE) {//判断悬浮菜单栏的显示状态

            shadow_uliv_user_info.setCurrentItem(index); //动画指示器设置有动画效果
            //RadioGroup中的RadioButton同步选中
            ((RadioButton)rg_user_info.findViewById(checkedId)).setChecked(true);

            uliv_user_info.setCurrentItemWithoutAnim(index); //隐藏标题栏调用没有动画

        } else {
            uliv_user_info.setCurrentItem(index);//动画指示器设置有动画效果

            ((RadioButton)shadow_rg_user_info.findViewById(checkedId)).setChecked(true);
            shadow_uliv_user_info.setCurrentItemWithoutAnim(index);//隐藏标题栏调用没有动画
        }

    }
}
