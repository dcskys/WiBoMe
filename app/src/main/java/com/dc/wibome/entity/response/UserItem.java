package com.dc.wibome.entity.response;

/**
 * 列表数据
 */
public class UserItem {

    public UserItem(boolean isShowTopDivider, int leftImg, String subhead, String caption) {
        this.isShowTopDivider = isShowTopDivider;
        this.leftImg = leftImg;
        this.subhead = subhead;
        this.caption = caption;
    }

    private boolean isShowTopDivider; //是否显示顶部的分割部分
    private int leftImg;  //左边的图像
    private String subhead; //主题信息
    private String caption; //辅助信息

    public boolean isShowTopDivider() {

        return isShowTopDivider;
    }

    public void setShowTopDivider(boolean isShowTopDivider) {
        this.isShowTopDivider = isShowTopDivider;
    }

    public int getLeftImg() {

        return leftImg;
    }

    public void setLeftImg(int leftImg) {

        this.leftImg = leftImg;
    }

    public String getSubhead() {

        return subhead;

    }

    public void setSubhead(String subhead) {

        this.subhead = subhead;
    }

    public String getCaption() {

        return caption;
    }

    public void setCaption(String caption) {
        
        this.caption = caption;
    }

}
