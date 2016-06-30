package com.dc.wibome.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

/**
 * Created by dc on 2016/4/11.
 */
public class Pull2RefreshScrollView extends PullToRefreshScrollView {
    public Pull2RefreshScrollView(Context context) {
        super(context);
    }

    public Pull2RefreshScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Pull2RefreshScrollView(Context context, Mode mode) {
        super(context, mode);
    }

    public Pull2RefreshScrollView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
    }


    private void init(Context context) {
//		lv = new InnerListView(context);
//		int height = DisplayUtils.getScreenHeightPixels((Activity)context)
//				- DisplayUtils.dp2px(context, 56 + 48 + 48);
//		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, height);
//		lv.setLayoutParams(params);
//
//		FrameLayout refreshContainer = (FrameLayout) getChildAt(1);
//		ScrollView refreshView = (ScrollView) refreshContainer.getChildAt(0);
//		LinearLayout llScrollContent = (LinearLayout) refreshView.getChildAt(0);
//		llScrollContent.addView(lv);

    }

    private OnScrollChangeListener onScrollChangeListener;

    public void setOnScrollChangeListener(OnScrollChangeListener onScrollChangeListener) {
        this.onScrollChangeListener = onScrollChangeListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if(onScrollChangeListener != null) {
            onScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public static interface OnScrollChangeListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }
}
