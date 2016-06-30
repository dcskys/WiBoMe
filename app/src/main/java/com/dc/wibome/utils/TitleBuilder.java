package com.dc.wibome.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dc.wibome.R;
import com.dc.wibome.activity.UserInfoActivity;

/**
 *
 */
/*
* 标题栏构造器，为了简化标题栏代码
* */
public class TitleBuilder {

    private View viewTitle;
    private TextView tvTitle;
    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvLeft;
    private TextView tvRight;

    //在activity中使用
    public TitleBuilder(Activity context) {
        viewTitle = context.findViewById(R.id.rl_titlebar);
        tvTitle = (TextView) viewTitle.findViewById(R.id.titlebar_tv);
        ivLeft = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_left);
        ivRight = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_right);
        tvLeft = (TextView) viewTitle.findViewById(R.id.titlebar_tv_left);
        tvRight = (TextView) viewTitle.findViewById(R.id.titlebar_tv_right);
    }

    //在fragment中使用
    public TitleBuilder(View context) {
        viewTitle = context.findViewById(R.id.rl_titlebar);
        tvTitle = (TextView) viewTitle.findViewById(R.id.titlebar_tv);
        ivLeft = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_left);
        ivRight = (ImageView) viewTitle.findViewById(R.id.titlebar_iv_right);
        tvLeft = (TextView) viewTitle.findViewById(R.id.titlebar_tv_left);
        tvRight = (TextView) viewTitle.findViewById(R.id.titlebar_tv_right);
    }

    // 设置标题栏主布局的颜色

    public TitleBuilder setTitleBgRes(int resid) {
        viewTitle.setBackgroundResource(resid);  //设置view背景
        return this;  //返回本类对象的好处，可以 。方法。方法 这样使用
    }

    public TitleBuilder setTitleText(String text) {
        tvTitle.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        tvTitle.setText(text);
        return this;
    }

    // left

    public TitleBuilder setLeftImage(int resId) {
        ivLeft.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        ivLeft.setImageResource(resId);
        return this;
    }

    public TitleBuilder setLeftText(String text) {
        tvLeft.setVisibility(TextUtils.isEmpty(text) ? View.GONE : View.VISIBLE);
        tvLeft.setText(text);
        return this;
    }

    public TitleBuilder setLeftOnClickListener(View.OnClickListener listener) {
        if (ivLeft.getVisibility() == View.VISIBLE) {
            ivLeft.setOnClickListener(listener);
        } else if (tvLeft.getVisibility() == View.VISIBLE) {
            tvLeft.setOnClickListener(listener);
        }
        return this;
    }

    // right

    public TitleBuilder setRightImage(int resId) {
        ivRight.setVisibility(resId > 0 ? View.VISIBLE : View.GONE);
        ivRight.setImageResource(resId);
        return this;
    }

    public TitleBuilder setRightText(String text) {
        //判断字符串是否为空
        tvRight.setVisibility(TextUtils.isEmpty(text) ? View.GONE
                : View.VISIBLE);
        tvRight.setText(text);
        return this;
    }

    public TitleBuilder setRightOnClickListener(View.OnClickListener listener) {
        if (ivRight.getVisibility() == View.VISIBLE) {
            ivRight.setOnClickListener(listener);
        } else if (tvRight.getVisibility() == View.VISIBLE) {
            tvRight.setOnClickListener(listener);
        }
        return this;
    }
   //可略的方法
    public View build() {
        return viewTitle;
    }


}

