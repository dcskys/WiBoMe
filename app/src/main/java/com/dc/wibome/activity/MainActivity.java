package com.dc.wibome.activity;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.dc.wibome.BaseActivity;
import com.dc.wibome.R;
import com.dc.wibome.fragment.FragmentController;
import com.dc.wibome.utils.ToastUtils;

/*FragmentActivity  必须继承这个
* */


public class MainActivity extends FragmentActivity implements RadioGroup.OnCheckedChangeListener {

    private RadioGroup rg_tab;
    private ImageView iv_add;
    private FragmentController controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //设置竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //添加fragment到activity中framelayout布局中，并显示第一个界面
        controller = FragmentController.getInstance(this, R.id.fl_content);
        controller.showFragment(0);

        initView();
    }

    private void initView() {
        rg_tab = (RadioGroup) findViewById(R.id.rg_tab);
        iv_add = (ImageView) findViewById(R.id.iv_add);

        rg_tab.setOnCheckedChangeListener(this);
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, WriteStatusActivity.class);
                startActivityForResult(intent, 110);
            }
        });
    }

    //点击标题栏，这里使用隐藏全部fragment,显示某一个的方式进行呈现，
    //由于通过show,hide  不走生命周期
    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_home:
                controller.showFragment(0);
                break;
            case R.id.rb_meassage:
                controller.showFragment(1);
                break;
            case R.id.rb_search:
                controller.showFragment(2);
                break;
            case R.id.rb_user:
                controller.showFragment(3);
                break;
            default:
                break;
        }

    }


}
