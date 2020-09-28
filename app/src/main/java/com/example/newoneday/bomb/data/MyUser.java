package com.example.newoneday.bomb.data;

import cn.bmob.v3.BmobUser;

/**
 * Created by yongg on 2019/2/1.
 */

public class MyUser extends BmobUser {
    private static final long serialVersionUID = 1L;
    private boolean loginState;         //登录状态
    private String nick_URL_NET;        //头像,将图像上传至服务器后会返回URL 保存
    private String nick_URL_LOCAL;      //头像,将图像保存至本地   路径
    public long time;                   //记录最近一次登录时间，自动登录保存1个星期


    public boolean isLoginState() {
        return loginState;
    }
    public void setLoginState(boolean loginState) {
        this.loginState = loginState;
    }

    public String getNick_URL_NET() {
        return nick_URL_NET;
    }
    public void setNick_URL_NET(String nick_URL_NET) {
        this.nick_URL_NET = nick_URL_NET;
    }
    public String getNick_URL_LOCAL() {
        return nick_URL_LOCAL;
    }
    public void setNick_URL_LOCAL(String nick_URL_LOCAL) {
        this.nick_URL_LOCAL = nick_URL_LOCAL;
    }
    public MyUser() {
        super();
    }
    public long getTime() {
        return time;
    }
    public void setTime(long time) {
        this.time = time;
    }
    @Override
    public String toString() {
        return "MyUser [loginState=" + loginState + ", " +
                " nick_URL_NET=" + nick_URL_NET + ", nick_URL_LOCAL="
                + nick_URL_LOCAL + ", time=" + time + "]";
    }

}
