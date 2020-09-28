
package com.example.newoneday.litepal;

//import org.litepal.crud.DataSupport;

import com.example.newoneday.friend.data.Friend;

import org.litepal.crud.LitePalSupport;


import java.util.ArrayList;
import java.util.List;

public class UserInfo  extends LitePalSupport{


    private String nikeName;

    private String userId;

    private String emailAddress;
    private String telePhoneNumber;
    private String password;


    //    添加与GoalInfo表的关联
    private List<GoalInfo> goalList = new ArrayList<GoalInfo>();

    public List<GoalInfo> getGoalList() {
        return goalList;
    }

    public void setGoalList(List<GoalInfo> goalList) {
        this.goalList = goalList;
    }


    //    添加与Friend表的关联
    private List<Friend> friendList = new ArrayList<>();


    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }



    public String getNikeName() {
        return nikeName;
    }

    public String getPassword() {
        return password;
    }

    public String getTelePhoneNumber() {
        return telePhoneNumber;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTelePhoneNumber(String telePhoneNumber) {
        this.telePhoneNumber = telePhoneNumber;
    }
}
