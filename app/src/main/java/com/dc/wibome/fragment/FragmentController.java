package com.dc.wibome.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * fragment控制器，进行fragment的切换
 */
public class FragmentController {

    private int containerId;  //呈放fragment的主容器id,也就是mainActivity中framelayout布局id
    private FragmentManager fm;

    private ArrayList<Fragment> fragments;

   //本类对象的静态引用
    private static FragmentController controller;

    //使用单引模式，只用这一个控制器就可以了，提供一个获取本类对象的静态方法
    //外部只能通过getInstance去获取本类对象，且永远都是同一个对象
    public static FragmentController getInstance(FragmentActivity activity, int containerId) {
        if (controller == null) {
            controller = new FragmentController(activity, containerId);
        }
        return controller;
    }
     //私有化构造方法，防止其他地方去new本类对象
    private FragmentController(FragmentActivity activity, int containerId) {
        this.containerId = containerId;
        fm = activity.getSupportFragmentManager();  //获取activity管理fragment对象
        initFragment();
    }

    private void initFragment() {
          //把所有的fragment添加到activity中
        fragments = new ArrayList<Fragment>();
        fragments.add(new HomeFragment());
        fragments.add(new MessageFragment());
        fragments.add(new SearchFragment());
        fragments.add(new UserFragment());

        FragmentTransaction ft = fm.beginTransaction();
        for(Fragment fragment : fragments) {
            ft.add(containerId, fragment);
        }
        ft.commit();
    }


    public void showFragment(int position) {
        hideFragments();//隐藏所有
        Fragment fragment = fragments.get(position); //显示选中的fragment
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fragment);
        ft.commit();
    }

    public void hideFragments() {
        //循环整个集合，隐藏所有fragment
        FragmentTransaction ft = fm.beginTransaction();
        for(Fragment fragment : fragments) {
            if(fragment != null) {
                ft.hide(fragment);
            }
        }
        ft.commit();
    }

    public Fragment getFragment(int position) {

        return fragments.get(position);
    }
}
