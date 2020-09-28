package com.example.newoneday.bomb.data;

import cn.bmob.v3.BmobObject;

/**
 * Created by yongg on 2019/2/1.
 */

public class BmobGoal extends BmobObject {

    private String goalId;
    private String goalName;
    private String goalStartDate;
    private String goalEndDate;
    private long lastingTime;
//    注注注：这里的targetHours本质上是mintues 为了作统一时间单位处理 在lastingHours数据里记录的而是分钟数
    private long targetHours;
    private boolean isUrgent;
    private MyUser relatedUser;
    private String reasonTodo;      //这个reasonTodo包含了两种可能 worry和reason  统一存储到这一栏
    private int goalStatus;
    /*
    * goalStatus = 1   Finished
    * goalStatus = 2   OverDue
    * goalStatus = 3   Doing
    * */

    //    添加与MyUser表的关联
    public MyUser getRelatedUser() {
        return relatedUser;
    }

    public void setRelatedUser(MyUser relatedUser) {
        this.relatedUser = relatedUser;
    }

    public boolean isUrgent() {
        return isUrgent;
    }

    public void setUrgent(boolean urgent) {
        isUrgent = urgent;
    }

    public String getReasonTodo() {
        return reasonTodo;
    }

    public void setReasonTodo(String reasonTodo) {
        this.reasonTodo = reasonTodo;
    }

    public int getGoalStatus() {
        return goalStatus;
    }

    public void setGoalStatus(int goalStatus) {
        this.goalStatus = goalStatus;
    }

    public long getTargetHours() {
        return targetHours;
    }

    public void setTargetHours(long targetHours) {
        this.targetHours = targetHours;
    }


    public String getGoalId() {
        return goalId;
    }

    public void setGoalId(String goalId) {
        this.goalId = goalId;
    }

    public String getGoalName() {
        return goalName;
    }

    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }

    public String getGoalStartDate() {
        return goalStartDate;
    }

    public void setGoalStartDate(String goalStartDate) {
        this.goalStartDate = goalStartDate;
    }

    public String getGoalEndDate() {
        return goalEndDate;
    }

    public void setGoalEndDate(String goalEndDate) {
        this.goalEndDate = goalEndDate;
    }

    public long getLastingTime() {
        return lastingTime;
    }

    public void setLastingTime(long lastingTime) {
        this.lastingTime = lastingTime;
    }

}
