package com.example.newoneday.bomb.model;

import android.content.Context;

import cn.bmob.v3.BmobInstallation;


public class CustomInstallation extends BmobInstallation {
        private static final Long serialVerisionUID = 1L;
        /**
         * 用户用户名,用于和设备进行绑定
         */
        private String uid;

        public CustomInstallation(Context context) {
                super();
        }

        public String getUid() {
                return uid;
        }

        public void setUid(String uid) {
                this.uid = uid;
        }
}
