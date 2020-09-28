package com.example.newoneday.listener.interf;

import com.example.newoneday.bomb.data.MyUser;


import cn.bmob.v3.exception.BmobException;



public interface CancelBlackCallBlackListener {

        void onSuccess(MyUser user);

        void onFailed(BmobException e);
}
