package com.example.newoneday.listener;

import com.example.newoneday.bomb.data.MyUser;

import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;


public abstract class QueryUserListener extends BmobListener1<MyUser> {

    public abstract void done(MyUser s, BmobException e);

    @Override
    protected void postDone(MyUser o, BmobException e) {
        done(o, e);
    }
}
