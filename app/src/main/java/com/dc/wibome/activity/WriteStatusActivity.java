package com.dc.wibome.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dc.wibome.BaseActivity;
import com.dc.wibome.R;
import com.dc.wibome.adapter.EmotionGvAdapter;
import com.dc.wibome.adapter.EmotionPagerAdapter;
import com.dc.wibome.adapter.WriteStatusGridImgsAdapter;
import com.dc.wibome.api.BoreWeiboApi;
import com.dc.wibome.constants.AccessTokenKeeper;
import com.dc.wibome.constants.WeiboConstants;
import com.dc.wibome.entity.response.Emotion;
import com.dc.wibome.entity.response.Status;
import com.dc.wibome.utils.DialogUtils;
import com.dc.wibome.utils.DisplayUtils;
import com.dc.wibome.utils.ImageUtils;
import com.dc.wibome.utils.StringUtils;
import com.dc.wibome.utils.TitleBuilder;
import com.dc.wibome.widget.WrapHeightGridView;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

import java.util.ArrayList;
import java.util.List;

/*
* 发微博界面*/
public class WriteStatusActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    // 输入框
    private EditText et_write_status;
    // 添加的九宫格图片
    private WrapHeightGridView gv_write_status;
    // 转发微博内容
    private View include_retweeted_status_card;
    private ImageView iv_rstatus_img;;
    private TextView tv_rstatus_username;;
    private TextView tv_rstatus_content;;
    // 底部添加栏
    private ImageView iv_image;
    private ImageView iv_at;
    private ImageView iv_topic;
    private ImageView iv_emoji;
    private ImageView iv_add;
    // 表情选择面板
    private LinearLayout ll_emotion_dashboard;
    private ViewPager vp_emotion_dashboard;
    // 进度对话框
    private ProgressDialog progressDialog;

    private WriteStatusGridImgsAdapter statusImgsAdapter;
    private ArrayList<Uri> imgUris = new ArrayList<Uri>();  //用来存放选取图片的uri集合

    private EmotionPagerAdapter emotionPagerGvAdapter;

    // 引用的微博
    private Status retweeted_status;
    // 显示在页面中,实际需要转发内容的微博
    private Status cardStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_status);
        //获取转发按钮 传递的类
        retweeted_status = (Status) getIntent().getSerializableExtra("status");

        initView();
    }

    private void initView() {

        // 标题栏
        new TitleBuilder(this)
                .setTitleText("发微博")
                .setLeftText("取消")
                .setLeftOnClickListener(this)
                .setRightText("发送")
                .setRightOnClickListener(this)
                .build();

        // 输入框
        et_write_status = (EditText) findViewById(R.id.et_write_status);
        // 添加的九宫格图片GridView
        gv_write_status = (WrapHeightGridView) findViewById(R.id.gv_write_status);

        // 转发微博内容
        include_retweeted_status_card = findViewById(R.id.include_retweeted_status_card);
        iv_rstatus_img = (ImageView) findViewById(R.id.iv_rstatus_img);
        tv_rstatus_username = (TextView) findViewById(R.id.tv_rstatus_username);
        tv_rstatus_content = (TextView) findViewById(R.id.tv_rstatus_content);

        // 底部添加栏
        iv_image = (ImageView) findViewById(R.id.iv_image);
        iv_at = (ImageView) findViewById(R.id.iv_at);
        iv_topic = (ImageView) findViewById(R.id.iv_topic);
        iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
        iv_add = (ImageView) findViewById(R.id.iv_add);

        // 表情选择面板总布局
        ll_emotion_dashboard = (LinearLayout) findViewById(R.id.ll_emotion_dashboard);
         //ViewPager控件
        vp_emotion_dashboard = (ViewPager) findViewById(R.id.vp_emotion_dashboard);

        // 进度框
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("微博发布中...");

          //图片集合和gridView
        statusImgsAdapter = new WriteStatusGridImgsAdapter(this, imgUris, gv_write_status);
        gv_write_status.setAdapter(statusImgsAdapter);
        gv_write_status.setOnItemClickListener(this);



        iv_image.setOnClickListener(this);
        iv_at.setOnClickListener(this);
        iv_topic.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
        iv_add.setOnClickListener(this);

        initRetweetedStatus(); //初始化引用微博内容

        initEmotion(); //初始化表情面板内容
    }


    /**
     * 初始化引用微博内容
     */
    private void initRetweetedStatus() {

        // 转发微博特殊处理，接收到传递过来的参数
        if(retweeted_status != null) {
            // 转发的微博是否包含转发内容
            Status rrStatus = retweeted_status.getRetweeted_status();
            if(rrStatus != null) {
                String content = "//@" + retweeted_status.getUser().getName()
                        + ":" + retweeted_status.getText();
                //editText 把转发内容引入
                et_write_status.setText(StringUtils.getWeiboContent(this, et_write_status, content));
                // 如果引用的为转发微博,则使用它转发的内容
                cardStatus = rrStatus;
            } else {
                et_write_status.setText("转发微博");
                // 如果引用的为原创微博,则使用它自己的内容
                cardStatus = retweeted_status;
            }


            // 设置转发图片内容
            String imgUrl = cardStatus.getThumbnail_pic();
            if(TextUtils.isEmpty(imgUrl)) {
                iv_rstatus_img.setVisibility(View.GONE); //没有图片，隐藏头像图片控件
            } else {
                iv_rstatus_img.setVisibility(View.VISIBLE);
                imageLoader.displayImage(cardStatus.getThumbnail_pic(), iv_rstatus_img);
            }

            // 设置转发文字内容
            tv_rstatus_username.setText("@" + cardStatus.getUser().getName());
            tv_rstatus_content.setText(cardStatus.getText());

            // 转发微博时,不能添加图片
            iv_image.setVisibility(View.GONE);
            //转发微博总布局呈现
            include_retweeted_status_card.setVisibility(View.VISIBLE);
        }

    }


    /**
     *  初始化表情面板内容
     *
     *  希望实现一个3 *21的表情图标，最后是一个回退按钮
     *  GridView是7列
     */
    private void initEmotion() {
        // 获取屏幕宽度   ，是为了动态获取宽度
        int gvWidth = DisplayUtils.getScreenWidthPixels(this);

        // 表情边距  8dp 对应像素值，考虑屏幕适配问题
        int spacing = DisplayUtils.dp2px(this, 8);

        // GridView中item的宽度
        int itemWidth = (gvWidth - spacing * 8) / 7; //每一个表情的宽度
        int gvHeight = itemWidth * 3 + spacing * 4;  //3行+4个边距大小

        List<GridView> gvs = new ArrayList<GridView>();

        List<String> emotionNames = new ArrayList<String>();

        // 遍历所有的表情名字 key
        for (String emojiName : Emotion.emojiMap.keySet()) {
            emotionNames.add(emojiName); //key添加到数组
            // 每20个表情作为一组,同时添加到ViewPager对应的view集合中
            if (emotionNames.size() == 20) {
                GridView gv = createEmotionGridView(emotionNames, gvWidth, spacing, itemWidth, gvHeight);
                gvs.add(gv);  //添加GridView到集合中去
                // 添加完一组表情,重新创建一个表情名字集合
                emotionNames = new ArrayList<String>();
            }
        }

        // 检查最后是否有不足20个表情的剩余情况
        if (emotionNames.size() > 0) {
            GridView gv = createEmotionGridView(emotionNames, gvWidth, spacing, itemWidth, gvHeight);
            gvs.add(gv);
        }

        // 将多个GridView添加显示到ViewPager中
        emotionPagerGvAdapter = new EmotionPagerAdapter(gvs);
        vp_emotion_dashboard.setAdapter(emotionPagerGvAdapter);
         //使用的是父布局LinearLayout
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(gvWidth, gvHeight);
        vp_emotion_dashboard.setLayoutParams(params); //设置ViewPager宽高，动态计算得到
    }

    /**
     * 创建显示表情的GridView
     * emotionNames 表情对应的key集合
     * itemWidth 每个表情的宽度
     * gvHeight的宽度
     */
    private GridView createEmotionGridView(List<String> emotionNames, int gvWidth, int padding, int itemWidth, int gvHeight) {
        // 创建GridView
        GridView gv = new GridView(this);
        gv.setBackgroundResource(R.color.bg_gray);
        gv.setSelector(R.color.transparent);  //点击颜色为透明
        gv.setNumColumns(7);  //设置7列
        gv.setPadding(padding, padding, padding, padding);//内边距
        gv.setHorizontalSpacing(padding);
        gv.setVerticalSpacing(padding);//水平和垂直方向的间距
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(gvWidth, gvHeight);
        gv.setLayoutParams(params);
        // 给GridView设置表情图片
        EmotionGvAdapter adapter = new EmotionGvAdapter(this, emotionNames, itemWidth);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(this);
        return gv;
    }


    /**
     * 更新图片显示
     */
    private void updateImgs() {
        if(imgUris.size() > 0) {
            // 如果有图片则显示GridView,同时更新内容
            gv_write_status.setVisibility(View.VISIBLE);
            statusImgsAdapter.notifyDataSetChanged(); //刷新
        } else {
            // 无图则不显示GridView
            gv_write_status.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.titlebar_tv_left:
                finish();
                break;
            case R.id.titlebar_tv_right:    //发送微博
                sendStatus();
                break;
            case R.id.iv_image:

                DialogUtils.showImagePickDialog(this);   //图片选择时弹出对话框选择图片
                //ImageUtils.showImagePickDialog(this);
                break;
            case R.id.iv_at:
                break;
            case R.id.iv_topic:
                break;
            case R.id.iv_emoji:   //点击表情面板时显示
                if(ll_emotion_dashboard.getVisibility() == View.VISIBLE) {
                    // 显示表情面板时点击,将按钮图片设为笑脸按钮,同时隐藏面板
                    iv_emoji.setImageResource(R.drawable.btn_insert_emotion);
                    ll_emotion_dashboard.setVisibility(View.GONE);
                } else {
                    // 未显示表情面板时点击,将按钮图片设为键盘,同时显示面板
                    iv_emoji.setImageResource(R.drawable.btn_insert_keyboard);
                    ll_emotion_dashboard.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_add:
                break;
        }

    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Object itemAdapter = parent.getAdapter();  //获取当前被点击的item 所在的父布局适配器
        //因为表情面板也会有GrideView点击事件，为了不冲突
        if (itemAdapter instanceof WriteStatusGridImgsAdapter) {
            // 点击的是添加的图片
            if (position == statusImgsAdapter.getCount() - 1) {   //-1是因为创建适配器数量多+1，从1开始计数
                // 如果点击了最后一个加号图标,则显示选择图片对话框
                DialogUtils.showImagePickDialog(this);
            }

        } else if (itemAdapter instanceof EmotionGvAdapter) {
            // 点击的是表情
            EmotionGvAdapter emotionGvAdapter = (EmotionGvAdapter) itemAdapter;

            if (position == emotionGvAdapter.getCount() - 1) {
                // 如果点击了最后一个回退按钮,则调用删除键事件
                et_write_status.dispatchKeyEvent(new KeyEvent(
                        KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL));
            } else {
                // 如果点击了表情,则添加到输入框中
                String emotionName = emotionGvAdapter.getItem(position);

                // 获取当前光标位置,在指定位置上添加表情图片文本
                int curPosition = et_write_status.getSelectionStart();
                StringBuilder sb = new StringBuilder(et_write_status.getText().toString());
                sb.insert(curPosition, emotionName);

                // 特殊文字处理,将表情等转换一下
                et_write_status.setText(StringUtils.getWeiboContent(
                        this, et_write_status, sb.toString()));

                // 将光标设置到新增完表情的右侧
                et_write_status.setSelection(curPosition + emotionName.length());
            }
        }

    }



    /*选取图片的返回,进行处理*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case ImageUtils.GET_IMAGE_BY_CAMERA:  //拍照获取的图片，地址uri，手动指定的

                if(resultCode == RESULT_CANCELED) {
                    // 如果拍照取消,将之前新增的图片地址删除 ，图片的uri已创建
                    ImageUtils.deleteImageUri(this, ImageUtils.imageUriFromCamera);
                } else {
//				// 拍照后将图片添加到页面上
				imgUris.add(ImageUtils.imageUriFromCamera);
				updateImgs();

                    // crop裁剪
                    //ImageUtils.cropImage(this, ImageUtils.imageUriFromCamera);
                }

            case ImageUtils.CROP_IMAGE:
                if(resultCode != RESULT_CANCELED) {
                    imgUris.add(ImageUtils.cropImageUri);
                    updateImgs(); //更新图片显示
                }
                break;

            case ImageUtils.GET_IMAGE_FROM_PHONE:  //// 本地相册选择
                if(resultCode != RESULT_CANCELED) {
                    // 本地相册选择完后将图片添加到页面上
                    imgUris.add(data.getData()); //获取返回的uri地址
                    updateImgs();
                }
                break;
            default:
                break;
        }
    }





    /**
     * 发送微博
     */
    private void sendStatus() {

        String comment = et_write_status.getText().toString();
        if(TextUtils.isEmpty(comment)) {
            showToast("微博内容不能为空");
            return;
        }

        String imgFilePath = null;
        if (imgUris.size() > 0) {
            // 微博API中只支持上传一张图片
            Uri uri = imgUris.get(0);//只上传列表中第一张
            imgFilePath = ImageUtils.getImageAbsolutePath(this, uri);  //把uri转化成图片路径
        }

        // 转发微博的id
        long retweetedStatsId = cardStatus == null ? -1 : cardStatus.getId();
        // 上传微博api接口
        progressDialog.show(); //呈现进度



        //实例化微博api ,可以改进到BaseActivity
        token  = AccessTokenKeeper.readAccessToken(this);
        BoreWeiboApi api = new BoreWeiboApi(this, WeiboConstants.APP_KEY,token);


        api.statusesSend(et_write_status.getText().toString(), imgFilePath, retweetedStatsId, new RequestListener() {
            @Override
            public void onComplete(String s) {
                //setResult(RESULT_OK);
                progressDialog.dismiss();
                showToast("微博发送成功");
                WriteStatusActivity.this.finish();

            }

            @Override
            public void onWeiboException(WeiboException e) {
                progressDialog.dismiss();
                showToast("发送失败"+e);
                showLog("发送失败"+e);
            }
        });
    }




     /* private void showIfNeedEditDialog(final Uri imageUri) {
        DialogUtils.showListDialog(this, "是否需要编辑图片?", new String[]{"编辑图片", "使用原图"},
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            Intent intent = new Intent(WriteStatusActivity.this, ImageFilterActivity.class);
                            intent.putExtra("path", ImageUtils.getImageAbsolutePath(WriteStatusActivity.this, imageUri));
                            startActivityForResult(intent, 1234);
                        } else {
                            imgUris.add(imageUri);
                            updateImgs();
                        }
                    }
                });
    }*/

}
