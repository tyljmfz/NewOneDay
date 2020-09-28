package com.example.newoneday.listener.interf;

import com.example.newoneday.bomb.data.MyUser;

//import chen.testchat.bean.User;
import cn.bmob.v3.exception.BmobException;

public interface AddFriendCallBackListener {
        void onSuccess(MyUser user);

        void onFailed(BmobException e);
}
