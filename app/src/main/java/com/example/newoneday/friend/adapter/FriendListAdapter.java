package com.example.newoneday.friend.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.newoneday.R;
import com.example.newoneday.bomb.data.MyUser;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by yongg on 2019/2/14.
 */

public class FriendListAdapter extends ArrayAdapter<MyUser> {
    private int resourceId;
//    public static Integer keyVaule;
    private Context mContext = getContext();
    public TextView currentFriendName;
    public TextView disAgreeFriendRequest;
    public MyUser currentUser;

    public FriendListAdapter(Context context, int textViewResourceId, List<MyUser> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
//        这个friend其实就是objects
        MyUser friend = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        currentUser = BmobUser.getCurrentUser(MyUser.class);

        currentFriendName = view.findViewById(R.id.show_friend_name_textview);

        currentFriendName.setText(friend.getUsername());
        return view;
    }
}
