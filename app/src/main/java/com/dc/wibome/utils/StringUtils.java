package com.dc.wibome.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dc.wibome.R;
import com.dc.wibome.activity.UserInfoActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 处理字符串的工具类，SpannableString，
 * 这里在StatusAdapter中，获取正文数据时使用
 */
public class StringUtils {

    public static SpannableString getWeiboContent(final Context context, final TextView tv, String source) {
        String regexAt = "@[\u4e00-\u9fa5\\w]+";   //@用户部分， 前面表示中文 ，\w包含下划线的字符，0-9，a-z，+匹配一个或多个
        String regexTopic = "#[\u4e00-\u9fa5\\w]+#"; //引用部分
        String regexEmoji = "\\[[\u4e00-\u9fa5\\w]+\\]";//表情部分 ，[] ，\\]表示括号

        // |或符号满足任意一个都可以   ，用小括号（） 就可以group（1）确认匹配的是 哪一部分的内容
        String regex = "(" + regexAt + ")|(" + regexTopic + ")|(" + regexEmoji + ")";

        SpannableString spannableString = new SpannableString(source); //把字符串包装成spannableString

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(spannableString); //查找匹配

        if(matcher.find()) {
            tv.setMovementMethod(LinkMovementMethod.getInstance()); //设置文字是可点击的，
            matcher.reset();  //重置下，重新匹配
        }

        while(matcher.find()) {  //不断循环整个字符串
            final String atStr = matcher.group(1); //group表示查找到的具体字符，匹配到第一个字符串
            final String topicStr = matcher.group(2);
            String emojiStr = matcher.group(3);

            if(atStr != null) {  //匹配的@用户部分
                int start = matcher.start(1); //查到的字符串开始位置
                //设置可点击的文字，BoreClickableSpan自定义，没有下划线
                BoreClickableSpan clickableSpan = new BoreClickableSpan(context) {

                    @Override
                    public void onClick(View widget) {
                           //@用户跳转到详情界面
                        Intent intent = new Intent(context, UserInfoActivity.class);
                        intent.putExtra("userName", atStr.substring(1)); //@用户昵称 从第2个开始去掉@
                        context.startActivity(intent);

                    }
                };
                spannableString.setSpan(clickableSpan, start, start + atStr.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

              //匹配到话题部分
            if(topicStr != null) {
                int start = matcher.start(2);

                BoreClickableSpan clickableSpan = new BoreClickableSpan(context) {

                    @Override
                    public void onClick(View widget) {
                        ToastUtils.showToast(context, "话题: " + topicStr, Toast.LENGTH_SHORT);
                    }
                };
                spannableString.setSpan(clickableSpan, start, start + topicStr.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }

            //表情处理
            if(emojiStr != null) {

                int start = matcher.start(3);   //查到的字符串开始位置

                int imgRes = EmotionUtils.getImgByName(emojiStr);   //查找到的具体表情部分，根据字符获取本地表情资源R.mipmap
                //查找到的表情资源解析成bitmap
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), imgRes);

                if(bitmap != null) {  //当本地没有匹配的图片资源，就不转化
                    int size = (int) tv.getTextSize();   //获取控件文字大小
                    //根据文字大小进行压缩图片 ，原图 ，目标宽高，true表示压缩的高质量
                    bitmap = Bitmap.createScaledBitmap(bitmap, size, size, true);

                    ImageSpan imageSpan = new ImageSpan(context, bitmap);
                    //把查找到的字符串替换成bitmap呈现，开始和结束位置
                    spannableString.setSpan(imageSpan, start, start + emojiStr.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }



        }


        return spannableString; //最后返回这个类型
    }

     //因为原始的ClickableSpan，有下划线，这里重载 ，不需要下划线
    static class BoreClickableSpan extends ClickableSpan {

        private Context context;

        public BoreClickableSpan(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View widget) {
            // TODO Auto-generated method stub

        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(context.getResources().getColor(R.color.txt_at_blue)); //设置文字颜色蓝色
            ds.setUnderlineText(false);  //没有下划线
        }


    }
}
