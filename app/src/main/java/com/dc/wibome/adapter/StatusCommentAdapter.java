package com.dc.wibome.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dc.wibome.R;
import com.dc.wibome.activity.UserInfoActivity;
import com.dc.wibome.entity.response.Comment;
import com.dc.wibome.entity.response.User;
import com.dc.wibome.utils.DateUtils;
import com.dc.wibome.utils.StringUtils;
import com.dc.wibome.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * 评论列表的适配器
 */
public class StatusCommentAdapter  extends BaseAdapter {

    private Context context;
    private List<Comment> comments;
    private ImageLoader imageLoader;

    public StatusCommentAdapter(Context context, List<Comment> comments) {
        this.context = context;
        this.comments = comments;
        this.imageLoader = ImageLoader.getInstance();
    }


    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Comment getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolderList holder; //优化机制

        if (convertView == null) {
            holder = new ViewHolderList();
            convertView = View.inflate(context, R.layout.item_comment, null);

            holder.ll_comments = (LinearLayout) convertView
                    .findViewById(R.id.ll_comments);   //评论列表总布局id
            holder.iv_avatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);   //微博头像

            holder.tv_subhead = (TextView) convertView
                    .findViewById(R.id.tv_subhead);  //对应的微博昵称  ，时间
            holder.tv_caption = (TextView) convertView
                    .findViewById(R.id.tv_caption);

            holder.tv_comment = (TextView) convertView
                    .findViewById(R.id.tv_comment);  //评论内容

            convertView.setTag(holder);//设置标签
        }else {
            holder = (ViewHolderList) convertView.getTag();
        }


        Comment comment = getItem(position);
        final User user = comment.getUser();    //获取评论列表的用户信息
        //加载图片
        imageLoader.displayImage(user.getProfile_image_url(), holder.iv_avatar);

        holder.tv_subhead.setText(user.getName());  //用户昵称
        //时间处理
        holder.tv_caption.setText(DateUtils.getShortTime(comment.getCreated_at()));
         //处理可显示图片，点击文字//呈现微博正文
        SpannableString weiboContent = StringUtils.getWeiboContent(
                context, holder.tv_comment, comment.getText());
        holder.tv_comment.setText(weiboContent);

        //点击头像跳转 个人详情界面
        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("userName", user.getName());
                context.startActivity(intent);
            }
        });

        holder.tv_subhead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("userName", user.getName());
                context.startActivity(intent);
            }
        });


        //点击textView的事件
        holder.ll_comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(context, "回复评论", Toast.LENGTH_SHORT);
            }
        });


        return convertView;
    }


    public static class ViewHolderList {
        public LinearLayout ll_comments;
        public ImageView iv_avatar;
        public TextView tv_subhead;
        public TextView tv_caption;
        public TextView tv_comment;
    }
}
