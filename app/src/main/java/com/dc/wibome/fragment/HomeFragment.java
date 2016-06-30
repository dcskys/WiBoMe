package com.dc.wibome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dc.wibome.BaseFragment;
import com.dc.wibome.R;
import com.dc.wibome.adapter.StatusAdapter;
import com.dc.wibome.api.BoreWeiboApi;
import com.dc.wibome.constants.AccessTokenKeeper;
import com.dc.wibome.constants.WeiboConstants;
import com.dc.wibome.entity.response.Status;
import com.dc.wibome.entity.response.StatusTimeLineResponse;
import com.dc.wibome.utils.Logger;
import com.dc.wibome.utils.TitleBuilder;
import com.dc.wibome.utils.ToastUtils;
import com.google.gson.Gson;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 主页面
 */
public class HomeFragment extends BaseFragment {

    private View view;
    private PullToRefreshListView plv_home;
    private View footView;

    private StatusAdapter adapter;
    private List<Status> statuses = new ArrayList<Status>();
    private int curPage = 1;

    private Oauth2AccessToken token;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        initView();
        loadData(1); //加载最新页
        return view;
    }


    private void initView() {
        view = View.inflate(activity, R.layout.frag_home, null);

        //使用自定义的标题栏构造器，使用在fragment中使用的构造方法
        new TitleBuilder(view).setTitleText("首页");

        plv_home = (PullToRefreshListView) view.findViewById(R.id.lv_home);
        adapter = new StatusAdapter(activity, statuses);
        plv_home.setAdapter(adapter);


        //下拉刷新 ，加载最新界面
        plv_home.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {

                loadData(1);
            }
        });

        //最后一个item 显示后 ，自动加载下一页，和上拉加载差不多
        plv_home.setOnLastItemVisibleListener(new PullToRefreshBase.OnLastItemVisibleListener() {

            @Override
            public void onLastItemVisible() {

                loadData(curPage + 1);
            }
        });
           //ProgressBar和文字加载更多。。
        footView = View.inflate(activity, R.layout.footview_loading, null);
    }


    /*加载数据的时候，希望能停留在一开始的地方*/
    private void loadData(final int page) {

        //实例化微博api
        token  = AccessTokenKeeper.readAccessToken(activity);
        BoreWeiboApi api = new BoreWeiboApi(activity, WeiboConstants.APP_KEY,token);

        //page  想要获取的页数
        api.statusesHome_timeline( page, new RequestListener() {
            @Override
            public void onComplete(String s) {
                //就能获取微博返回的json数据了  s

                if(page == 1) {         //当加载最新界面时，清空所有集合
                    statuses.clear();
                }

                curPage = page;   //更新一下当前页

                //将解析的json 数据 s 添加到集合中去
                addData(new Gson().fromJson(s, StatusTimeLineResponse.class));

               /* // ，返回类参数。class 的数据 StatusTimeLineResponse 数据类根据返回的json格式得到
                StatusTimeLineResponse timeLineResponse = new Gson().fromJson(s, StatusTimeLineResponse.class);
                //把statuses 数组传入
                lv_home.setAdapter(new StatusAdapter(activity, timeLineResponse.getStatuses()));*/

                plv_home.onRefreshComplete();//下拉刷新已完成
            }

            @Override
            public void onWeiboException(WeiboException e) {
                Logger.show("REQUEST onIOException", e.toString());
            }

        });
    }


    //resBean.getStatuses() 获取类中集合
    private void addData(StatusTimeLineResponse resBean) {

        for(Status status : resBean.getStatuses()) {
            if(!statuses.contains(status)) {  //当前集合中没有包含时
                statuses.add(status);       //将新数据添加都原有集合中
            }
        }

        adapter.notifyDataSetChanged();  //刷新适配器

        if(curPage < resBean.getTotal_number()) {  //当前页小于总页数。说明后面还有页需要我们添加
            addFootView(plv_home, footView);   //加载的文字和进度条
        } else {
            removeFootView(plv_home, footView);  //已加载到最后一页，隐藏
        }
    }

    private void addFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if(lv.getFooterViewsCount() == 1) {
            lv.addFooterView(footView);   //设置底部加载的布局
        }
    }

    private void removeFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if(lv.getFooterViewsCount() > 1) {
            lv.removeFooterView(footView);  //移除底部加载的布局
        }
    }


}
