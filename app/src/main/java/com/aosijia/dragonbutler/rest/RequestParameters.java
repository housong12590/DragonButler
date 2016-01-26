package com.aosijia.dragonbutler.rest;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wanglj on 15/12/15.
 */
public class RequestParameters {

    /**
     * 获取unix时间戳
     *
     * @return
     */
    public static String getTimeStamp() {
        String timestamp = (System.currentTimeMillis() / 1000) + "";
//        String timestamp = "1449908478";
        return timestamp;
    }


    /**
     * 获取短信验证码
     *
     * @param mobile 手机号
     * @return
     */
    public static Map<String, String> verificationCode(String mobile) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("mobile", mobile);
        return signature(parameters);
    }

    /**
     * 验签
     *
     * @param parameters
     * @return
     */
    public static Map<String, String> signature(Map<String, String> parameters) {
        parameters.put("timestamp", getTimeStamp());
        parameters.put("signature", HmacSHA1.getSignature(parameters));
//        parameters.put("signature","e70b4edb139c6c37533f8ad8d2b44ffd4884973a");
        return parameters;
    }

    /**
     * 登录
     *
     * @param mobile           手机号
     * @param verificationCode 验证码
     * @return
     */
    public static Map<String, String> login(String mobile, String verificationCode) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("mobile", mobile);
        parameters.put("verification_code", verificationCode);
        return signature(parameters);
    }

    /**
     * 获取用户信息
     *
     * @param access_token
     * @return
     */
    public static Map<String, String> userInfo(String access_token) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        return signature(parameters);
    }

    /**
     * 绑定房屋编号
     *
     * @param verification_code 验证码
     * @param access_token
     * @return
     */
    public static Map<String, String> bindHousehold(String verification_code, String access_token) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("verification_code", verification_code);
//        return parameters;
        return signature(parameters);
    }


    /**
     * 查询社区公告列表
     * 用小区编码community_id请求小区公告列表，公告按时间降序排列
     *
     * @param access_token
     * @param community_announcement_id
     * @param page_size
     * @return
     */
    public static Map<String, String> communityAnnouncements(String access_token, String community_announcement_id, String page_size) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        if (community_announcement_id != null) {
            parameters.put("community_announcement_id", community_announcement_id);
        }

        if (page_size != null) {
            parameters.put("page_size", page_size);
        }
        return signature(parameters);
    }

    /**
     * 查询物业账单列表
     * 获取用户房号的物业账单账单，账单按创建时间降序排列
     *
     * @param access_token
     * @param status       1-未支付；2-已支付。默认查询所有账单
     * @param bill_id      上一页最后一条账单id，首页不需要此参数
     * @param page_size    每页账单数量，默认为20
     * @return
     */
    public static Map<String, String> propertyBills(String access_token, String status, String bill_id, String page_size) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        if (status != null) {
            parameters.put("status", status);
        }
        if (bill_id != null) {
            parameters.put("bill_id", bill_id);
        }

        if (page_size != null) {
            parameters.put("page_size", page_size);
        }
        return signature(parameters);
    }

    /**
     * 物业报修
     *
     * @param access_token
     * @param content      报修内容
     * @param type         报修类型
     * @param pic_urls     图片URL
     * @return 物业报修请求参数
     */
    public static Map<String, String> maintenanceOrderSubmit(String access_token, String content, String type, String pic_urls) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("content", content);
        parameters.put("type", type);
        if (pic_urls != null)
            parameters.put("pic_urls", pic_urls);
        return signature(parameters);
    }

    /**
     * 物业报修列表
     *
     * @param access_token
     * @param page_size            每页报修单数量，默认为20
     * @param maintenance_order_id 上一页最后一条报修单id，首页不需要此参数
     * @return 物业报修列表请求参数
     */
    public static Map<String, String> maintenanceOrders(String access_token, String page_size, String maintenance_order_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        if (page_size != null) {
            parameters.put("page_size", page_size);
        }
        if (maintenance_order_id != null) {
            parameters.put("maintenance_order_id", maintenance_order_id);
        }
        return signature(parameters);
    }


    /**
     * 确认报修问题解决
     *
     * @param maintenance_order_id 报修单ID
     * @param access_token
     * @return 确认报修单请求参数
     */
    public static Map<String, String> maintenanceOrderConfirm(String maintenance_order_id, String access_token) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("maintenance_order_id", maintenance_order_id);
        parameters.put("access_token", access_token);
        return signature(parameters);
    }


    /**
     * 物业投诉
     *
     * @param access_token
     * @param content      投诉内容
     * @param pic_urls     图片URL，图片为0~5张。如没有图片，则该参数为一个空Array
     * @return
     */
    public static Map<String, String> complaintSubmit(String access_token, String content, String pic_urls) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("content", content);
        if (pic_urls != null) {
            parameters.put("pic_urls", pic_urls);
        }

        return signature(parameters);
    }

    /**
     * 物业投诉列表
     *
     * @param access_token
     * @param page_size    每页投诉单数量，默认为20
     * @param complaint_id 上一页最后一条报修单id，首页不需要此参数
     * @return
     */
    public static Map<String, String> complaints(String access_token, String page_size, String complaint_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        if (page_size != null) {
            parameters.put("page_size", page_size);
        }
        if (complaint_id != null) {
            parameters.put("complaint_id", complaint_id);
        }
        return signature(parameters);
    }

    /**
     * 取消投诉
     *
     * @param complaint_id 投诉ID
     * @param access_token
     * @return
     */
    public static Map<String, String> complaintCancel(String complaint_id, String access_token) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("complaint_id", complaint_id);
        return signature(parameters);
    }

    /**
     * 确认投诉已解决
     *
     * @param complaint_id 投诉ID
     * @param access_token
     * @return
     */
    public static Map<String, String> complaintConfirm(String complaint_id, String access_token) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("complaint_id", complaint_id);
        return signature(parameters);
    }


    /**
     * 确认/取消报修单
     *
     * @param maintenance_order_id 报修单ID
     * @param access_token
     * @return
     */
    public static Map<String, String> maintenanceOrderCancelOrConfirm(String maintenance_order_id, String access_token) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("maintenance_order_id", maintenance_order_id);
        parameters.put("access_token", access_token);
        return signature(parameters);
    }

    /**
     * 首页银行公告
     *
     * @return
     */
    public static Map<String, String> bankAdvertisements() {
        Map<String, String> parameters = new HashMap<>();
        return signature(parameters);
    }

    /**
     * 发表帖子（含普通帖子、活动和投票）
     *
     * @param access_token
     * @param title        帖子标题
     * @param content      帖子内容
     * @param type         帖子类型
     * @param pic_urls     图片URL数组 可为null
     * @param start_date   开始时间
     * @param end_date     结束时间
     * @param options      选项列表，仅投票帖子有此参数，不能少于两项
     * @return
     */
    public static Map<String, String> forumTopicCreate(String access_token, String title, String content, String type,
                                                       String pic_urls, String start_date, String end_date, String options) {
        Map<String, String> parameters = new HashMap<>();
        if (pic_urls != null) {
            parameters.put("pic_urls", pic_urls);
        }
        //仅投票才需传此参数
        if (options != null && !TextUtils.isEmpty(options)) {
            parameters.put("options", options);
        }
        //仅活动才有此参数
        if (start_date != null && !TextUtils.isEmpty(start_date)) {
            parameters.put("start_date", start_date);
        }
        if (end_date != null && !TextUtils.isEmpty(end_date)) {
            parameters.put("end_date", end_date);
        }
        parameters.put("access_token", access_token);
        parameters.put("title", title);
        parameters.put("content", content);
        parameters.put("type", type);
        return signature(parameters);
    }

    /**
     * 查询论坛帖子列表
     *
     * @param access_token  访问令牌
     * @param page_size     每页帖子数量，默认为20
     * @param last_topic_id 上一页最后一条帖子id，首页不需要此参数
     * @return
     */
    public static Map<String, String> forumTopics(String access_token, String page_size, String last_topic_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        if (page_size != null) {
            parameters.put("page_size", page_size);
        }
        if (last_topic_id != null) {
            parameters.put("last_topic_id", last_topic_id);
        }
        return signature(parameters);
    }

    /**
     * 查询帖子(含普通帖子、活动和投票)详情 get
     *
     * @param forum_topic_id 帖子ID
     * @param access_token   访问令牌
     * @return
     */
    public static Map<String, String> forumTopic(String forum_topic_id, String access_token) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("forum_topic_id", forum_topic_id);
        return signature(parameters);
    }

    /**
     * 查询跟帖列表 get
     *
     * @param forum_topic_id   论坛帖子ID
     * @param page_size        默认为20
     * @param forum_comment_id 上一页跟帖最后一个跟帖的ID，首页不要传此参数
     * @return
     */
    public static Map<String, String> forumComments(String access_token, String forum_topic_id, String page_size, String forum_comment_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("forum_topic_id", forum_topic_id);
        if (page_size != null) {
            parameters.put("page_size", page_size);
        }
        if (forum_comment_id != null) {
            parameters.put("forum_comment_id", forum_comment_id);
        }
        return signature(parameters);
    }


    /**
     * 论坛跟帖（评论/回复） post
     *
     * @param access_token   访问令牌
     * @param forum_topic_id 主帖ID
     * @param content        跟帖内容
     * @param reply_of       如果是回复某条评论，则此参数为被回复的评论的ID
     * @return
     */
    public static Map<String, String> forumCommentCreate(String access_token, String forum_topic_id, String content, String reply_of) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("forum_topic_id", forum_topic_id);
        parameters.put("content", content);
        if (reply_of != null) {
            parameters.put("reply_of", reply_of);
        }
        return signature(parameters);
    }


    /**
     * 删除帖子 post
     *
     * @param access_token   访问令牌
     * @param forum_topic_id 帖子ID
     * @return
     */
    public static Map<String, String> forumTopicDelete(String access_token, String forum_topic_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("forum_topic_id", forum_topic_id);
        return signature(parameters);
    }

    /**
     * 删除评论  post
     *
     * @param access_token     访问令牌
     * @param forum_comment_id 跟帖ID
     * @return
     */
    public static Map<String, String> forumCommentDelete(String access_token, String forum_comment_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("forum_comment_id", forum_comment_id);
        parameters.put("access_token", access_token);
        return signature(parameters);
    }


    /**
     * 请求二手市场物品列表
     *
     * @param access_token
     * @param type
     * @return
     */
    public static Map<String, String> secondhandItems(String access_token, String type, String last_secondhand_item_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("type", type);
        parameters.put("access_token", access_token);
        if (last_secondhand_item_id != null) {
            parameters.put("last_secondhand_item_id", last_secondhand_item_id);
        }

        return signature(parameters);
    }

    /**
     * 发布二手商品，含转让和求购
     * 标题必填，1~20个字符
     * 描述选填，不大于200个字符
     * 联系电话选填，必须为合法的手机号码
     * 价格不填，则默认为-1（面议），如填写则必须为合法正数
     *
     * @param access_token 访问令牌
     * @param title        二手标题
     * @param content      描述
     * @param pic_urls     图片URL数组
     * @param prices       价格，一般为正数，如果用户用户不填，则默认为-1（表示价格面议）
     * @param type         交易类型：1-转让；2-求购
     * @param mobile       发布人的联系电话，可以为空
     * @return map
     */
    public static Map<String, String> secondhandItemCreate(String access_token, String title, String content, String pic_urls, String prices, String type, String mobile) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("title", title);
        if (content != null) {
            parameters.put("content", content);
        }
        if (pic_urls != null) {
            parameters.put("pic_urls", pic_urls);
        }
        parameters.put("price", prices);
        parameters.put("type", type);
        if (mobile != null) {
            parameters.put("mobile", mobile);
        }

        return signature(parameters);
    }

    /**
     * 查看二手商品详情
     *
     * @param access_token
     * @param secondhand_item_id 二手商品ID
     * @return
     */
    public static Map<String, String> secondhandItem(String access_token, String secondhand_item_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("secondhand_item_id", secondhand_item_id);
        return signature(parameters);
    }

    /**
     * 收藏论坛帖子或者二手商品
     *
     * @param access_token 访问令牌
     * @param type         类型：1-论坛；2-二手
     * @param favorite_id  如果是论坛类型，该参数为帖子的forum_topic_id;如果是二手类型，该参数为二手商品的secondhand_item_id
     * @return
     */
    public static Map<String, String> favoriteForumAndCommodity(String access_token, String type, String favorite_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("type", type);
        parameters.put("favorite_id", favorite_id);
        return signature(parameters);
    }

    /**
     * 取消收藏论坛帖子或者二手商品
     *
     * @param access_token 访问令牌
     * @param type         类型：1-论坛；2-二手
     * @param favorite_id  如果是论坛类型，该参数为帖子的forum_topic_id;如果是二手类型，该参数为二手商品的secondhand_item_id
     * @return
     */
    public static Map<String, String> favoriteCancelForumAndCommodity(String access_token, String type, String favorite_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("type", type);
        parameters.put("favorite_id", favorite_id);
        return signature(parameters);
    }

    /**
     * 编辑(修改)论坛普通帖子  post
     *
     * @param access_token   访问令牌
     * @param forum_topic_id 被编辑帖子的ID
     * @param title          帖子标题
     * @param content        帖子内容
     * @param pic_urls       图片URL数组，可以为空
     * @return
     */
    public static Map<String, String> forumTopicUpdate(String access_token, String forum_topic_id, String title, String content, String pic_urls) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("forum_topic_id", forum_topic_id);
        parameters.put("title", title);
        parameters.put("content", content);
        if (pic_urls != null)
            parameters.put("pic_urls", pic_urls);
        return signature(parameters);
    }


    public static Map<String, String> forumTopicVote(String access_token, String forum_topic_id, String vote_option_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("forum_topic_id", forum_topic_id);
        parameters.put("vote_option_id", vote_option_id);
        return signature(parameters);
    }


    /**
     * 二手发布取消
     *
     * @param access_token       访问令牌
     * @param secondhand_item_id 二手商品ID
     * @return
     */
    public static Map<String, String> secondhandItemCancelOrClose(String access_token, String secondhand_item_id) {
        Map<String, String> parameters = new HashMap<>();
        if (access_token != null) {
            parameters.put("access_token", access_token);
        }
        parameters.put("secondhand_item_id", secondhand_item_id);
        return signature(parameters);
    }


    /**
     * 我的发布-邻里
     *
     * @param access_token
     * @param last_topic_id
     * @return
     */
    public static Map<String, String> myForumTopics(String access_token, String last_topic_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        if (last_topic_id != null) {
            parameters.put("last_topic_id", last_topic_id);
        }
        return signature(parameters);
    }


    /**
     * 我的发布-二手
     *
     * @param access_token
     * @param last_item_id
     * @return
     */
    public static Map<String, String> mySecondhandItems(String access_token, String last_item_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        if (last_item_id != null) {
            parameters.put("last_item_id", last_item_id);
        }
        return signature(parameters);
    }

    /**
     * 我的收藏列表
     *
     * @param access_token
     * @param last_item_id
     * @param type
     * @return
     */
    public static Map<String, String> favorites(String access_token, String last_item_id, String type) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("type", type);
        if (last_item_id != null) {
            parameters.put("last_item_id", last_item_id);
        }
        return signature(parameters);
    }

    /**
     * 我的活动
     *
     * @param access_token
     * @return
     */
    public static Map<String, String> my_participation(String access_token) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        return signature(parameters);
    }

    /**
     * 我的评论
     *
     * @param access_token
     * @param last_comment_id
     * @param type
     * @return
     */
    public static Map<String, String> myComments(String access_token, String last_comment_id, String type) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        if (last_comment_id != null) {
            parameters.put("last_comment_id", last_comment_id);
        }
        parameters.put("type", type);
        return signature(parameters);
    }

    /**
     * 参加活动
     *
     * @param access_token   访问令牌
     * @param forum_topic_id 活动贴子ID
     * @return
     */
    public static Map<String, String> forumTopicJoin(String access_token, String forum_topic_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("forum_topic_id", forum_topic_id);
        return signature(parameters);
    }

    /**
     * 编辑(修改)二手发布
     *
     * @param access_token       访问令牌
     * @param secondhand_item_id 被编辑的二手交易ID
     * @param title              二手标题
     * @param content            描述
     * @param pic_urls           图片URL数组
     * @param price              价格，一般为正数，如果用户用户不填，则默认为-1（表示价格面议）
     * @param type               交易类型：1-转让；2-求购
     * @param mobile             发布人的联系电话，可以为空
     * @return
     */
    public static Map<String, String> secondhandItemUpdate(String access_token, String secondhand_item_id, String title, String content, String pic_urls, String price, String type, String mobile) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("secondhand_item_id", secondhand_item_id);
        parameters.put("title", title);
        parameters.put("content", content);
        if (pic_urls != null) {
            parameters.put("pic_urls", pic_urls);
        }
        if (price != null) {
            parameters.put("price", price);
        }
        parameters.put("type", type);
        if (mobile != null) {
            parameters.put("mobile", mobile);
        }
        return signature(parameters);
    }

    /**
     * 更新个人信息
     *
     * @param access_token
     * @param avatar_url
     * @param nickname
     * @param gender
     * @return
     */
    public static Map<String, String> userInfoUpdate(String access_token, String avatar_url, String nickname, String gender) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("avatar_url", avatar_url);
        parameters.put("nickname", nickname);
        parameters.put("gender", gender);
        return signature(parameters);
    }

    /**
     * 获取我的信息箱
     *
     * @param access_token
     * @param page_size
     * @param last_timestamp
     * @return
     */
    public static Map<String, String> messageBox(String access_token, String page_size, String last_timestamp) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        if (page_size != null) {
            parameters.put("page_size", page_size);
        }
        if (!TextUtils.isEmpty(last_timestamp)) {
            parameters.put("last_timestamp", last_timestamp);
        }
        return signature(parameters);
    }

    /**
     * 查询私信详情
     *
     * @param access_token
     * @param contact_user_id
     * @param page_size
     * @param message_id
     * @return
     */
    public static Map<String, String> messages(String access_token, String contact_user_id, String page_size, String message_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("contact_user_id", contact_user_id);
        if (page_size != null) {
            parameters.put("page_size", page_size);
        }
        if (message_id != null) {
            parameters.put("message_id", message_id);
        }
        return signature(parameters);
    }

    /**
     * 发送私信
     *
     * @param access_token
     * @param receiver_id  接收者的用户ID
     * @param content      内容
     * @return
     */
    public static Map<String, String> messageCreate(String access_token, String receiver_id, String content) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        parameters.put("receiver_id", receiver_id);
        parameters.put("content", content);
        return signature(parameters);
    }

    /**
     * 未读消息个数
     *
     * @param access_token
     * @return
     */
    public static Map<String, String> unreadMessage(String access_token) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        return signature(parameters);

    }

    /**
     * 查询二手跟帖列表
     *
     * @param access_token          访问令牌
     * @param secondhand_item_id    二手交易物品ID
     * @param page_size             默认为20
     * @param secondhand_comment_id 上一页跟帖最后一个跟帖的ID，首页不要传此参数
     * @return
     */
    public static Map<String, String> secondhandComments(String access_token, String secondhand_item_id, String page_size, String secondhand_comment_id) {
        Map<String, String> parameners = new HashMap<>();
        parameners.put("secondhand_item_id", secondhand_item_id);
        if (page_size != null) {
            parameners.put("page_size", page_size);
        }
        if (secondhand_comment_id != null) {
            parameners.put("secondhand_comment_id", secondhand_comment_id);
        }
        parameners.put("access_token", access_token);
        return signature(parameners);
    }

    /**
     * 二手跟帖（评论/回复）
     *
     * @param access_token       访问令牌
     * @param secondhand_item_id 二手商品ID
     * @param content            回复内容
     * @param reply_of           如果是回复某条评论，则此参数为被回复的评论的secondhand_comment_id
     * @return
     */
    public static Map<String, String> secondhandCommentCreate(String access_token, String secondhand_item_id, String content, String reply_of) {
        Map<String, String> parameners = new HashMap<>();
        parameners.put("access_token", access_token);
        parameners.put("secondhand_item_id", secondhand_item_id);
        parameners.put("content", content);
        if (reply_of != null) {
            parameners.put("reply_of", reply_of);
        }
        return signature(parameners);
    }

    /**
     * 意见反馈
     *
     * @param access_token 访问令牌
     * @param content      意见内容
     * @param os_type      操作系统类型：0-iOS，1-Android
     * @param version_code 版本代码
     * @return
     */
    public static Map<String, String> feedback(String access_token, String content, String os_type, String version_code) {
        Map<String, String> parameners = new HashMap<>();
        parameners.put("access_token", access_token);
        parameners.put("content", content);
        parameners.put("os_type", os_type);
        parameners.put("version_code", version_code);
        return signature(parameners);
    }

    /**
     * 版本检查
     *
     * @return
     */
    public static Map<String, String> version() {
        Map<String, String> parameners = new HashMap<>();
        return signature(parameners);
    }

    /**
     * 社区黄页  get
     *
     * @param access_token 访问令牌
     * @return
     */
    public static Map<String, String> yellowPages(String access_token) {
        Map<String, String> parameners = new HashMap<>();
        parameners.put("access_token", access_token);
        return signature(parameners);
    }

    /**
     * 系统消息 get
     *
     * @param access_token           访问令牌
     * @param page_size              默认为20
     * @param last_system_message_id 上页最后一条系统消息ID
     * @return
     */
    public static Map<String, String> systemMessages(String access_token, String page_size, String last_system_message_id) {
        Map<String, String> parameners = new HashMap<>();
        parameners.put("access_token", access_token);
        if (page_size != null) {
            parameners.put("page_size", page_size);
        }
        if (last_system_message_id != null) {
            parameners.put("last_system_message_id", last_system_message_id);
        }
        return signature(parameners);
    }

    /**
     * 查询未读系统消息条数
     *
     * @param access_token 访问令牌
     * @return
     */
    public static Map<String, String> unreadsystemMessages(String access_token) {
        Map<String, String> parameners = new HashMap<>();
        parameners.put("access_token", access_token);
        return signature(parameners);
    }

    /**
<<<<<<< HEAD
     * 优惠活动列表 get
     *
     * @param access_token         访问令牌
     * @param page_size            默认为20
     * @param last_ccb_activity_id 上一页的最后一条ID
     * @return
     */
    public static Map<String, String> ccbActivities(String access_token, String page_size, String last_ccb_activity_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        if (page_size != null) {
            parameters.put("page_size", page_size);
        }
        if (last_ccb_activity_id != null) {
            parameters.put("last_ccb_activity_id", last_ccb_activity_id);
        }
        return signature(parameters);
    }

    /**
     * 社区便利店 get
     * @param access_token 访问令牌
     * @param page_size 默认为20
     * @param last_cvs_item_id 上页最后一条ID
     * @return
     */
    public static Map<String, String> convenienceStore(String access_token, String page_size, String last_cvs_item_id) {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("access_token", access_token);
        if (page_size != null) {
            parameters.put("page_size", page_size);
        }
        if (last_cvs_item_id != null) {
            parameters.put("last_cvs_item_id", last_cvs_item_id);
        }
        return signature(parameters);
    }

    /**
     * @param old_mobile
     * @param old_mobile_vericode
     * @param new_mobile
     * @param new_mobile_vericode
     * @return
     */
    public static Map<String,String> updateUserMobile(String access_token,String old_mobile,String old_mobile_vericode,String new_mobile,String new_mobile_vericode){
        Map<String,String> parameners = new HashMap<>();
        parameners.put("access_token",access_token);
        parameners.put("old_mobile",old_mobile);
        parameners.put("old_mobile_vericode",old_mobile_vericode);
        parameners.put("new_mobile",new_mobile);
        parameners.put("new_mobile_vericode",new_mobile_vericode);
        return signature(parameners);
    }
}







