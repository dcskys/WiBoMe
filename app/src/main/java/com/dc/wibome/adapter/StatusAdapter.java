package com.dc.wibome.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dc.wibome.R;
import com.dc.wibome.activity.ImageBrowserActivity;
import com.dc.wibome.activity.StatusDetailActivity;
import com.dc.wibome.activity.UserInfoActivity;
import com.dc.wibome.activity.WriteCommentActivity;
import com.dc.wibome.activity.WriteStatusActivity;
import com.dc.wibome.entity.response.PicUrls;
import com.dc.wibome.entity.response.Status;
import com.dc.wibome.entity.response.User;
import com.dc.wibome.utils.DateUtils;
import com.dc.wibome.utils.StringUtils;
import com.dc.wibome.utils.ToastUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页列表呈现的适配器
 */
public class StatusAdapter  extends BaseAdapter {

    private Context context;
    private List<Status> datas;
    private ImageLoader imageLoader;

    public StatusAdapter(Context context, List<Status> datas) {
        this.context = context;
        this.datas = datas;
        imageLoader = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Status getItem(int i) {
        return datas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView( final int position, View convertView, ViewGroup viewGroup) {
        final ViewHolder holder; //优化机制

        if (convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_status, null);//绑定布局

            holder.ll_card_content = (LinearLayout) convertView
                    .findViewById(R.id.ll_card_content); //每一个列表项的总布局

            holder.iv_avatar = (ImageView) convertView
                    .findViewById(R.id.iv_avatar);  //发表微博人布局中头像id
            holder.rl_content = (RelativeLayout) convertView
                    .findViewById(R.id.rl_content); //发表微博的人布局textView总布局
            holder.tv_subhead = (TextView) convertView
                    .findViewById(R.id.tv_subhead);
            holder.tv_caption = (TextView) convertView
                    .findViewById(R.id.tv_caption);

            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);  //微博正文

            /*呈现图片的布局*/
            holder.include_status_image = (FrameLayout) convertView
                    .findViewById(R.id.include_status_image);
            holder.gv_images = (GridView) holder.include_status_image
                    .findViewById(R.id.gv_images);   //多图
            holder.iv_image = (ImageView) holder.include_status_image
                    .findViewById(R.id.iv_image);  //单图


            //<!--引用微博的布局-->
            holder.include_retweeted_status = (LinearLayout) convertView
                    .findViewById(R.id.include_retweeted_status);  //总布局

            holder.tv_retweeted_content = (TextView) holder.include_retweeted_status
                    .findViewById(R.id.tv_retweeted_content);    //引用总布局中的textView
            holder.include_retweeted_status_image = (FrameLayout) holder.include_retweeted_status
                    .findViewById(R.id.include_status_image);  //引用总布局中的图片布局，
            holder.gv_retweeted_images = (GridView) holder.include_retweeted_status_image
                    .findViewById(R.id.gv_images);      ////引用总布局中的图片布局中的多图，和上面进行区分
            holder.iv_retweeted_image = (ImageView) holder.include_retweeted_status_image
                    .findViewById(R.id.iv_image);

             // <!--底部操作栏的布局，点赞，评论等-->
            holder.ll_share_bottom = (LinearLayout) convertView
                    .findViewById(R.id.ll_share_bottom);    //转发总布局
            holder.iv_share_bottom = (ImageView) convertView
                    .findViewById(R.id.iv_share_bottom);     //转发总布局中图片
            holder.tv_share_bottom = (TextView) convertView
                    .findViewById(R.id.tv_share_bottom);  //转发总布局中文字“转发”

            holder.ll_comment_bottom = (LinearLayout) convertView
                    .findViewById(R.id.ll_comment_bottom);    //评论总布局
            holder.iv_comment_bottom = (ImageView) convertView
                    .findViewById(R.id.iv_comment_bottom);   //评论总布局中图片
            holder.tv_comment_bottom = (TextView) convertView
                    .findViewById(R.id.tv_comment_bottom);  //评论总布局中文字“评论”

            holder.ll_like_bottom = (LinearLayout) convertView
                    .findViewById(R.id.ll_like_bottom);     //点赞总布局
            holder.iv_like_bottom = (ImageView) convertView
                    .findViewById(R.id.iv_like_bottom);     //点赞总布局中图片
            holder.tv_like_bottom = (TextView) convertView
                    .findViewById(R.id.tv_like_bottom);    //点赞总布局中文字“点赞”

            convertView.setTag(holder);  //设置标签
        }else {  //布局已存在时，直接获取标签就可以了，不用重复findViewById
            holder = (ViewHolder) convertView.getTag();
        }


        // bind data  绑定列表资源
        final Status status =  getItem(position); //获取列表中某一项status类

        final User user = status.getUser();      //某一项status类的用户
        //获取json中用户头像的url地址，使用imageLoader第三方图片加载库，加载到ImageView,图片缓存已在BaseApplication设置了
        imageLoader.displayImage(user.getProfile_image_url(), holder.iv_avatar);

        holder.tv_subhead.setText(user.getName());  //显示名称

       /* holder.tv_caption.setText(status.getCreated_at()
                + " 来自 " + status.getSource());   //创建时间和微博来源*/

         //转化日期格式，使用工具类，Html.fromHtml系统提供的方法，直接转化
        holder.tv_caption.setText(DateUtils.getShortTime(status.getCreated_at())
                + " 来自 " + Html.fromHtml(status.getSource()));

        /*holder.tv_content.setText(status.getText()); 没有处理可显示图片，点击文字//呈现微博正文*/
        holder.tv_content.setText(StringUtils.getWeiboContent(
                context, holder.tv_content, status.getText()));  //直接呈现 spannableString类型

          //点击头像 跳转到详情界面
        holder.iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserInfoActivity.class);
                intent.putExtra("userName", user.getName()); //根据用户昵称查询，id不全面
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




        //单图或多图的设置封装到方法中，后面引用微博也会用到
        setImages(status, holder.include_status_image, holder.gv_images, holder.iv_image);

        //引用微博部分
        final  Status retweeted_status = status.getRetweeted_status();
        if(retweeted_status != null) {

            User retUser = retweeted_status.getUser();
            holder.include_retweeted_status.setVisibility(View.VISIBLE);//引用微博总布局呈现

          /*  holder.tv_retweeted_content.setText("@" + retUser.getName() + ":"
                    + retweeted_status.getText());     //引用微博的正文
*/
            String retweetedContent = "@" + retUser.getName() + ":"
                    + retweeted_status.getText();
              //引用微博部分同样也要进行 设置可点击文字，图片
            holder.tv_retweeted_content.setText(StringUtils.getWeiboContent(
                    context, holder.tv_retweeted_content, retweetedContent));

            //引用微博图片布局总布局，图片布局的多图，图片布局的单图
            setImages(retweeted_status,
                    holder.include_retweeted_status_image,
                    holder.gv_retweeted_images, holder.iv_retweeted_image);
        } else {  //引用微博为空，设置布局不显示
            holder.include_retweeted_status.setVisibility(View.GONE);
        }
           // <!--底部操作栏的布局，转发点赞，评论等 的数据呈现-->
        //使用3元运算符，当转发点赞，评论为0时，呈现"转发"，否则直接呈现数字
        holder.tv_share_bottom.setText(status.getReposts_count() == 0 ?
                "转发" : status.getReposts_count() + "");

        holder.tv_comment_bottom.setText(status.getComments_count() == 0 ?
                "评论" : status.getComments_count() + "");

        holder.tv_like_bottom.setText(status.getAttitudes_count() == 0 ?
                "赞" : status.getAttitudes_count() + "");


        //点击内容跳转到微博详情页
        holder.ll_card_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StatusDetailActivity.class);
                intent.putExtra("status", status);
                context.startActivity(intent);
            }
        });

        // //点击引用微博跳转到微博详情页
        holder.include_retweeted_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, StatusDetailActivity.class);
                intent.putExtra("status", retweeted_status);
                context.startActivity(intent);
            }
        });

        //转发按钮点击
        //holder.cb_like_bottom.setChecked(status.isLiked());
        holder.ll_share_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  ToastUtils.showToast(context, "转个发~", Toast.LENGTH_SHORT);
                Intent intent = new Intent(context, WriteStatusActivity.class);
                intent.putExtra("status", status);
                context.startActivity(intent);
            }
        });


        holder.ll_comment_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(status.getComments_count() > 0) { //评论大于0时，跳转到详情页面
                    Intent intent = new Intent(context, StatusDetailActivity.class);
                    intent.putExtra("status", status); //传递一个类需要实现序列化接口
                    intent.putExtra("scroll2Comment", true);
                    context.startActivity(intent);
                } else {  //=0时，跳转到写评论页面
                    Intent intent = new Intent(context, WriteCommentActivity.class);
                    intent.putExtra("status", status);
                    context.startActivity(intent);
                }
                ToastUtils.showToast(context, "评个论~", Toast.LENGTH_SHORT);
            }
        });

        holder.ll_like_bottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showToast(context, "点个赞~", Toast.LENGTH_SHORT);
            }
        });

        return convertView;
    }




     /*判断呈现单图还是多图*/
    private void setImages(final Status status, FrameLayout imgContainer, GridView gv_images, ImageView iv_image) {
        //获取多图集合
        ArrayList<PicUrls> pic_urls = status.getPic_urls();
        //单图缩略图
        String thumbnail_pic = status.getThumbnail_pic();

        if(pic_urls != null && pic_urls.size() > 1) {  //多图
            imgContainer.setVisibility(View.VISIBLE);  //显示图片的布局
            gv_images.setVisibility(View.VISIBLE);   //多图显示
            iv_image.setVisibility(View.GONE);    //单图隐藏
               //把图片集合传入进去，
            StatusGridImgsAdapter gvAdapter = new StatusGridImgsAdapter(context, pic_urls);
            gv_images.setAdapter(gvAdapter);  //GridView 呈现这个集合
             //多图浏览，
            gv_images.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(context, ImageBrowserActivity.class);
                    intent.putExtra("status", status);
                    intent.putExtra("position", position); //传入当前位置
                    context.startActivity(intent);
                }
            });


        } else if(thumbnail_pic != null) {  //单图
            imgContainer.setVisibility(View.VISIBLE);//显示图片的布局
            gv_images.setVisibility(View.GONE);//多图隐藏
            iv_image.setVisibility(View.VISIBLE); //单图显示

            imageLoader.displayImage(thumbnail_pic, iv_image);  //呈现图片

            //单图点击浏览
            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageBrowserActivity.class);
                    intent.putExtra("status", status);
                    context.startActivity(intent);
                }
            });


        } else {
            imgContainer.setVisibility(View.GONE);  //图片不存在时，隐藏图片布局
        }

    }



    public static class ViewHolder {
        public LinearLayout ll_card_content;
        public ImageView iv_avatar;
        public RelativeLayout rl_content;
        public TextView tv_subhead;
        public TextView tv_caption;

        public TextView tv_content;
        public FrameLayout include_status_image;
        public GridView gv_images;
        public ImageView iv_image;

        public LinearLayout include_retweeted_status;
        public TextView tv_retweeted_content;
        public FrameLayout include_retweeted_status_image;
        public GridView gv_retweeted_images;
        public ImageView iv_retweeted_image;

        public LinearLayout ll_share_bottom;
        public ImageView iv_share_bottom;
        public TextView tv_share_bottom;
        public LinearLayout ll_comment_bottom;
        public ImageView iv_comment_bottom;
        public TextView tv_comment_bottom;
        public LinearLayout ll_like_bottom;
        public ImageView iv_like_bottom;
        public TextView tv_like_bottom;
        public CheckBox cb_like_bottom;
    }
}
