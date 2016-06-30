package com.dc.wibome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.dc.wibome.BaseActivity;
import com.dc.wibome.R;
import com.dc.wibome.api.BoreWeiboApi;
import com.dc.wibome.constants.AccessTokenKeeper;
import com.dc.wibome.constants.WeiboConstants;
import com.dc.wibome.entity.response.Status;
import com.dc.wibome.utils.TitleBuilder;
import com.dc.wibome.utils.ToastUtils;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;

/**
 * 写评论界面
 */
public class WriteCommentActivity extends BaseActivity implements View.OnClickListener {

    // 评论输入框
    private EditText et_write_status;
    // 底部按钮
    private ImageView iv_image;
    private ImageView iv_at;
    private ImageView iv_topic;
    private ImageView iv_emoji;
    private ImageView iv_add;
    // 待评论的微博
    private Status status;

    private Oauth2AccessToken token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_write_status);

        // 获取Intent传入的微博
        status = (Status) getIntent().getSerializableExtra("status");

        initView();

    }

    private void initView() {

        new TitleBuilder(this)
                .setTitleText("发评论")
                .setLeftText("取消")
                .setLeftOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 取消发送评论,关闭本页面
                        WriteCommentActivity.this.finish();
                    }
                })
                .setRightText("发送")
                .setRightOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        sendComment();
                    }
                });

        et_write_status = (EditText) findViewById(R.id.et_write_status);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        iv_at = (ImageView) findViewById(R.id.iv_at);
        iv_topic = (ImageView) findViewById(R.id.iv_topic);
        iv_emoji = (ImageView) findViewById(R.id.iv_emoji);
        iv_add = (ImageView) findViewById(R.id.iv_add);

        iv_image.setOnClickListener(this);
        iv_at.setOnClickListener(this);
        iv_topic.setOnClickListener(this);
        iv_emoji.setOnClickListener(this);
        iv_add.setOnClickListener(this);
    }

    private void sendComment() {
        //输入的内容
        String comment = et_write_status.getText().toString();
        if(TextUtils.isEmpty(comment)) {
            showToast("评论内容不能为空");
            return;
        }

        //实例化微博api ,可以改进到BaseActivity
        token  = AccessTokenKeeper.readAccessToken(this);
        BoreWeiboApi api = new BoreWeiboApi(this, WeiboConstants.APP_KEY,token);


        api.commentsCreate(status.getId(), comment, new RequestListener() {
            @Override
            public void onComplete(String s) {

                showToast("微博发送成功");

                // 微博发送成功后,设置Result结果数据,然后关闭本页面
                Intent data = new Intent();
                data.putExtra("sendCommentSuccess", true);
                setResult(RESULT_OK, data);

                WriteCommentActivity.this.finish();

            }

            @Override
            public void onWeiboException(WeiboException e) {
                showToast("发送失败"+e);
                showLog("发送失败"+e);
            }
        });



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_image:
                break;
            case R.id.iv_at:
                break;
            case R.id.iv_topic:
                break;
            case R.id.iv_emoji:
                break;
            case R.id.iv_add:
                break;
        }

    }
}
