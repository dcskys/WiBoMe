package com.dc.wibome.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 自定义的高度自适应的GridView，用来呈现九宫图片
 */
public class WrapHeightGridView extends GridView {

    public WrapHeightGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public WrapHeightGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WrapHeightGridView(Context context) {
        super(context);
    }

    /*widthMeasureSpec宽度，
    heightMeasureSpec高度   计算期望值，32位数复合型 包含期望值和期望类型
    */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

       /*期望类型包含3种
        MeasureSpec.AT_MOST  能多大就多大，不要超过这个数值
        MeasureSpec.EXACTLY 明确期望有一个具体的大小
        MeasureSpec.UNSPECIFIED  不做任何限制
        */
        int heightSpec;

        if (getLayoutParams().height == LayoutParams.WRAP_CONTENT) {

            //高度自适应，能多大就多大，前面有限制数值，复合型32位，第30位才表示具体宽高，所有右移2位
            heightSpec = MeasureSpec.makeMeasureSpec(
                    Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }else {
            heightSpec = heightMeasureSpec;
        }

        super.onMeasure(widthMeasureSpec, heightSpec);
    }
}

