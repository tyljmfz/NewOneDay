package com.example.newoneday.friend.adapter;

/**
 * Created by yongg on 2019/2/2.
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newoneday.R;
import com.example.newoneday.bomb.data.MyUser;
import com.example.newoneday.friend.model.AddFriendMessage;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.bean.BmobIMConversation;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobIMClient;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.MessageSendListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class AddFriendAdapter extends ArrayAdapter<BmobUser> {

    private int resourceId;
//    public static Integer keyVaule;
    private Context mContext = getContext();
    private List<MyUser>data;
    private LayoutInflater layoutInflater;
    private UpdateListener updateListener;
    BmobIMUserInfo info;
    BmobUser bmobUser;
    BmobUser currentUser;

    public AddFriendAdapter(Context context, int textViewResourceId, List<BmobUser> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        bmobUser = getItem(position);
        currentUser = BmobUser.getCurrentUser(MyUser.class);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        TextView friendName = view.findViewById(R.id.show_search_friend_name_textview);
        Button addFriend = view.findViewById(R.id.add_friend_button);
        friendName.setText(bmobUser.getUsername());
//        viewHolder holder = null;

//        连接服务器

        //点击添加按钮
        addFriend.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                sendAddFriendMessage();
                Log.d("AddFriendAdapter","点击了添加按钮");
            }
        });

        return view;
    }

    private void sendAddFriendMessage(){
        BmobIM.init(mContext);
        BmobIM.connect(currentUser.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Log.d("AddFriendAdapter","connect success");
                    info = new BmobIMUserInfo(bmobUser.getObjectId(), bmobUser.getUsername(),null);
                    //启动一个暂态会话，也就是isTransient为true,表明该会话仅执行发送消息的操作，不会保存会话和消息到本地数据库中，
                    BmobIMConversation c = BmobIM.getInstance().startPrivateConversation(info, true,null);
                    //这个obtain方法才是真正创建一个管理消息发送的会话
                    BmobIMConversation conversation = BmobIMConversation.obtain(BmobIMClient.getInstance(), c);
                    //新建一个添加好友的自定义消息实体
                    AddFriendMessage msg =new AddFriendMessage();
                    MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
                    msg.setContent("很高兴认识你，可以做个朋友吗?");//给对方的一个留言信息
                    Map<String,Object> map =new HashMap<>();
                    map.put("name", currentUser.getUsername());//发送者姓名，这里只是举个例子，其实可以不需要传发送者的信息过去
                    map.put("uid",currentUser.getObjectId());//发送者的uid
                    msg.setExtraMap(map);
                    if(conversation!= null && msg != null){
                        conversation.sendMessage(msg, new MessageSendListener() {
                            @Override
                            public void done(BmobIMMessage msg, BmobException e) {
                                if (e == null) {//发送成功
                                    Toast.makeText(getContext(),"好友请求发送成功，等待验证",Toast.LENGTH_SHORT).show();
//                                    Log.d("AddFriendAdapter", "好友请求发送成功，等待验证");
//                    toast("好友请求发送成功，等待验证");
                                } else {//发送失败
                                    Log.d("AddFriendAdapter", "发送失败:" + e.getMessage());
//                    toast("发送失败:" + e.getMessage());
                                }
                            }
                        });
                    }else{

                        Toast.makeText(getContext(),"发送失败，请再次尝试",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),"发送失败，请再次尝试",Toast.LENGTH_SHORT).show();
                }
            }
        });
//        MyUser user = BmobUser.getCurrentUser(MyUser.class);
//        这个user是希望添加的好友方


    }


    /**
     * 获取本用户ID
     *
     * @return 用户ID
     */
    public String getCurrentUserObjectId() {
        return BmobUser.getCurrentUser(MyUser.class).getObjectId();
    }
}

