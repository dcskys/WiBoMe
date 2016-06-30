package com.dc.wibome.entity.response;

import java.util.ArrayList;

/**
 * 用来解析json 的类，根据首页列表返回的json数据，抽出所需要的（ Gosn解析）
 */
public class StatusTimeLineResponse {

    //类名一定要和返回json中数据一样，不能错

    private ArrayList<Status> statuses;  //返回这个类的数组


    private int total_number; //json返回的值，不清楚作用 ，总页数

    //get和set方法
    public ArrayList<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(ArrayList<Status> statuses) {
        this.statuses = statuses;
    }

    public int getTotal_number() {
        return total_number;
    }

    public void setTotal_number(int total_number) {
        this.total_number = total_number;
    }





}
