package com.example.newoneday;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.danikula.videocache.HttpProxyCacheServer;
import com.example.newoneday.achievement.AchievementPictureUploadActivity;
import com.example.newoneday.bomb.data.BmobGoal;
import com.example.newoneday.bomb.data.MyUser;
import com.example.newoneday.bomb.model.DemoMessageHandler;
import com.example.newoneday.bomb.model.UserModel;
import com.example.newoneday.friend.activity.FriendListActivity;
import com.example.newoneday.friend.activity.NewFriendListActivity;
import com.example.newoneday.friend.activity.SearchFriendActivity;
import com.example.newoneday.goal.activity.CreateGoalActivity;
import com.example.newoneday.goal.activity.GoalManagementActivity;
import com.example.newoneday.music.activity.PlayListActivity;
import com.example.newoneday.music.adapter.SongAdapter;
import com.example.newoneday.qrcode.CreateQRCodeActivity;

import org.angmarch.views.NiceSpinner;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

import static com.example.newoneday.bomb.model.BmobIMApplication.getMyProcessName;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TimePickerDialog.OnTimeSetListener{
    private ImageView startTask;
    public Phonograph phonograph;
    private NavigationView navigationView;
//    private NavSelectActivity navSelectActivity;


//    下面是日期时间选择器调试部分
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private int hourOfDay;
    private int minuteOfDay;

//    下面是倒计时调试部分
    private TextView countDownTime;
    private long SysTime;
    private boolean first=true;
    private long m = 4524752;
    private long millTime = 0;
    private long totalSetTime = 0;
    private boolean isStop = false;

//      下面是nice_Spinner 目标选择框部分
    public NiceSpinner niceSpinner;
    public BmobGoal currentSelectedGoal;
    static public String currentGoalName;
    public long currentLastingTime;
    static public List<String> spinnerData = new ArrayList<>();
    public MyUser currentUser = BmobUser.getCurrentUser(MyUser.class);
    public List<BmobGoal> currentGoalList = new ArrayList<>();
    public String currentGoalId;
    public int niceSpinnerPosition;


//    对话框
    public AlertDialog.Builder builder;
    public Dialog pictureDialog;

//    Bmob好友調試
    private Context context = MainActivity.this;

//    播放器
    public MediaPlayer mediaPlayer = new MediaPlayer();
    public HttpProxyCacheServer proxy;
    public String proxyUrl;
    public HashMap<Integer,String> AudioUrlList = new HashMap<>();


    public void searchSpinnerData(){
        currentUser = BmobUser.getCurrentUser(MyUser.class);
        if(currentUser != null){
//            做复合查询 只显示doing的任务
            BmobQuery<BmobGoal> goalQuery1 = new BmobQuery<>();
            goalQuery1.addWhereEqualTo("relatedUser", currentUser);
//            goalQuery1.include("relatedUser");

            BmobQuery<BmobGoal> goalQuery2 = new BmobQuery<>();
            goalQuery2.addWhereEqualTo("goalStatus", 3);
//            goalQuery2.include("relatedUser");
            //      关联表要带的指针项要Include进去
            List<BmobQuery<BmobGoal>> queries = new ArrayList<>();
            queries.add(goalQuery1);
            queries.add(goalQuery2);

            BmobQuery<BmobGoal> query = new BmobQuery<>();
            query.and(queries);
            query.include("relatedUser");
            query.include("goalStatus");
            query.findObjects(new FindListener<BmobGoal>() {  //按行查询，查到的数据放到List<BmobPerson>的集合
                @Override
                public void done(List<BmobGoal> list, BmobException e) {
                    if (e == null) {
                        Message message = spinnerhandler.obtainMessage();
                        message.what = 0;
                        message.obj = list;
                        spinnerhandler.sendMessage(message);
                        Log.d("MainActivity","查詢数据成功，返回objectId为：");
                    } else {
                        Log.d("MainActivity", "查詢失敗"+e.getMessage());
                        searchSpinnerData();
                    }
                }

            });

        }else{
            Toast.makeText(MainActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
        }
    }

    private Handler spinnerhandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    currentGoalList = (List<BmobGoal>) msg.obj;
                    if(spinnerData.size() < currentGoalList.size()){
                        for (BmobGoal goal : currentGoalList) {
                            spinnerData.add(goal.getGoalName());
                        }
                    }
                    if(currentGoalList.size() > 0){
                        niceSpinner.attachDataSource(spinnerData);
                        niceSpinner.setOnItemSelectedListener(new setNiceSpinner());
                        niceSpinner.setBackgroundResource(R.drawable.nicespinner_textview_round_border);
                    }
                    break;
            }
        }

    };

    //    Nice Spinner
    public class setNiceSpinner implements AdapterView.OnItemSelectedListener{
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            currentGoalName = spinnerData.get(position);
            niceSpinnerPosition = position;
            Log.d("MainActivity",niceSpinnerPosition+"");
            for (BmobGoal goal : currentGoalList){
                if (currentGoalName == goal.getGoalName()){
                    currentSelectedGoal = goal;
                    currentGoalId = goal.getObjectId();
//
                }
            }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
//            Toast.makeText(MainActivity.this, "Please select the goal you want to do", Toast.LENGTH_SHORT).show();
        }
    };

//时间选择
    private void showTime() {
        hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        minuteOfDay = calendar.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String desc=String.format("%02d:%02d", hourOfDay,minute);
                countDownTime.setText(desc);
                millTime =  (60*1000)*minute + (60*60*1000)*hourOfDay;
                totalSetTime = millTime;
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
        timePickerDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        String desc=String.format("您选择的时间是%d时%d分",hour,minute);
        countDownTime.setText(desc);
    }


//点击开始和停止按钮
    public void startTask(){

        if(millTime != 0){
            if(niceSpinnerPosition != 0){
                phonograph.setPlaying(false);

//        设置倒计时开始效果
                SysTime=System.currentTimeMillis();
                handler.postDelayed(runnable, 0);
                first=true;

                startTask.setImageResource(R.drawable.stop);

            }else{
                Toast.makeText(this, "请先选择一个目标", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "请先设定您想要进行的时间", Toast.LENGTH_SHORT).show();
        }

       
    }
    public void stopTask(){
        phonograph.setPlaying(true);

//        倒计时效果处理

        handler.removeCallbacks(runnable);
        long t=System.currentTimeMillis()-SysTime;
        m=m-t;

//        对话框
        CountDownDialogStopReminder("警告","确定要放弃正在进行的任务吗？");

    }
    public void onClick(View v){
        if(startTask.getDrawable().getConstantState().equals(getResources().getDrawable(R.drawable.start).getConstantState())){
            startTask();
        }else{
            stopTask();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }


//   为了调试暂时关掉的nav

    class toSelectedNav implements NavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();
            switch (id){
                case R.id.nav_one:
                    Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.nav_two:
                    Intent intent2 = new Intent(MainActivity.this, SignInActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.nav_three:
                    Intent intent3 = new Intent(MainActivity.this, CreateGoalActivity.class);
                    startActivity(intent3);
                case R.id.nav_four:
                    Intent intent4 = new Intent(MainActivity.this, GoalManagementActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.nav_five:
                    Intent intent5 = new Intent(MainActivity.this, SearchFriendActivity.class);
                    startActivity(intent5);
                    break;
                case R.id.nav_six:
                    Intent intent6 = new Intent(MainActivity.this, NewFriendListActivity.class);
                    startActivity(intent6);
                    break;
                case R.id.nav_seven:
                    Intent intent7 = new Intent(MainActivity.this, FriendListActivity.class);
                    startActivity(intent7);
                    break;
                case R.id.nav_eight:
                    Intent intent8 = new Intent(MainActivity.this, AchievementPictureUploadActivity.class);
                    startActivity(intent8);
                    break;
//                case R.id.nav_qrcode:
//                    Intent intent10 = new Intent(MainActivity.this, CreateQRCodeActivity.class);
//                    startActivity(intent10);
//                    break;
                case R.id.nav_log_out:
                    UserModel.getInstance().logout();
                    //TODO 连接：3.2、退出登录需要断开与IM服务器的连接
                    BmobIM.getInstance().disConnect();
                    BmobUser.logOut();
                    BmobUser currentUser = BmobUser.getCurrentUser(MyUser.class); // 现在的currentUser是null了
                    Toast.makeText(MainActivity.this, "Log Out Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent9 = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(intent9);
//                    searchSpinnerData();
                    break;

                default:
            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    /**
     * CountDownTimer 实现倒计时
     */

    public void start(View view) {
        SysTime=System.currentTimeMillis();
        handler.postDelayed(runnable, 0);
        first=true;
    }

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
                long t = System.currentTimeMillis() - SysTime;
                setTime(millTime - t);

                if (first) {
                    first = false;
                }
                handler.postDelayed(this, 1000);
//                倒计时停止 成功完成任务 应该进行更新
                if (millTime - t <= 50) {
                    startTask.setImageResource(R.drawable.start);
                    phonograph.setPlaying(true);
                    pictureDialog.show();
//                    phonograph.setColorPicture(Phonograph.currentPicutre);
                    long previousTime = currentSelectedGoal.getLastingTime();   // 统一存储为分钟数
                    long totalMintues = totalSetTime / (60*1000);
                    currentSelectedGoal.setLastingTime(previousTime + totalMintues);
                    currentSelectedGoal.update(currentGoalId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e  == null){
                                Toast.makeText(MainActivity.this, "Update Successfully", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(MainActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    Log.d("MainActivity", "user lastingtime " + currentSelectedGoal.getLastingTime());
                    handler.removeCallbacks(runnable);
                }
        }
    };


//  点击倒计时数字进入时间选择界面
    class toTimeChoose implements View.OnClickListener{
        @Override
        public void onClick(View v){
            showTime();
        }
    };

    private void setTime(long temp){
        if(temp<=0){
            handler.removeCallbacks(runnable);
            temp=0;
        }

        long time = temp / 1000;
        long hours = temp / (1000 * 60 * 60);
        long minutes = (temp-hours*(1000 * 60 * 60 ))/(1000* 60);

        countDownTime.setText(String.format("%02d:%02d:%02d", hours, minutes, time % 60));
    }


//    对话框

    public void CountDownDialogStopReminder(String title, String message){

        builder.setIcon(R.drawable.angela);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setView(R.layout.custom_dialog);

        //监听下方button点击事件

//        退出此次任务
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                handler.removeCallbacks(runnable);
                m=0;
                setTime(m);
                startTask.setImageResource(R.drawable.start);
                long currentTime = currentSelectedGoal.getLastingTime();
                currentTime = currentTime - totalSetTime;
                currentSelectedGoal.setLastingTime(currentTime);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                SysTime=System.currentTimeMillis();
                handler.postDelayed(runnable, 0);
                first=false;
            }
        });

        builder.setCancelable(true);
        AlertDialog dialog=builder.create();
        dialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.toolbar_music_player:
                Intent intent = new Intent(MainActivity.this, PlayListActivity.class);
                startActivity(intent);
                break;
            default:
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        phonograph = findViewById(R.id.phonograph_view);
        startTask = findViewById(R.id.task_start);
        startTask.setOnClickListener(MainActivity.this);

//        倒计时调试专场
        countDownTime = (TextView) findViewById(R.id.countdown_time);
        setTime(0);
        countDownTime.setOnClickListener(new toTimeChoose());

//        日期调试专场
        calendar = Calendar.getInstance();


//        现在为了调试方便暂时去掉导航栏效果
        navigationView = findViewById(R.id.nav_view);
        navigationView.setCheckedItem(R.id.nav_one);
        navigationView.setNavigationItemSelectedListener(new toSelectedNav());

//        对话框
        builder=new AlertDialog.Builder(this);
        pictureDialog = new Dialog(context, R.style.edit_PictureDialog_style);
        pictureDialog.setContentView(R.layout.activity_main_picture_dialog);
        ImageView pictureImageView = pictureDialog.findViewById(R.id.picture_imageview_activity_main_picture_dialog);
        Resources r = MainActivity.this.getResources();
//        这一块有问题 是设置随机值传不过来吗
//        Bitmap currentPic = BitmapFactory.decodeResource(r, pictureId);
//        pictureImageView.setImageBitmap(currentPic);
        pictureImageView.setBackgroundResource(Phonograph.currentPicutre);
        pictureDialog.setCanceledOnTouchOutside(true);
        Window window = pictureDialog.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.x = 0; layoutParams.y = 40;
        pictureDialog.onWindowAttributesChanged(layoutParams);
        pictureImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictureDialog.dismiss();
            }
        });

//        Bomb服务器
        Bmob.initialize(this, "86cb620aacc23833fb1d5cfe6d4c20a9");

        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }

//        NiceSpinner调试专场

        niceSpinner = findViewById(R.id.goal_choose_nice_spinner);
//        searchData();
        searchSpinnerData();

    }



}
