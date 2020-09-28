package com.example.newoneday;

import android.os.AsyncTask;
import android.util.Log;

import com.example.newoneday.bomb.data.BmobGoal;
import com.example.newoneday.bomb.data.MyUser;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by yongg on 2019/2/25.
 */

public class UpdateMainActivityUI extends AsyncTask<Void, Integer, Boolean>{
    @Override
    protected void onPreExecute(){

    }

    @Override
    protected Boolean doInBackground(Void... params){
        try{
            MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
            if(currentUser != null){
                BmobQuery<BmobGoal> goalQuery = new BmobQuery<>();
                goalQuery.addWhereEqualTo("relatedUser", currentUser);
                //      关联表要带的指针项要Include进去
                goalQuery.include("relatedUser");

                goalQuery.findObjects(new FindListener<BmobGoal>() {  //按行查询，查到的数据放到List<BmobPerson>的集合
                    @Override
                    public void done(List<BmobGoal> list, BmobException e) {
//                    goalList = list;
                        if (e == null) {
//                            for (BmobGoal bmobGoal: list){
//                                MainActivity.spinnerData.add(bmobGoal.getGoalName());
//                            }
//                            MainActivity.niceSpinner.attachDataSource(MainActivity.spinnerData);
                        } else {
                            Log.d("UpdateMainActivityUI", "查詢失敗");
                        }
                    }

                });

            }else{
                Log.d("UpdateMainActivityUI", "Please login first");
            }
        }catch (Exception e){
            return false;
        }
        return true;
    }
    @Override
    protected void onProgressUpdate(Integer... values){
//        MainActivity.niceSpinner.setOnItemSelectedListener(new MainActivity.setNiceSpinner());
//        MainActivity.niceSpinner.setBackgroundResource(R.drawable.nicespinner_textview_round_border);

    }

}
