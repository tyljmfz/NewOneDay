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

public class NotUrgentGoalInfoActivity extends AppCompatActivity {

    public TextView notUrgentGoalName;
    public BmobGoal currentGoal;
    public EditText worryEditText;
    public Button saveWorry;
    public TextView startDateTextView;
    public TextView endDateTextView;
    public TextView minutesLeftTextView;
    public TextView congratulations1TextView;
    public TextView congratulations2TextView;
    public ImageView shareImageView;
    public ShareDialog shareDialog;
    public int goalStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_urgent_goal_info);

//      findViewById
        notUrgentGoalName = findViewById(R.id.goalname_not_urgent_goal_info);
        saveWorry = findViewById(R.id.worry_save_button_not_urgent_goal_info);
        worryEditText = findViewById(R.id.worry_editText_not_urgent_goal_info);
        startDateTextView = findViewById(R.id.goal_start_date_textview_not_urgent_goal_info);
        endDateTextView = findViewById(R.id.goal_end_date_textview_not_urgent_goal_info);
        minutesLeftTextView = findViewById(R.id.goal_minutes_left_textview_not_urgent_goal_info);
        congratulations1TextView = findViewById(R.id.goal_finished_congratulations1_not_urgent_goal_info);
        congratulations2TextView = findViewById(R.id.goal_finished_congratulations2_not_urgent_goal_info);
        shareImageView = findViewById(R.id.goal_share_imageview_not_urgent_goal_info);

//      获取目标信息
        currentGoal = GoalManagementActivity.currentGoal;
        notUrgentGoalName.setText(currentGoal.getGoalName());
        startDateTextView.setText(currentGoal.getGoalStartDate());
        endDateTextView.setText(currentGoal.getGoalEndDate());
        long lastingminutes = currentGoal.getLastingTime();
        long targetMinutes = currentGoal.getTargetHours();
        long minutesLeft = targetMinutes - lastingminutes;
        goalStatus = currentGoal.getGoalStatus();
        if ((goalStatus != 2) && (minutesLeft  > 0)){
            minutesLeftTextView.setText(minutesLeft + "");
        }else{
            minutesLeftTextView.setText("0");
            congratulations1TextView.setVisibility(View.VISIBLE);
            congratulations2TextView.setVisibility(View.VISIBLE);
        }


//        设置第一个reasonTodo以及保存
        worryEditText.setText(currentGoal.getReasonTodo());
        worryEditText.setFocusable(false);
        worryEditText.setFocusableInTouchMode(false);

//        点击效果
        worryEditText.setOnClickListener(new setClickable());
        saveWorry.setOnClickListener(new saveWorry());

//        Facebook 分享
        initFacebook();
        shareImageView.setOnClickListener(new shareToFacebook());


//        Resources r = this.getApplicationContext().getResources();
//        Bitmap test = BitmapFactory.decodeResource(r, R.drawable.angela);
//        Bitmap afterConvert = PictureUtils.gray2Binary(test);
//        Bitmap tes1 = PictureUtils.bitmap2Gray(test);
//        Bitmap tes2 = PictureUtils.lineGrey(test);
////        imageview 可以直接set bitmap类型的对象
//        shareImageView.setImageBitmap(afterConvert);
//        test1.setImageBitmap(tes1);
//        test2.setImageBitmap(tes2);

    }

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
                Toast.makeText(NotUrgentGoalInfoActivity.this, "Share to Facebook successfully", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(NotUrgentGoalInfoActivity.this, "Cancel Share to Facebook", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(NotUrgentGoalInfoActivity.this, "Some Errors happened when share to Facebook", Toast.LENGTH_SHORT).show();
            }
        });
    }


    class setClickable implements View.OnClickListener{
        @Override
        public void onClick(View v){
            worryEditText.setFocusableInTouchMode(true);
            worryEditText.setFocusable(true);
            worryEditText.requestFocus();
        }
    };

    class saveWorry implements View.OnClickListener{
        @Override
        public void onClick(View v){
            String newReason = worryEditText.getText().toString();
            currentGoal.setReasonTodo(newReason);
            String currentGoalId = currentGoal.getObjectId();
            currentGoal.update(currentGoalId, new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e  == null){
                        Log.d("NotUrgentGoalInfoAct",  "Update Successfully");
                    }else{
                        Log.d("NotUrgentGoalInfoAct",  "Update failed");
                    }
                }
            });
            worryEditText.setFocusable(false);
            worryEditText.setFocusableInTouchMode(false);
            Toast.makeText(NotUrgentGoalInfoActivity.this, "Save successfully", Toast.LENGTH_SHORT).show();
        }
    };
}
