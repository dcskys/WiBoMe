package com.dc.wibome.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dc.wibome.BaseFragment;
import com.dc.wibome.R;
import com.dc.wibome.adapter.UserItemAdapter;
import com.dc.wibome.entity.response.UserItem;
import com.dc.wibome.utils.TitleBuilder;
import com.dc.wibome.utils.ToastUtils;
import com.dc.wibome.widget.WrapHeightListView;
import com.plter.lib.ab.AndroidBanner;

import java.util.ArrayList;
import java.util.List;

/**
 * 发现页面
 */
public class SearchFragment extends BaseFragment {

    private View view;
    private AndroidBanner ab;

    private WrapHeightListView lv_user_items;
    private List<UserItem> userItems;   //列表数据
    private UserItemAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_search, null);

        initView();

        setItem(); //添加列表数据
        return view;
    }



    private void initView() {
        new TitleBuilder(view)
                .setTitleText("发现")
                .setRightImage(R.mipmap.find_talk)
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ToastUtils.showToast(activity, "暂未实现", Toast.LENGTH_SHORT);
                    }
                });


        // 设置栏列表，呈现列表数据
        lv_user_items = (WrapHeightListView) view.findViewById(R.id.lv_user_items);
        userItems = new ArrayList<UserItem>();
        adapter = new UserItemAdapter(activity, userItems);
        lv_user_items.setAdapter(adapter);

         //图片轮播
        ab = (AndroidBanner) view.findViewById(R.id.ab);
         //getActivity().getSupportFragmentManager(),
        ab.setAdapter(new FragmentPagerAdapter(activity.getSupportFragmentManager()) {

            private final List<Integer> imgIds = new ArrayList<Integer>() {
                {
                    add(R.mipmap.bar4);
                    add(R.mipmap.bar5);
                    add(R.mipmap.bar3);
                    add(R.mipmap.bar2);

                }
            };

            @Override
            public Fragment getItem(final int position) {
                return new Fragment() {
                    @Nullable
                    @Override
                    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                        ImageView iv = new ImageView(getContext());
                        iv.setScaleType(ImageView.ScaleType. FIT_XY);//图片塞满整个View。
                       /* LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        iv.setLayoutParams(params);*/
                        iv.setImageResource(imgIds.get(position));
                        return iv;
                    }
                };
            }

            @Override
            public int getCount() {

                return imgIds.size();
            }

        });

    }


    private void setItem() {

        userItems.add(new UserItem(false, R.mipmap.message_weibo, "微博热门", "全站最热微博尽搜罗"));
        userItems.add(new UserItem(false, R.mipmap.find_search, "找人", ""));
        userItems.add(new UserItem(true, R.mipmap.find_title, "微博头条", "随时随地一起看新闻"));
        userItems.add(new UserItem(false, R.mipmap.find_game, "玩游戏", "微博上最火的游戏"));
        userItems.add(new UserItem(false, R.mipmap.find_self, "周边", "发现最值得去的地儿"));
        userItems.add(new UserItem(true, R.mipmap.find_camear, "随手拍", "发照片视屏赢奖金！"));
        userItems.add(new UserItem(false, R.mipmap.find_movie, "电影", ""));
        userItems.add(new UserItem(false, R.mipmap.find_shop, "红人淘", ""));
        userItems.add(new UserItem(false, R.mipmap.find_more, "更多频道", ""));
        adapter.notifyDataSetChanged();


    }

}
