package com.dc.wibome.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;

import com.dc.wibome.R;

/**
 * 自定义的下划线指示器控件，带动画
 */
public class UnderlineIndicatorView extends LinearLayout {

    private int mCurrentPosition;

    public UnderlineIndicatorView(Context context) {
        this(context, null);
    }

    public UnderlineIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(HORIZONTAL);

        int count = 4;
        for (int i = 0; i < count; i++) {
            View view = new View(context);  //平均分配了4个控件
            LayoutParams params = new LayoutParams(0, LayoutParams.MATCH_PARENT);
            params.weight = 1;
            view.setLayoutParams(params);
            view.setBackgroundResource(R.color.transparent); //背景色为透明
            addView(view);
        }
    }
    //无动画效果的设置，用于隐藏控件的同步问题
    public void setCurrentItemWithoutAnim(int position) {

        final View oldChlid = getChildAt(mCurrentPosition);
        final View newChild = getChildAt(position);

        oldChlid.setBackgroundResource(R.color.transparent);
        newChild.setBackgroundResource(R.color.orange);

        mCurrentPosition = position;
        invalidate();
    }

    //选中状态
    public void setCurrentItem(int position) {
        //获取当前位置的视图
        final View oldChlid = getChildAt(mCurrentPosition);
        final View newChild = getChildAt(position);//目标位置视图


        //代码创建位移动画，x轴移动 ，移动自身的大小，移动几个位置
        TranslateAnimation translateAnimation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, position - mCurrentPosition,
                Animation.RELATIVE_TO_SELF, 0,
                Animation.RELATIVE_TO_SELF, 0);
        translateAnimation.setDuration(200);

        //动画事件的监听

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }
            //动画结束时
            @Override
            public void onAnimationEnd(Animation animation) {
                //补间动画并不会改变控件位置，只是一种效果
                //改变视图颜色
                oldChlid.setBackgroundResource(R.color.transparent);
                newChild.setBackgroundResource(R.color.orange);
            }
        });

        oldChlid.setAnimation(translateAnimation); //执行动画

        mCurrentPosition = position;  //更新位置
        invalidate(); //进行页面更新
    }

}
