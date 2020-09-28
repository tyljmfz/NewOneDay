package com.example.newoneday.friend.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newoneday.R;
import com.example.newoneday.bomb.data.MyUser;
import com.example.newoneday.friend.adapter.AddFriendAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SearchFriendActivity extends AppCompatActivity {


    private AutoCompleteTextView searchFriendNameView;
    private Button searchFriendAction;
    private List<BmobUser> friendList = new ArrayList<BmobUser>();
    private AddFriendAdapter adapter;
    private ListView showSearchFriendList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);

        searchFriendNameView = findViewById(R.id.friend_name);
        searchFriendAction = findViewById(R.id.search_friend_button);
        showSearchFriendList = findViewById(R.id.search_friend_listview);

//        adapter = new AddFriendAdapter(SearchFriendActivity.this, friendList);
//        Bmob.initialize(this, "86cb620aacc23833fb1d5cfe6d4c20a9");
        searchFriendAction.setOnClickListener(new searchFriend());


    }
    class searchFriend implements View.OnClickListener{
        @Override
        public void onClick(View v){
            serachFriend();
        }
    };
    private void serachFriend() {
        String inputFriendName = searchFriendNameView.getText().toString().trim();
        if(TextUtils.isEmpty(inputFriendName)){
            Toast.makeText(getApplicationContext(), "查询内容不能为空，请输入您想要添加的用户名", Toast.LENGTH_LONG).show();
            return;
        }
        String searchName = inputFriendName;
//        progress = new ProgressDialog(AddFrindActivity.this);
//        progress.setMessage("正在搜索...");
//        progress.setCanceledOnTouchOutside(true);
//        progress.show();
        searchService(searchName);
    }


        private void searchService(String searchName) {
            // TODO Auto-generated method stub
            BmobQuery<MyUser> query = new BmobQuery<MyUser>();
            query.addWhereEqualTo("username", searchName);


            query.findObjects(new FindListener<MyUser>() {  //按行查询，查到的数据放到List<BmobPerson>的集合
                @Override

//                这个地方是MyUser哈~~~！
                public void done(List<MyUser> list, BmobException e) {

                    if (e == null) {
                        for(BmobUser person: list){
//                            userNikeName = person.getNikeName().toString().trim();
//                            loginUser = person;
                            friendList.add(person);
                            Toast.makeText(SearchFriendActivity.this, "搜索用户成功，用户名为 ："+person.getUsername(), Toast.LENGTH_SHORT).show();
                        }
                        adapter = new AddFriendAdapter(SearchFriendActivity.this, R.layout.listview_search_add_friend_item, friendList);
                        showSearchFriendList.setAdapter(adapter);
//
                    } else {
                        Toast.makeText(SearchFriendActivity.this, "搜索失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    }
                }

            });
        }
}
