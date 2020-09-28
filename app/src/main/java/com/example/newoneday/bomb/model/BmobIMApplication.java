package com.example.newoneday.bomb.model;


import android.app.Application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

/**
 * Created by yongg on 2019/2/6.
 */

public class BmobIMApplication extends Application {

    final static String MY_TAG = "LoggerOut";
    private static BmobIMApplication INSTANCE;
    public static BmobIMApplication INSTANCE() {
        return INSTANCE;
    }

    private void setInstance(BmobIMApplication app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(BmobIMApplication a) {
        BmobIMApplication.INSTANCE = a;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        //NewIM初始化
        Bmob.initialize(this, "86cb620aacc23833fb1d5cfe6d4c20a9");
//        if (getApplicationInfo().packageName.equals(getMyProcessName())){
//            //im初始化
//            BmobIM.init(this);
//            //注册消息接收器
//            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
//        }
        BmobIM.init(this);
//        //注册消息接收器
        BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));

    }

    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
