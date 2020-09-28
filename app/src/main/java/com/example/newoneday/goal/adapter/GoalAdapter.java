package com.example.newoneday.goal.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newoneday.R;
import com.example.newoneday.bomb.data.BmobGoal;
import com.example.newoneday.bomb.model.Constant;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import   java.text.SimpleDateFormat;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by yongg on 2019/1/19.
 */

public class GoalAdapter extends ArrayAdapter<BmobGoal> {
    private int resourceId;
    public TextView goalName;
    public ImageView goalStatus;
    public int goalStatusRetureValue;
    public static String goalId;
//    public static Integer keyVaule;

//    static public List<BmobGoal> goaltoMain = new ArrayList<BmobGoal>();

    public GoalAdapter(Context context, int textViewResourceId, List<BmobGoal> objects){
        super(context,textViewResourceId,objects);
        resourceId = textViewResourceId;
//        goaltoMain = objects;
    }

    public static int getTimeCompareSize(String targetTime, String actualTime){
        int i=0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");;//年-月-日 时-分
        try {
            Date date1 = dateFormat.parse(targetTime);//开始时间
            Date date2 = dateFormat.parse(actualTime);//结束时间
            // 1 目标日期小于当前日期 2 目标日期等于当前日期 3 目标日期大于当前日期
            if (date1.getTime()<date2.getTime()){
                i= 1;
            }else if (date1.getTime()==date2.getTime()){
                i= 2;
            }else if (date1.getTime()>date2.getTime()){
                //正常情况下的逻辑操作.
                i= 3;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return  i;
    }


    public View getView(int position, View convertView, ViewGroup parent){
        BmobGoal goal = getItem(position);
        goalId  = goal.getObjectId();
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent,false);

        goalName = view.findViewById(R.id.show_goal_name_textview);
        goalName.setText(goal.getGoalName());

//    获取当前时间
        SimpleDateFormat   actualTimeFormat   =   new   SimpleDateFormat   ("yyyy.MM.dd");
        Date curDate =  new Date(System.currentTimeMillis());
        String   actualTime   =   actualTimeFormat.format(curDate);
        Log.d("GoalAdapter",actualTime);

//        比较
        goalStatus = view.findViewById(R.id.show_goal_status_imageview);
        String targetTime = goal.getGoalEndDate();
        int compareDateResult = getTimeCompareSize(targetTime,actualTime);

        switch (compareDateResult){
            case 3:
                goalStatus.setImageResource(R.drawable.doing);
                goal.setGoalStatus(Constant.GOAL_STATUS_DOING);
                goalStatusRetureValue = 3;
                break;
            default:
                if(goal.getLastingTime() >= goal.getTargetHours()){
                    goalStatus.setImageResource(R.drawable.ok);
                    goal.setGoalStatus(Constant.GOAL_STATUS_FINISHED);
                    goalStatusRetureValue = 1;
                }else {
                    goalStatus.setImageResource(R.drawable.overdue);
                    goal.setGoalStatus(Constant.GOAL_STATUS_OVERDUE);
                    goalStatusRetureValue = 2;
                }
                break;
        }


        goal.setGoalStatus(goalStatusRetureValue);
        String goalId = goal.getObjectId();
//        绝了 实测不判断更快
       /* 错误原因 不是按compareResult更新 */
//        if(goal.getGoalStatus() == 0 || (compareDateResult != goal.getGoalStatus())){
        goal.update(goalId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e  == null){
                    Log.d("GoalAdapter",  "Update Successfully");
                }else{
                    Log.d("GoalAdapter",  "Update failed");
                }
            }
        });
//        }
        return view;
    }
}
