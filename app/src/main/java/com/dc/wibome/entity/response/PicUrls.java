package com.dc.wibome.entity.response;

import android.text.TextUtils;

/**
 *  json 中图片的类
 */
public class PicUrls extends BaseEntity {
    // 中等质量图片url前缀
    private static final String BMIDDLE_URL = "http://ww3.sinaimg.cn/bmiddle";
    // 原质量图片url前缀
    private static final String ORIGINAL_URL = "http://ww3.sinaimg.cn/large";

    private String thumbnail_pic;  //缩略图片地址，没有时不返回此字段
    private String bmiddle_pic;   //中等尺寸图片地址，没有时不返回此字段
    private String original_pic;  // 原始图片地址，没有时不返回此字段

    private boolean showOriImag;//标识符用于是否显示原图

    /**
     * 从缩略图url中截取末尾的图片id,用于和拼接成其他质量图片url
     */
    public String getImageId() {
        //取最后位置加获取索引
        int indexOf = thumbnail_pic.lastIndexOf("/");
        return thumbnail_pic.substring(indexOf);//截取字符串
    }

    public String getThumbnail_pic() {

        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {

        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        //返回的中等图片为空时 ，拼接字符串，获取中等图地址
        return TextUtils.isEmpty(bmiddle_pic) ? BMIDDLE_URL + getImageId() : bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        //返回的原始图片为空时 ，拼接字符串，获取原始图地址
        return TextUtils.isEmpty(original_pic) ? ORIGINAL_URL + getImageId() : original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }

    public boolean isShowOriImag() {
        return showOriImag;
    }

    public void setShowOriImag(boolean showOriImag) {
        this.showOriImag = showOriImag;
    }
}
