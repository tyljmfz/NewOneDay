package com.example.newoneday.friend.model;

import android.text.TextUtils;
import android.util.Log;

import com.example.newoneday.bomb.model.Config;
import com.example.newoneday.friend.data.NewFriend;

import org.json.JSONObject;

import cn.bmob.newim.bean.BmobIMExtraMessage;
import cn.bmob.newim.bean.BmobIMMessage;

/**
 * Created by yongg on 2019/2/6.
 */

public class AddFriendMessage extends BmobIMExtraMessage {

    public static final String ADD = "add";
    public static String addFriendReqContent;
    public static String addFriendReqName;
    public static NewFriend add;
    public AddFriendMessage() {
    }

    /**
     * 将BmobIMMessage转成NewFriend
     *
     * @param msg 消息
     * @return
     */
    public static NewFriend convert(BmobIMMessage msg) {
        add = new NewFriend();
        addFriendReqContent = msg.getContent();
        Log.i("AddFriendMessage",addFriendReqContent);
        add.setMsg(addFriendReqContent);
        add.setTime(msg.getCreateTime());
        add.setStatus(Config.STATUS_VERIFY_NONE);
        try {
            String extra = msg.getExtra();
            if (!TextUtils.isEmpty(extra)) {
                JSONObject json = new JSONObject(extra);
                addFriendReqName = json.getString("name");
                Log.i("AddFriendMessage",addFriendReqName);
                add.setName(addFriendReqName);
//                String avatar = json.getString("avatar");
//                add.setAvatar(avatar);
                add.setUid(json.getString("uid"));
            } else {
                Log.i("AddFriendMessage","AddFriendMessage的extra为空");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return add;
    }


    @Override
    public String getMsgType() {
        return ADD;
    }

    @Override
    public boolean isTransient() {
        //设置为true,表明为暂态消息，那么这条消息并不会保存到本地db中，SDK只负责发送出去
        //设置为false,则会保存到指定会话的数据库中
        return true;
    }
}
