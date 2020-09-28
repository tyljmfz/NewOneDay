package com.example.newoneday.friend.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.newoneday.bomb.data.MyUser;

import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.v3.BmobUser;

/**
 * Created by yongg on 2019/2/6.
 */

public class UserInfoActivity extends AppCompatActivity {

    //用户
    MyUser user;
    //用户信息
    BmobIMUserInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_user_info);

        //用户
        user = BmobUser.getCurrentUser(MyUser.class);
//        MyUser = (MyUser) getBundle().getSerializable("u");
//        if (user.getObjectId().equals(getCurrentUid())) {//用户为登录用户
//            btn_add_friend.setVisibility(View.GONE);
//            btn_chat.setVisibility(View.GONE);
//        } else {//用户为非登录用户
//            btn_add_friend.setVisibility(View.VISIBLE);
//            btn_chat.setVisibility(View.VISIBLE);
//        }
        //构造聊天方的用户信息:传入用户id、用户名和用户头像三个参数
        info = new BmobIMUserInfo(user.getObjectId(), user.getUsername(),null);

        //显示名称
//        tv_name.setText(user.getUsername());
    }
}
