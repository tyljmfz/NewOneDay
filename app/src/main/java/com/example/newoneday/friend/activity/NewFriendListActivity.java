package com.example.newoneday.friend.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.newoneday.bomb.model.DemoMessageHandler;
import com.example.newoneday.R;
import com.example.newoneday.bomb.data.MyUser;
import com.example.newoneday.friend.adapter.FriendAdapter;
import com.example.newoneday.friend.data.NewFriend;
import com.example.newoneday.friend.model.AddFriendMessage;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

import static cn.bmob.newim.core.BmobIMClient.getContext;

public class NewFriendListActivity extends AppCompatActivity {

    private List<NewFriend> friendList = new ArrayList<NewFriend>();
    public static List<String> friendName = new ArrayList<>();
    public static List<Integer> friendId =  new ArrayList<>();
//    public ImageView iv_conversation_tips;
//    public ImageView iv_contact_tips;
//    private static Context context;
    private Context mContext = getContext();
    void initCurrentFriendList(){
        friendList.add(AddFriendMessage.add);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_request);
        MyUser user = BmobUser.getCurrentUser(MyUser.class);
//        iv_conversation_tips = findViewById(R.id.add_friend_imageview);
//        iv_contact_tips = findViewById(R.id.iv_contact_tips);

        initCurrentFriendList();
        FriendAdapter adapter = new FriendAdapter(NewFriendListActivity.this, R.layout.listview_friend_request_item, friendList);
        ListView listView = (ListView) findViewById(R.id.friend_request_listview);
        listView.setAdapter(adapter);
        Bmob.initialize(this, "86cb620aacc23833fb1d5cfe6d4c20a9");
        BmobIM.init(this);
////        //注册消息接收器
        BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
////        調試接受好友請求
        MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
        BmobIM.connect(currentUser.getObjectId(), new ConnectListener() {
            @Override
            public void done(String uid, BmobException e) {
                if (e == null) {
                    Log.d("DemoMessageHandler","connect success");
                } else {
                    Log.d("DemoMessageHandler","connect fail");
                }
            }
        });
        BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
    }


}
