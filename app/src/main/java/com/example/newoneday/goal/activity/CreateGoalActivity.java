package com.example.newoneday.goal.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import org.litepal.LitePal
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.newoneday.R;
import com.example.newoneday.bomb.data.BmobGoal;
import com.example.newoneday.bomb.data.MyUser;

import java.util.Calendar;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class CreateGoalActivity extends AppCompatActivity {

    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private TextView setGoalName;
    private TextView setDate;
    private TextView startDate;
    private TextView endDate;
    private TextView setGoalTargetHours;
    private Button createGoal;
//    private RadioGroup goalDifficultyGroup;
    private RadioGroup goalUrgencyGroup;
    private Boolean goalUrgentState;  //如果是true就是Urgent


    class setGoalDate implements View.OnClickListener{
        @Override
        public void onClick(View v){
            chooseDate();
        }
    };

    private void chooseDate() {
        View view = LayoutInflater.from(this).inflate(R.layout.create_goal_dialog_date, null);
        final DatePicker startTime = (DatePicker) view.findViewById(R.id.start_date_datepicker);
        final DatePicker endTime = (DatePicker) view.findViewById(R.id.end_date_datepicker);
        startTime.updateDate(startTime.getYear(), startTime.getMonth(), 01);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Start & End Date");
        builder.setView(view);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int month = startTime.getMonth() + 1;
                String startTimeShow = "" + startTime.getYear() + "." + month + "." + startTime.getDayOfMonth();
                startDate.setText(startTimeShow);
                int month1 = endTime.getMonth() + 1;
                String endTimeShow = "" + endTime.getYear() + "." + month1 + "." + endTime.getDayOfMonth();
                endDate.setText(endTimeShow);
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
        //自动弹出键盘问题解决
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }


    class registerGoal implements View.OnClickListener{
        @Override
        public void onClick(View v){


            MyUser user = BmobUser.getCurrentUser(MyUser.class);
            BmobGoal newGoal = new BmobGoal();
//            newGoal.setObjectId("ESIt3334");
            newGoal.setGoalName(setGoalName.getText().toString().trim());
            newGoal.setGoalStartDate(startDate.getText().toString().trim());
            newGoal.setGoalEndDate(endDate.getText().toString().trim());
            newGoal.setUrgent(goalUrgentState);
            String targetHoursString = setGoalTargetHours.getText().toString().trim();
            long millstargetHoursLong = Long.parseLong(targetHoursString);
            long mintuesTargetHoursLong = millstargetHoursLong * 60;    //转换为分钟数
            newGoal.setTargetHours(mintuesTargetHoursLong);
            newGoal.setRelatedUser(user);
            newGoal.save(new SaveListener<String>() {

                @Override
                public void done(String objectId,BmobException e) {
                    if(e==null){
                        Toast.makeText(CreateGoalActivity.this, "创建目标成功", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(CreateGoalActivity.this, "创建目标失败，失败原因 "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }

            });

        }
    };

//    class setGoalDifficulty implements RadioGroup.OnCheckedChangeListener{
//        @Override
//        public void onCheckedChanged(RadioGroup rg,int checkedId)
//        {
//            switch(checkedId){
//                case R.id.goal_difficult_radiobutton:
//
//                    break;
//                case R.id.goal_easy_radiobutton:
//
//                    break;
//            }
//        }
//
//    };

    class setGoalUrgency implements RadioGroup.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(RadioGroup rg,int checkedId)
        {
            switch(checkedId){
                case R.id.goal_urgent_radiobutton:
                    goalUrgentState = true;
                    break;
                case R.id.goal_not_urgent_radiobutton:
                    goalUrgentState = false;
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_goal);

        setGoalName = findViewById(R.id.goal_set_name_textview);
        setDate = findViewById(R.id.goal_set_time_textview);
        startDate = findViewById(R.id.goal_start_date_set_textview);
        endDate = findViewById(R.id.goal_end_date_set_textview);
        setDate.setOnClickListener(new setGoalDate());
        createGoal = findViewById(R.id.create_goal_btn);
        setGoalTargetHours = findViewById(R.id.goal_set_target_hours_textview);

//        目标分级和引导
//        goalDifficultyGroup = findViewById(R.id.goal_difficulty_radiogroup);
//        goalDifficultyGroup.setOnCheckedChangeListener(new setGoalDifficulty());
        goalUrgencyGroup = findViewById(R.id.goal_urgency_radiogroup);
        goalUrgencyGroup.setOnCheckedChangeListener(new setGoalUrgency());

        createGoal.setOnClickListener(new registerGoal());



    }
}
