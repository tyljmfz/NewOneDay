package com.example.newoneday.friend.adapter;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newoneday.bomb.model.Config;
import com.example.newoneday.R;
import com.example.newoneday.friend.data.NewFriend;
import com.example.newoneday.friend.model.NewFriendManager;
import com.example.newoneday.friend.model.AddFriendMessage;
import com.example.newoneday.friend.model.AgreeAddFriendMessage;
import com.example.newoneday.listener.interf.ToastControl;
import com.example.newoneday.bomb.data.MyUser;
import com.example.newoneday.bomb.model.UserModel;

import java.util.HashMap;
import java.util.List;

import android.widget.Toast;

import com.orhanobut.logger.Logger;

import java.util.Map;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import static com.example.newoneday.friend.model.AddFriendMessage.add;


/**
 * Created by yongg on 2019/2/2.
 */

public class FriendAdapter extends ArrayAdapter<NewFriend> {
    private int resourceId;
//    public static Integer keyVaule;

    public ImageView agreeFriendRequest;
    public ImageView disAgreeFriendRequest;

//    Toast 接口
    ToastControl toastControl;
    public FriendAdapter(Context context, int textViewResourceId, List<NewFriend> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    /**
     * TODO 好友管理：9.10、添加到好友表中再发送同意添加好友的消息
     *
     * @param add
     * @param listener
     */
    private void agreeAdd(final NewFriend add, final SaveListener<Object> listener) {
        MyUser user = new MyUser();
        user.setObjectId(add.getUid());
        UserModel.getInstance()
                .agreeAddFriend(user, new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            sendAgreeAddFriendMessage(add, listener);
                        } else {
                            Logger.e(e.getMessage());
                            listener.done(null, e);
                        }
                    }
                });
    }

    /**
     * 发送同意添加好友的消息
     */
    //TODO 好友管理：9.8、发送同意添加好友
    private void sendAgreeAddFriendMessage(final NewFriend add, final SaveListener<Object> listener) {
        if (BmobIM.getInstance().getCurrentStatus().getCode() != ConnectionStatus.CONNECTED.getCode()) {
            Log.d("FriendAdapter","尚未连接IM服务器");
            return;
        }
        BmobIMUserInfo info = new BmobIMUserInfo(add.getUid(), add.getName(), null);
        //TODO 会话：4.1、创建一个暂态会话入口，发送同意好友请求
        BmobIMConversation conversationEntrance = BmobIM.getInstance().startPrivateConversation(info, true, null);
        //TODO 消息：5.1、根据会话入口获取消息管理，发送同意好友请求
        BmobIMConversation messageManager = BmobIMConversation.obtain(BmobIMClient.getInstance(), conversationEntrance);
        //而AgreeAddFriendMessage的isTransient设置为false，表明我希望在对方的会话数据库中保存该类型的消息
        AgreeAddFriendMessage msg = new AgreeAddFriendMessage();
        final MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
        msg.setContent("我通过了你的好友验证请求，我们可以开始 聊天了!");//这句话是直接存储到对方的消息表中的
        Map<String, Object> map = new HashMap<>();
        map.put("msg", currentUser.getUsername() + "同意添加你为好友");//显示在通知栏上面的内容
        map.put("uid", add.getUid());//发送者的uid-方便请求添加的发送方找到该条添加好友的请求
        map.put("time", add.getTime());//添加好友的请求时间
        msg.setExtraMap(map);
        if(messageManager != null){
            messageManager.sendMessage(msg, new MessageSendListener() {
                @Override
                public void done(BmobIMMessage msg, BmobException e) {
                    if (e == null) {//发送成功
                        NewFriendManager.getInstance(getContext()).updateNewFriend(add, Config.STATUS_VERIFIED);
                        listener.done(msg, e);
                    } else {//发送失败
                        Log.d("FriendAdapter",e.getMessage());
                        listener.done(msg, e);
                    }
                }

            });
        }

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        NewFriend friend = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        final TextView friendName = view.findViewById(R.id.show_request_friend_name_textview);
        friendName.setText(AddFriendMessage.addFriendReqName);

        agreeFriendRequest = view.findViewById(R.id.friend_request_agree_imageview);
        disAgreeFriendRequest = view.findViewById(R.id.friend_request_disagree_imageview);

        if(add != null){
            agreeFriendRequest.setVisibility(View.VISIBLE);
            disAgreeFriendRequest.setVisibility(View.VISIBLE);

        }else{
            agreeFriendRequest.setVisibility(View.INVISIBLE);
            disAgreeFriendRequest.setVisibility(View.INVISIBLE);
        }
        agreeFriendRequest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if( add != null){
                    agreeAdd(add, new SaveListener<Object>() {
                        @Override
                        public void done(Object o, BmobException e) {
                            if (e == null) {
                                Log.d("FriendAdapter","已添加");
                                Toast.makeText(getContext(),"Add friends successfully！",Toast.LENGTH_SHORT).show();
                            } else {
//                            holder.setEnabled(R.id.btn_aggree, true);
                                Logger.e("添加好友失败:" + e.getMessage());
//                            toast("添加好友失败:" + e.getMessage());
                            }
                        }
                    });
                }else{
                    Toast.makeText(getContext(),"Fail to add new friends, please check your network",Toast.LENGTH_SHORT).show();
                }


                Log.d("FriendAdapter","点击了添加按钮");
            }
        });

        return view;
    }



}
