package com.aosijia.dragonbutler.rest;

/**
 * Created by wanglj on 15/12/15.
 */
public final class URLManager {

    public static final String STATUS_CODE_OK = "200";
    public static final String STATUS_CODE_OK_TOAST = "100";
    public static final String STATUS_INVALID_ACCESSTOKEN = "30000";
    /**
     * API KEY
     */
    public static final String API_SECRET = "pdcHYNAkItD41yIXj6FxMjss3yml3Q9C";
    /**
     * 本机服务器地址
     */
//    public static final String SERVER_API_URL = "http://192.168.0.101:12306";
    /**
     * 测试外网服务器环境
     */
//    public static final String SERVER_API_URL = "http://iiseeuumobile.ngrok.cc";

    /**
     * 正式服务器环境
     */
    public static final String SERVER_API_URL = "https://dragonbutler.memeyin.com";


    public static final String SERVER_API_PATH = "/api";

    public static final String SERVER_API_VERSION = "/v1";

    public static final String SERVER_URL = SERVER_API_URL + SERVER_API_PATH + SERVER_API_VERSION;


    /**
     * 发送短信验证码
     */
    public static final String VERIFICATION_CODE = SERVER_URL + "/verification_code";

    /**
     * 登录
     */
    public static final String USER_LOGIN = SERVER_URL + "/user/login";

    /**
     * 获取个人信息
     */
    public static final String USER_INFO = SERVER_URL + "/user/info";

    /**
     * 房号绑定
     */
    public static final String BIND_HOUSEHOLD = SERVER_URL + "/household/bind";

    /**
     * 查询社区公告列表
     */
    public static final String COMMUNITY_ANNOUNCEMENTS = SERVER_URL + "/community_announcements";

    /**
     * 查询物业账单列表
     */
    public static final String PROPERTY_BILLS = SERVER_URL + "/property_bills";

    /**
     * 物业报修 post
     */
    public static final String MAINTENANCE_ORDER_SUBMIT = SERVER_URL + "/maintenance_order/submit";

    /**
     * 取消报修单（撤销） post
     */
    public static final String MAINTENANCE_ORDER_CANCEL = SERVER_URL + "/maintenance_order/cancel";

    /**
     * 确认报修问题解决 post
     */
    public static final String MAINTENANCE_ORDER_CONFIRM = SERVER_URL + "/maintenance_order/confirm";

    /**
     * 物业报修列表 get
     */
    public static final String MAINTENANCE_ORDERS = SERVER_URL + "/maintenance_orders";

    /**
     * 修改报修单状态
     */
    public static final String MAINTENANCE_PROCESS = SERVER_URL + "/maintenance_process";

    /**
     * 物业投诉 post
     */
    public static final String COMPLAINT_SUBMIT = SERVER_URL + "/complaint/submit";

    /**
     * 物业投诉列表 get
     */
    public static final String COMPLAINTS = SERVER_URL + "/complaints";

    /**
     * 确认投诉已解决 post
     */
    public static final String COMPLAINT_CONFIRM = SERVER_URL + "/complaint/confirm";

    /**
     * 取消投诉 post
     */
    public static final String COMPLAINT_CANCEL = SERVER_URL + "/complaint/cancel";

    /**
     * 发表帖子（含普通帖子、活动和投票）post
     */
    public static final String FORUM_TOPIC_CREATE = SERVER_URL + "/forum_topic/create";

    /**
     * 帖子列表post
     */
    public static final String FORUM_TOPICS = SERVER_URL + "/forum_topics";

    /**
     * 查询帖子(含普通帖子、活动和投票)详情 get
     */
    public static final String FORUM_TOPIC = SERVER_URL + "/forum_topic";

    /**
     * 查询跟帖列表 get
     */
    public static final String FORUM_COMMENTS = SERVER_URL + "/forum_comments";

    /**
     * 论坛跟帖（评论/回复） post
     */
    public static final String FORUM_COMMENT_CREATE = SERVER_URL + "/forum_comment/create";

    /**
     * 二手跟帖（评论/回复）
     */
    public static final String SECONDHAND_COMMENT_CREATE = SERVER_URL + "/secondhand_comment/create";

    /**
     * 删除帖子 post
     */
    public static final String FORUM_TOPIC_DELETE = SERVER_URL + "/forum_topic/delete";

    /**
     * 删除评论 post
     */
    public static final String FORUM_COMMENT_DELETE = SERVER_URL + "/forum_comment/delete";

    /**
     * 请求二手市场物品列表 get
     */
    public static final String SECONDHAND_ITEMS = SERVER_URL + "/secondhand_items";

    /**
     * 发布二手商品，含转让和求购 post
     */
    public static final String SECONDHAND_ITEMS_CREATE = SERVER_URL + "/secondhand_item/create";

    /**
     * 查看二手商品详情
     */
    public static final String SECONDHAND_ITEM = SERVER_URL + "/secondhand_item";

    /**
     * 二手发布取消
     */
    public static final String SECONDHAND_ITEM_CANCEL = SERVER_URL + "/secondhand_item/cancel";

    /**
     * 二手交易完成
     */
    public static final String SECONDHAND_ITEM_CLOSE = SERVER_URL + "/secondhand_item/close";

    /**
     * 收藏论坛帖子或者二手商品 post
     */
    public static final String FAVORITE_CREATE = SERVER_URL + "/favorite/create";

    /**
     * 编辑(修改)论坛普通帖子
     */
    public static final String FORUM_TOPIC_UPDATE = SERVER_URL + "/forum_topic/update";


    public static final String FORUM_TOPIC_VOTE = SERVER_URL + "/forum_topic/vote";

    /**
     * 取消收藏
     */
    public static final String FAVORITE_CANCEL = SERVER_URL + "/favorite/cancel";

    public static final String FORUM_TOPIC_CLOSE_VOTE_ = SERVER_URL + "/forum_topic/close_vote";

    /**
     * 我的发布-邻里
     */
    public static final String MY_FORUM_TOPICS = SERVER_URL + "/my_forum_topics";

    /**
     * 我的发布-二手
     */
    public static final String MY_SECONDHAND_ITEMS = SERVER_URL + "/my_secondhand_items";
    /**
     * 我的收藏
     */
    public static final String FAVORITES = SERVER_URL + "/favorites";
    /**
     * 我的活动
     */
    public static final String MY_PARTICIPATION = SERVER_URL + "/my_participation";
    /**
     * 我的评论
     */
    public static final String MY_COMMENTS = SERVER_URL + "/my_comments";

    /**
     * 参加活动
     */
    public static final String FORUM_TOPIC_JOIN = SERVER_URL + "/forum_topic/join";

    /**
     * 编辑(修改)二手发布 post
     */
    public static final String SECONDHAND_ITEM_UPDATE = SERVER_URL + "/secondhand_item/update";


    /**
     * 修改个人信息
     */
    public static final String USER_INFO_UPDATE = SERVER_URL + "/user_info/update";

    /**
     * 我的私信列表
     */
    public static final String MESSAGE_BOX = SERVER_URL + "/message_box";
    /**
     * 发送私信
     */
    public static final String MESSAGE_CREATE = SERVER_URL + "/message/create";

    /**
     * 查询私信详情
     */
    public static final String MESSAGES = SERVER_URL + "/messages";

    /**
     * 查询未读私信
     */
    public static final String UNREAD_MESSAGES = SERVER_URL + "/unread_messages";


    /**
     * 查询二手跟帖列表
     */
    public static final String SECONDHAND_COMMENTS = SERVER_URL + "/secondhand_comments";

    /**
     * 查询二手跟帖列表
     */
    public static final String BANK_ADVERTISEMENTS = SERVER_URL + "/bank_advertisements";

    /**
     * 意见反馈
     */
    public static final String FEEDBACK = SERVER_URL + "/feedback";

    /**
     * 版本检查
     */
    public static final String VERSION = SERVER_URL + "/version";

    /**
     * 社区黄页
     */
    public static final String YELLOW_PAGES = SERVER_URL + "/yellow_pages";

    /**
     * 修改绑定手机号码
     */
    public static final String USER_MOBILE_UPDATE = SERVER_URL + "/user_mobile/update";

    /**
    * 系统消息
     */
    public static final String SYSTEM_MESSAGES = SERVER_URL + "/system_messages";

    /**
     * 查询未读系统消息条数 get
     */
    public static final String UNREAD_SYSTEM_MESSAGES = SERVER_URL + "/unread_system_messages";

    /**
     * 优惠活动列表
     */
    public static final String CCB_ACTIVITIES = SERVER_URL + "/ccb_activities";

    /**
     * 社区便利店
     */
    public static final String CONVENIENCE_STORE = SERVER_URL + "/convenience_store";
}
