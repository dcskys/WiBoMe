package com.dc.wibome.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dc.wibome.BaseActivity;
import com.dc.wibome.R;
import com.dc.wibome.adapter.ImageBrowserAdapter;
import com.dc.wibome.entity.response.PicUrls;
import com.dc.wibome.entity.response.Status;

import java.util.ArrayList;

/**
 * 图片浏览器
 */
public class ImageBrowserActivity extends BaseActivity implements View.OnClickListener {

    private ViewPager vp_image_brower;
    private TextView tv_image_index;
    private Button btn_save;
    private Button btn_original_image;

    private Status status;
    private int position;
    private ImageBrowserAdapter adapter;
    private ArrayList<PicUrls> imgUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_image_brower);

        initData();
        initView();
        setData();
    }

    private void setData() {
        adapter = new ImageBrowserAdapter(this, imgUrls);
        vp_image_brower.setAdapter(adapter);

        final int size = imgUrls.size();

        //为了实现传入Position为0时也能，左滑动
        //Integer.MAX_VALUE / 2 / size * size  例如50/2/9*9=45会损失精度 ，最大值一半左右，余数和Position一样
        int initPosition = Integer.MAX_VALUE / 2 / size * size + position;

        if(size > 1) { //多图时显示 3/9 textview
            tv_image_index.setVisibility(View.VISIBLE);
            tv_image_index.setText((position+1) + "/" + size);
        } else {//单图不显示
            tv_image_index.setVisibility(View.GONE);
        }

        //viewPager 改变监听
        vp_image_brower.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {

                int index = arg0 % size; //取余数才是实际位置
                tv_image_index.setText((index+1) + "/" + size);

                PicUrls pic = adapter.getPic(arg0); ////获取实际的集合中的图片
                //原图按钮显示还是隐藏
                btn_original_image.setVisibility(pic.isShowOriImag() ?
                        View.GONE : View.VISIBLE);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }
        });
        //为了能够实现0位置时左滑动 。
        //初始时 第一次呈现的view视图位置
        vp_image_brower.setCurrentItem(initPosition); //余数时，实际Position为0-8之间

    }


    private void initData() {
        //获取传入的对象
        status = (Status) getIntent().getSerializableExtra("status");
        position = getIntent().getIntExtra("position", 0);
        // 获取图片数据集合(单图也有对应的集合,集合的size为1)
        imgUrls = status.getPic_urls();
    }

    private void initView() {
        vp_image_brower = (ViewPager) findViewById(R.id.vp_image_brower);
        tv_image_index = (TextView) findViewById(R.id.tv_image_index);
        btn_save = (Button) findViewById(R.id.btn_save);

        btn_original_image = (Button) findViewById(R.id.btn_original_image);

        btn_save.setOnClickListener(this);
        btn_original_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        //当前显示位置的图片
        PicUrls picUrl = adapter.getPic(vp_image_brower.getCurrentItem());

        switch (v.getId()) {
            case R.id.btn_save:
                //获取bitmap图片
                Bitmap bitmap = adapter.getBitmap(vp_image_brower.getCurrentItem());

                boolean showOriImag = picUrl.isShowOriImag();
                //设置图片文件名
                String fileName = "img-" + (showOriImag?"ori-" : "mid-") + picUrl.getImageId();

                //把文件名移除后缀 .   保存会自动添加后缀，多余
                String title = fileName.substring(0, fileName.lastIndexOf("."));
                //系统自带保存图片方法，简单，会自动生成缩略图
                String insertImage = MediaStore.Images.Media.insertImage(
                        getContentResolver(), bitmap, title, "BoreWBImage");

                if(insertImage == null) {
                    showToast("图片保存失败");
                } else {
                    showToast("图片保存成功");
                }

                //ImageUtils.saveFile 保存图片的另一种方法 ，好处，可以指定保存路径
//			try {
//				ImageUtils.saveFile(this, bitmap, fileName);
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

                break;
            case R.id.btn_original_image:

                picUrl.setShowOriImag(true); //显示原图
                adapter.notifyDataSetChanged(); //更新adapter
                btn_original_image.setVisibility(View.GONE); //隐藏原图按钮

                break;
        }


    }
}
