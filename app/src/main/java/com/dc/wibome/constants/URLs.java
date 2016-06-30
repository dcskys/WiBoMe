package com.dc.wibome.constants;


public interface URLs {

    String BASE_URL = "https://api.weibo.com/2/";

    // 获取用户信息
    String usersShow = BASE_URL + "users/show.json";

    // 获取某个用户最新发表的微博列表
    String statusesUser_timeline = BASE_URL + "statuses/user_timeline.json";

    //获取当前登录用户及其所关注（授权）用户的最新微博,地址由官方文档得到
    String statusesHome_timeline = BASE_URL + "statuses/home_timeline.json";

    // 微博评论列表
    String commentsShow = BASE_URL + "comments/show.json";
    // 对一条微博进行评论
    String commentsCreate = BASE_URL + "comments/create.json";


    // 转发一条微博
    String statusesRepost = BASE_URL + "statuses/repost.json";
    // 发布一条微博(带图片)
    String statusesUpload = BASE_URL + "statuses/upload.json";
    // 发布一条微博(不带图片)
    String statusesUpdate = BASE_URL + "statuses/update.json";

   /* String usersShow = BASE_URL + "users/show.json";
    String statusesUser_timeline = BASE_URL + "statuses/user_timeline.json";
    String statusesRepost_timeline = BASE_URL + "statuses/repost_timeline.json";
*/
}
