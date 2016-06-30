package com.dc.wibome.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 自定义的Pull2RefreshListView
 */
public class Pull2RefreshListView extends PullToRefreshListView {


    public Pull2RefreshListView(Context context) {
        super(context);
    }

    public Pull2RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Pull2RefreshListView(Context context, Mode mode) {
        super(context, mode);
    }

    public Pull2RefreshListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }


    /*自带的滚动监听，只是针对列表部分
     * 希望包含下拉刷新部分进行监听，对整个控件进行监听*/
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if(onPlvScrollListener != null) {//非空时就将数据暴露出去
            onPlvScrollListener.onScrollChanged(l, t, oldl, oldt);
        }
    }


    //创建一个局部变量
    private OnPlvScrollListener onPlvScrollListener;

    public void setOnPlvScrollListener(OnPlvScrollListener onPlvScrollListener) {
        this.onPlvScrollListener = onPlvScrollListener;
    }

    //自定义一个接口把onScrollChanged 暴露出去
    public static interface OnPlvScrollListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }


}
