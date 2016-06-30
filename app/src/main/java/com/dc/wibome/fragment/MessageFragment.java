package com.dc.wibome.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dc.wibome.BaseFragment;
import com.dc.wibome.R;
import com.dc.wibome.adapter.UserItemAdapter;
import com.dc.wibome.entity.response.UserItem;
import com.dc.wibome.utils.TitleBuilder;
import com.dc.wibome.utils.ToastUtils;
import com.dc.wibome.widget.WrapHeightListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息页面
 */
public class MessageFragment extends BaseFragment {

    private View view;
    private WrapHeightListView lv_user_items;
    private List<UserItem> userItems;   //列表数据

    private UserItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = View.inflate(activity, R.layout.frag_message, null);



        initView();

        setItem(); //添加列表数据


        return view;
    }



    private void initView() {
        new TitleBuilder(view)
                .setTitleText("消息")
                .setRightImage(R.mipmap.message_chat)
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

    }


    private void setItem() {

        userItems.add(new UserItem(false, R.mipmap.message_weibo, "@我的", ""));
        userItems.add(new UserItem(false, R.mipmap.message_comment, "评论", ""));
        userItems.add(new UserItem(false, R.mipmap.message_like, "赞", ""));

    }


}
