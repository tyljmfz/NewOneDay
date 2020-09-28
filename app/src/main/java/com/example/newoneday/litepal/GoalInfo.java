package com.example.newoneday.litepal;

import com.example.newoneday.litepal.UserInfo;

import org.litepal.crud.LitePalSupport;

/**
 * Created by yongg on 2019/1/18.
 */

public class GoalInfo extends LitePalSupport {

    private int goalId;
    private String goalName;
    private String goalStartDate;
    private String goalEndDate;
    private long lastingTime;

    //    添加与GoalInfo表的关联
    private UserInfo userInfos;

    public UserInfo getUserInfos() {
        return userInfos;
    }

    public void setUserInfos(UserInfo userInfos) {
        this.userInfos = userInfos;
    }

    public int getGoalId() {
        return goalId;
    }

    public void setGoalId(int goalId) {
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
