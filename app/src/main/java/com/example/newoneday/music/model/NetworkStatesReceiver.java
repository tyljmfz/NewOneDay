package com.example.newoneday.music.model;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;

import static android.content.ContentValues.TAG;

public class NetworkStatesReceiver extends BroadcastReceiver implements Serializable{

//    public AlertDialog.Builder builder;
    private ImageView imageView;
    private NetworkInfo monitorChange = null;
    public static boolean canPlay = false;
    public  boolean ifAppear = true;
    public  boolean tag = true;
    public static boolean ifSelectedCancel =  false;

//    public static void setValueOfDialog(String title, String message, boolean ifAppear){
//
//        MainActivity.builder.setIcon(R.mipmap.dialog_icon);
//        MainActivity.builder.setTitle(title);
//        MainActivity.builder.setMessage(message);
//        MainActivity.builder.setView(R.layout.custom_dialog);
//
//
//        //监听下方button点击事件
//        if(ifAppear){
//            MainActivity.builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    canPlay = true;
//                    ifSelectedCancel = false;
//                }
//            });
//
//            MainActivity.builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    canPlay = false;
//                    ifSelectedCancel = true;
//                }
//            });
//
//            MainActivity.builder.setCancelable(true);
//            AlertDialog dialog=MainActivity.builder.create();
//            dialog.show();
//        }
//    }

    public boolean setifAppear(){
        if(canPlay && !ifSelectedCancel){
            ifAppear = false;
        }
        return ifAppear;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(TAG,"网络状态发生变化");
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP){
//            获得connectivityManager对象
            ConnectivityManager connectivityManager =(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            获取CM对象对应的NetworkInfo对象
//            获取wifi连接信息
            if(connectivityManager != null){
                NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            4G
                NetworkInfo dataNetworkInfo = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE);

                if(wifiNetworkInfo != null && dataNetworkInfo != null){
                    if(wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()){
//                        setValueOfDialog("网络连接情况","Wifi已连接，移动网络已连接",setifAppear());
                        Toast.makeText(context,"Wifi已连接，移动网络已连接",Toast.LENGTH_SHORT).show();
                    }else if(wifiNetworkInfo.isConnected() && !dataNetworkInfo.isConnected()){
//                        setValueOfDialog("网络连接情况","Wifi已连接，移动网络未连接",setifAppear());
                        Toast.makeText(context,"Wifi已连接，移动数据未连接",Toast.LENGTH_SHORT).show();
                    }else if(!wifiNetworkInfo.isConnected() && dataNetworkInfo.isConnected()){
//                        setValueOfDialog("网络连接情况","Wifi未连接，移动网络已连接",setifAppear());
                        Toast.makeText(context,"Wifi未连接，移动数据已连接",Toast.LENGTH_SHORT).show();
                    }else {
//                        setValueOfDialog("网络连接情况","Wifi未连接，移动网络未连接",setifAppear());
                        Toast.makeText(context,"Wifi未连接，移动数据未连接",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }else {
            Log.d(TAG, "网络状态发生变化 && API level > 23");

            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo.State wifiState = null;
            NetworkInfo.State mobileState = null;

            wifiState = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            mobileState = connectivityManager.getNetworkInfo(connectivityManager.TYPE_MOBILE).getState();

            if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED == mobileState) {

                // 手机网络连接成功
//                这个地方要做弹窗提示 是否用移动数据继续播放
                if (canPlay == false) {
                    Toast.makeText(context,"当前无wifi连接，是否使用移动数据继续播放？",Toast.LENGTH_SHORT).show();
//                    setValueOfDialog("网络提示", "当前无wifi连接，是否使用移动数据继续播放？", setifAppear());
                }
                Toast.makeText(context, "移动数据网络连接成功", Toast.LENGTH_SHORT).show();

            } else if (wifiState != null && NetworkInfo.State.CONNECTED == wifiState) {

                // 无线网络连接成功
//                Toast.makeText(context, "无线网络连接成功", Toast.LENGTH_SHORT).show();

            } else if (wifiState != null && mobileState != null
                    && NetworkInfo.State.CONNECTED != wifiState
                    && NetworkInfo.State.CONNECTED != mobileState) {
//              无网络连接
                Toast.makeText(context, "手机没有任何的网络", Toast.LENGTH_SHORT).show();

            }
        }
    }

}
