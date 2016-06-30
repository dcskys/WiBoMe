package com.dc.wibome.entity.response;

import java.util.ArrayList;

/**
 * 获取当前登录用户及其所关注（授权）用户的最新微博，第一个数组json
 * 微博（status）的一些信息
 */
public class Status extends BaseEntity{


    private String created_at;   //微博创建时间
    private long id;        //微博ID
    private String mid;    //微博MID
    private String idstr;  //字符串型的微博ID
    private String text;   //微博信息内容
    private int source_allowclick;
    private int source_type;
    private String source;     //微博来源
    private boolean favorited;  //是否已收藏，true：是，false：否
    private boolean truncated;  //是否被截断，true：是，false：否
    private String in_reply_to_status_id;  //（暂未支持）回复ID
    private String in_reply_to_user_id;   //（暂未支持）回复人UID
    private String in_reply_to_screen_name;  //暂未支持）回复人昵称
    private ArrayList<PicUrls> pic_urls;
    private String thumbnail_pic;   //缩略图片地址，没有时不返回此字段
    private String bmiddle_pic;    //中等尺寸图片地址，没有时不返回此字段
    private String original_pic;  // 原始图片地址，没有时不返回此字段
    private Object geo;           //地理信息字段 详细
    private int reposts_count;    //转发数
    private int comments_count;  //评论数
    private int attitudes_count; //表态数
    private User user;               //微博作者的用户信息字段 详细
    private Status retweeted_status;   //引用微博
    private boolean liked;
    private int mlevel;           //暂未支持
    private Visible visible;    //微博的可见性及指定可见分组信息。该object中type取值，0：普通微博，1：私密微博，3：指定分组微博，4：密友微博；list_id为分组的组号
    private ArrayList<Object> darwin_tags;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public String getIdstr() {
        return idstr;
    }

    public void setIdstr(String idstr) {
        this.idstr = idstr;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getSource_allowclick() {
        return source_allowclick;
    }

    public void setSource_allowclick(int source_allowclick) {
        this.source_allowclick = source_allowclick;
    }

    public int getSource_type() {
        return source_type;
    }

    public void setSource_type(int source_type) {
        this.source_type = source_type;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public boolean isTruncated() {
        return truncated;
    }

    public void setTruncated(boolean truncated) {
        this.truncated = truncated;
    }

    public String getIn_reply_to_status_id() {
        return in_reply_to_status_id;
    }

    public void setIn_reply_to_status_id(String in_reply_to_status_id) {
        this.in_reply_to_status_id = in_reply_to_status_id;
    }

    public String getIn_reply_to_user_id() {
        return in_reply_to_user_id;
    }

    public void setIn_reply_to_user_id(String in_reply_to_user_id) {
        this.in_reply_to_user_id = in_reply_to_user_id;
    }

    public String getIn_reply_to_screen_name() {
        return in_reply_to_screen_name;
    }

    public void setIn_reply_to_screen_name(String in_reply_to_screen_name) {
        this.in_reply_to_screen_name = in_reply_to_screen_name;
    }

    public ArrayList<PicUrls> getPic_urls() {
        return pic_urls;
    }

    public void setPic_urls(ArrayList<PicUrls> pic_urls) {
        this.pic_urls = pic_urls;
    }

    public String getThumbnail_pic() {
        return thumbnail_pic;
    }

    public void setThumbnail_pic(String thumbnail_pic) {
        this.thumbnail_pic = thumbnail_pic;
    }

    public String getBmiddle_pic() {
        return bmiddle_pic;
    }

    public void setBmiddle_pic(String bmiddle_pic) {
        this.bmiddle_pic = bmiddle_pic;
    }

    public String getOriginal_pic() {
        return original_pic;
    }

    public void setOriginal_pic(String original_pic) {
        this.original_pic = original_pic;
    }

    public Object getGeo() {
        return geo;
    }

    public void setGeo(Object geo) {
        this.geo = geo;
    }

    public int getReposts_count() {
        return reposts_count;
    }

    public void setReposts_count(int reposts_count) {
        this.reposts_count = reposts_count;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getAttitudes_count() {
        return attitudes_count;
    }

    public void setAttitudes_count(int attitudes_count) {
        this.attitudes_count = attitudes_count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getRetweeted_status() {
        return retweeted_status;
    }

    public void setRetweeted_status(Status retweeted_status) {
        this.retweeted_status = retweeted_status;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public int getMlevel() {
        return mlevel;
    }

    public void setMlevel(int mlevel) {
        this.mlevel = mlevel;
    }

    public Visible getVisible() {
        return visible;
    }

    public void setVisible(Visible visible) {
        this.visible = visible;
    }

    public ArrayList<Object> getDarwin_tags() {
        return darwin_tags;
    }

    public void setDarwin_tags(ArrayList<Object> darwin_tags) {
        this.darwin_tags = darwin_tags;
    }


    @Override
    public boolean equals(Object o) {
        if (o instanceof Status) {
            return this.id == ((Status) o).id;
        }
        return super.equals(o);
    }




}
