package com.example.newoneday.goal.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.newoneday.R;
import com.example.newoneday.bomb.data.BmobGoal;
import com.example.newoneday.bomb.data.MyUser;
import com.example.newoneday.goal.adapter.GoalAdapter;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class GoalManagementActivity extends AppCompatActivity {

    private List<BmobGoal> goalList = new ArrayList<BmobGoal>();
    public static List<String> goalName = new ArrayList<>();
    public static List<Integer> goalId =  new ArrayList<>();
    private GoalAdapter goalAdapter;
    private ListView currentGoalsListview;
    private ImageView addNewGoal;
    public static BmobGoal currentGoal;

    public void initCurrentGoalList(){

       MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
       if(currentUser != null){
           BmobQuery<BmobGoal> goalQuery = new BmobQuery<>();
           goalQuery.addWhereEqualTo("relatedUser", currentUser);
           //      关联表要带的指针项要Include进去
           goalQuery.include("relatedUser");

           goalQuery.findObjects(new FindListener<BmobGoal>() {  //按行查询，查到的数据放到List<BmobPerson>的集合
               @Override

               public void done(List<BmobGoal> list, BmobException e) {

                   if (e == null) {

                       for(BmobGoal bmobGoal: list){
                           goalList.add(bmobGoal);
                           Log.d("GoalManagementActivity","查詢数据成功，返回objectId为："+bmobGoal.getGoalName());
                       }

                       goalAdapter = new GoalAdapter(GoalManagementActivity.this, R.layout.listview_goal_managment_goal_item, goalList);

                       currentGoalsListview.setAdapter(goalAdapter);

                   } else {
                       Toast.makeText(GoalManagementActivity.this, "查詢失敗", Toast.LENGTH_SHORT).show();
                   }
               }

           });
       }else{
           Toast.makeText(GoalManagementActivity.this, "Please login first", Toast.LENGTH_SHORT).show();
       }


    }

    class openCrateGoalActivity implements View.OnClickListener{
        @Override
        public void onClick(View v){
            Intent intent = new Intent(GoalManagementActivity.this, CreateGoalActivity.class);
            startActivity(intent);
        }
    };

    class openGoalInfoActivity implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            currentGoal = goalList.get(position);
            if(currentGoal.isUrgent()){
                Intent intent = new Intent(GoalManagementActivity.this, UrgentGoalInfoActivity.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(GoalManagementActivity.this, NotUrgentGoalInfoActivity.class);
                startActivity(intent);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_management);
        currentGoalsListview = (ListView) findViewById(R.id.current_goals_listview);
        initCurrentGoalList();
        addNewGoal = findViewById(R.id.add_goal_imageview);
        addNewGoal.setOnClickListener(new openCrateGoalActivity());
        currentGoalsListview.setOnItemClickListener(new openGoalInfoActivity());
    }
}
