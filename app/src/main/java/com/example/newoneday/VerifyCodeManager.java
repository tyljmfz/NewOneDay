
package com.example.newoneday;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.newoneday.utils.ToastUtils;
import com.mob.MobSDK;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import cn.smssdk.SMSSDK;
import cn.smssdk.EventHandler;
import cn.smssdk.gui.RegisterPage;


public class VerifyCodeManager extends Activity {

    public final static int REGISTER = 1;
    public final static int RESET_PWD = 2;
    public final static int BIND_PHONE = 3;

    private Context mContext;
    private int recLen = 60;
    private Timer timer = new Timer();
    private Handler mHandler = new Handler();
    private String phone;

    private EditText phoneEdit;
    private Button getVerifiCodeButton;

    public VerifyCodeManager(Context context, EditText editText, Button btn) {
        this.mContext = context;
        this.phoneEdit = editText;
        this.getVerifiCodeButton = btn;
    }

    public void getVerifyCode(int type) {
        // 获取验证码之前先判断手机号

        phone = phoneEdit.getText().toString().trim();

        if (TextUtils.isEmpty(phone)) {
            ToastUtils.showShort(mContext, R.string.log_in_prompt_phone_number_english);
            return;
        }
//        else if (phone.length() < 11) {
//            ToastUtils.showShort(mContext, R.string.error_invalid_phone);
//            return;
//        } else if (!RegexUtils.checkMobile(phone)) {
//            ToastUtils.showShort(mContext, R.string.error_invalid_phone);
//            return;
//        }


        // 两种方式：1.集成第三方SDK，调用skd的方法获取验证码

//        SMSSDK.getVerificationCode("86", phone);

        // 2. 请求服务端，由服务端为客户端发送验证码
//		HttpRequestHelper.getInstance().getVerifyCode(mContext, phone, type,
//                getVerifyCodeHandler);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        setButtonStatusOff();
                        if (recLen < 1) {
                            setButtonStatusOn();
                        }
                    }
                });
            }
        };

        timer = new Timer();
        timer.schedule(task, 0, 1000);

        MobSDK.init(this, "26e27da8f7ea1", "51497494b01e330fd5ce8467ac0459a9");
//          修改国家代码
        SMSSDK.getVerificationCode("44", phone);

    }


    public void sendCode(Context context) {
        RegisterPage page = new RegisterPage();
        //如果使用我们的ui，没有申请模板编号的情况下需传null
        page.setTempCode(null);
        page.setRegisterCallback(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 处理成功的结果
                    HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
                    String country = (String) phoneMap.get("country"); // 国家代码，如“86”
                    String phone = (String) phoneMap.get("phone"); // 手机号码，如“13800138000”
                    // TODO 利用国家代码和手机号码进行后续的操作
                } else{
                    // TODO 处理错误的结果
                }
            }
        });
        page.show(context);
    }

    private void setButtonStatusOff() {
//        getVerifiCodeButton.setText(String.format(
//                mContext.getResources().getString(R.string.count_down), recLen--));
        getVerifiCodeButton.setClickable(false);
        getVerifiCodeButton.setTextColor(Color.parseColor("#f3f4f8"));
        getVerifiCodeButton.setBackgroundColor(Color.parseColor("#b1b1b3"));
    }

    private void setButtonStatusOn() {
        timer.cancel();
        getVerifiCodeButton.setText("重新发送");
        getVerifiCodeButton.setTextColor(Color.parseColor("#b1b1b3"));
        getVerifiCodeButton.setBackgroundColor(Color.parseColor("#f3f4f8"));
        recLen = 60;
        getVerifiCodeButton.setClickable(true);
    }

}