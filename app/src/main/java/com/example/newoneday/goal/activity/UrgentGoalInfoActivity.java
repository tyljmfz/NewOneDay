package com.example.newoneday.goal.activity;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newoneday.R;
import com.example.newoneday.bomb.data.BmobGoal;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UrgentGoalInfoActivity extends AppCompatActivity {

    public TextView urgentGoalName;
    public BmobGoal currentGoal;
    public EditText reasonTodoEditText;
    public Button saveReasonTodo;
    public TextView startDateTextView;
    public TextView endDateTextView;
    public TextView lastingMinutesTextView;
    public ImageView shareImageView;
    public TextView congratulateTextView;
    public int goalStatus;
    public ShareDialog shareDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urgent_goal_info);

//      findViewById
        urgentGoalName = findViewById(R.id.goalname_urgent_goal_info);
        saveReasonTodo = findViewById(R.id.reason_todo_save_button_urgent_goal_info);
        reasonTodoEditText = findViewById(R.id.reason_todo_editText_urgent_goal_info);
        startDateTextView = findViewById(R.id.goal_start_date_textview_urgent_goal_info);
        endDateTextView = findViewById(R.id.goal_end_date_textview_urgent_goal_info);
        lastingMinutesTextView = findViewById(R.id.goal_lasting_minutes_textview_urgent_goal_info);
        shareImageView = findViewById(R.id.goal_share_imageview_urgent_goal_info);
        congratulateTextView = findViewById(R.id.goal_finished_congratulations_urgent_goal_info);

//      获取目标信息
        currentGoal = GoalManagementActivity.currentGoal;
        urgentGoalName.setText(currentGoal.getGoalName());
        startDateTextView.setText(currentGoal.getGoalStartDate());
        endDateTextView.setText(currentGoal.getGoalEndDate());
        lastingMinutesTextView.setText(currentGoal.getLastingTime()+"");
        goalStatus = currentGoal.getGoalStatus();


//        设置第一个reasonTodo以及保存
        reasonTodoEditText.setText(currentGoal.getReasonTodo());
        reasonTodoEditText.setFocusable(false);
        reasonTodoEditText.setFocusableInTouchMode(false);

//        点击效果
        reasonTodoEditText.setOnClickListener(new setClickable());
        saveReasonTodo.setOnClickListener(new saveReason());

//        分享
        initFacebook();
        shareImageView.setOnClickListener(new UrgentGoalInfoActivity.shareToFacebook());
    }

    class setClickable implements View.OnClickListener{
        @Override
        public void onClick(View v){
            reasonTodoEditText.setFocusableInTouchMode(true);
            reasonTodoEditText.setFocusable(true);
            reasonTodoEditText.requestFocus();
        }
    };

    class saveReason implements View.OnClickListener{
        @Override
        public void onClick(View v){
            String newReason = reasonTodoEditText.getText().toString();
            currentGoal.setReasonTodo(newReason);
            String currentGoalId = currentGoal.getObjectId();
            currentGoal.update(currentGoalId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e  == null){
                        Log.d("UrgentGoalInfoActivity",  "Update Successfully");
                    }else{
                        Log.d("UrgentGoalInfoActivity",  "Update failed");
                    }
                }
            });
            reasonTodoEditText.setFocusable(false);
            reasonTodoEditText.setFocusableInTouchMode(false);
            Toast.makeText(UrgentGoalInfoActivity.this, "Save reason successfully", Toast.LENGTH_SHORT).show();
        }
    };
    class shareToFacebook implements View.OnClickListener{
        @Override
        public void onClick(View v){
            if (ShareDialog.canShow(ShareLinkContent.class)) {
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
                        .build();
                shareDialog.show(linkContent);
            }
        }
    };

    private void initFacebook() {
        //抽取成员变量
        CallbackManager callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {
                //分享成功的回调，在这里做一些自己的逻辑处理
                Toast.makeText(UrgentGoalInfoActivity.this, "Share to Facebook successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(UrgentGoalInfoActivity.this, "Cancel Share to Facebook", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(UrgentGoalInfoActivity.this, "Some Errors happened when share to Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
