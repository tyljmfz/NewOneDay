package com.example.newoneday.friend.data;

import com.example.newoneday.bomb.data.MyUser;

import cn.bmob.v3.BmobObject;

/**
 * Created by yongg on 2019/2/2.
 */

public class Friend extends BmobObject {

    private MyUser user;
    private MyUser friendUser;

    private transient String pinyin;

    public String getPinyin() {
        return pinyin;
    }

    public void setPinyin(String pinyin) {
        this.pinyin = pinyin;
    }

    public MyUser getUser() {
        return user;
    }

    public void setUser(MyUser user) {
        this.user = user;
    }

    public MyUser getFriendUser() {
        return friendUser;
    }

    public void setFriendUser(MyUser friendUser) {
        this.friendUser = friendUser;
    }


}
