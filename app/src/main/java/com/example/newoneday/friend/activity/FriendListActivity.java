package com.example.newoneday.friend.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newoneday.bomb.model.DemoMessageHandler;
import com.example.newoneday.R;
import com.example.newoneday.bomb.data.MyUser;
import com.example.newoneday.friend.adapter.FriendListAdapter;
import com.example.newoneday.friend.data.Friend;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class FriendListActivity extends AppCompatActivity {

    public List<MyUser> friendList = new ArrayList<>();
    private FriendListAdapter friendListAdapter;
    private ListView friendListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

//        又忘了，要先findview
        friendListView = findViewById(R.id.friendlist_listview);
//        MyUser user = BmobUser.getCurrentUser(MyUser.class);
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


        queryFriendData();

    }

    public void queryFriendData(){
        BmobQuery<Friend> querySendReq = new BmobQuery<Friend>();
//        因为是指针，所以user对应的不是id，而是MyUser对象
        querySendReq.addWhereEqualTo("user",BmobUser.getCurrentUser(MyUser.class));

        BmobQuery<Friend> queryAcceptReq = new BmobQuery<>();
        queryAcceptReq.addWhereEqualTo("friendUser",BmobUser.getCurrentUser(MyUser.class));

        List<BmobQuery<Friend>> queries = new ArrayList<>();
        queries.add(querySendReq);
        queries.add(queryAcceptReq);
        BmobQuery<Friend> mainQuery = new BmobQuery<>();
        mainQuery.or(queries);
        //      关联表要带的指针项要Include进去
        mainQuery.include("user");
        mainQuery.include("friendUser");

        mainQuery.findObjects(new FindListener<Friend>() {  //按行查询，查到的数据放到List<BmobPerson>的集合
            @Override

            public void done(List<Friend> list, BmobException e) {

                if (e == null) {

                    for(Friend friend: list){
                        friendList.add(friend.getFriendUser());
//                        friendList.add(friend);
//                        Toast.makeText(FriendListActivity.this, "查詢数据成功，返回objectId为："+friend.getFriendUser().getUsername(), Toast.LENGTH_SHORT).show();
                    }
                    for(MyUser friend: friendList){

//                        friendList.add(friend);
                        Log.d("FriendListActivity","查詢数据成功，返回objectId为："+friend.getUsername()+friend.getMobilePhoneNumber());
//                        Toast.makeText(FriendListActivity.this, "查詢数据成功，返回objectId为："+friend, Toast.LENGTH_SHORT).show();
                    }

                    friendListAdapter = new FriendListAdapter(FriendListActivity.this, R.layout.listview_friendlist_managemnt_friend_item, friendList);
                    friendListView.setAdapter(friendListAdapter);

                } else {
                    Toast.makeText(FriendListActivity.this, "查詢失敗", Toast.LENGTH_SHORT).show();
                }
            }

        });


    }
}
