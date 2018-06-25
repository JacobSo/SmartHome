package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.CountDownTimer;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.lsapp.smarthome.BuildConfig;
import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.app.SignWay;
import com.lsapp.smarthome.databinding.ActivityLoginBinding;
import com.lsapp.smarthome.event.ThirdPartyLoginEvent;
import com.lsapp.smarthome.ui.model.LoginModel;
import com.zuni.library.ui.CircularAnim;
import com.zuni.library.utils.zPhoneUtil;
import com.zuni.library.utils.zToastUtil;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import rx.Subscription;

/**
 * Created by Administrator on 2016/8/8.
 */
public class LoginActivity extends BaseActivity {
    private ActivityLoginBinding binding;
    private LoginModel model;
    private CountDownTimer timer = null;
    private Subscription subscription;
    private final static String TAG = "LoginActivity";
    private long[] hits = new long[10];
    private boolean isDebugLogin = false;
    private Handler smsHandler = new Handler(){
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            zToastUtil.showSnack(binding.getRoot(),"验证错误");
            loginFail();
        }
    };
    EventHandler eh;
    @Override
    protected void initModel() {
        model = new LoginModel(this);

    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        SMSSDK.unregisterAllEventHandler();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setDebug(BuildConfig.LOG_DEBUG);
        if (BuildConfig.LOG_DEBUG) {
            binding.loginAccount.setInputType(InputType.TYPE_CLASS_TEXT);
        } else binding.loginAccount.setText(null);
        if (getSupportActionBar() != null)
            getSupportActionBar().hide();
        initSubscription();


       // SMSSDK.initSDK(this, "159ec0aa36c74", "2cbf0388f2a895ba60720f456f61a271");
         eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交验证码成功
                        model.login(binding.loginAccount.getText().toString(), SignWay.SMS.ordinal(), null,null);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    smsHandler.sendEmptyMessage(0);
                    ((Throwable) data).printStackTrace();
                }
            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调

    }

    private void initSubscription() {
        subscription = RxBus.getDefault().toObserverable(ThirdPartyLoginEvent.class).subscribe(thirdPartyLoginEvent -> {
            if (!thirdPartyLoginEvent.isUnbind()) {
                model.login(thirdPartyLoginEvent.getId(), thirdPartyLoginEvent.getPlatform(), thirdPartyLoginEvent.getName(),  null);
            }
        });
    }

    public void onSendCode(View v) {
        String account = binding.loginAccount.getText().toString();
        if (TextUtils.isEmpty(account) || !zPhoneUtil.isMobile(account)) {
            binding.loginAccount.setError(getString(R.string.phone_failed));
            return;
        }
        binding.loginCodeBtn.setEnabled(false);
        beginTimer();
        // model.sms(account);
        SMSSDK.getVerificationCode("86", account);
    }

    public void loginFail() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_center);
        binding.submit.startAnimation(animation);
        binding.submit.setVisibility(View.VISIBLE);
    }

    public void onLogin(View v) {
    //    model.login(binding.loginAccount.getText().toString(), SignWay.SMS.ordinal(), "",  null);//test
        //  model.login(binding.loginAccount.getText().toString(), SignWay.SMS.ordinal(),"");
        String code = binding.loginCode.getText().toString();
        String pwd = binding.loginPwd.getText().toString();
        if (binding.loginPwd.getVisibility() == View.VISIBLE) {
            if (TextUtils.isEmpty(pwd) || TextUtils.isEmpty(binding.loginAccount.getText().toString())) {
                binding.loginPwd.setError(getString(R.string.info_null_toast));
                binding.loginAccount.setError(getString(R.string.info_null_toast));
                return;
            }
        } else {
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(binding.loginAccount.getText().toString())) {
                binding.loginCode.setError(getString(R.string.phone_code_null));
                binding.loginAccount.setError(getString(R.string.phone_code_null));
                return;
            }
        }
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fade_out_center);
        binding.submit.startAnimation(animation);
        binding.submit.setVisibility(View.GONE);
        //  model.login(binding.loginAccount.getText().toString(), SignWay.SMS.ordinal(), "",code,pwd);
        if(binding.loginPwd.getVisibility() == View.VISIBLE ){
            model.login(binding.loginAccount.getText().toString(), SignWay.SMS.ordinal(), null,pwd);
        }else SMSSDK.submitVerificationCode("86", binding.loginAccount.getText().toString(), code);
    }

    public void onSwitchLogin(View v) {
        binding.loginCodeLayout.setVisibility(binding.loginPwd.getVisibility() == View.VISIBLE ? View.VISIBLE : View.GONE);
        binding.loginPwd.setVisibility(binding.loginPwd.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
        Button btn = (Button) v;
        btn.setText(getString(binding.loginPwd.getVisibility() == View.VISIBLE ? R.string.sms_login : R.string.pwd_login));
    }

    public void onWechat(View v) {
       model.shareLogin(0);
    }

    public void onSina(View v) {
        model.shareLogin(1);

    }

    public void onTaobao(View v) {
        model.shareLogin(2);
    }

    public void testLogin(View v) {
        if (isDebugLogin || BuildConfig.LOG_DEBUG) {
            model.login(binding.loginAccount.getText().toString(), SignWay.SMS.ordinal(), "",  null);
        } else {
            System.arraycopy(hits, 1, hits, 0, hits.length - 1);
            hits[hits.length - 1] = SystemClock.uptimeMillis();
            if (hits[0] >= (hits[hits.length - 1] - 1000)) {
                zToastUtil.showSnackWithButton(binding.getRoot(), "是否进入开发者模式？", getString(R.string.confirm), view -> {
                    isDebugLogin = true;
                    binding.loginAccount.setInputType(InputType.TYPE_CLASS_TEXT);
                });
            }
        }
    }

    private void beginTimer() {
        timer = new CountDownTimer(120000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.loginCodeBtn.setText(getString(R.string.been_send) + millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                binding.loginCodeBtn.setText(getString(R.string.get_code));
                binding.loginCodeBtn.setEnabled(true);
                if (timer != null)
                    timer.cancel();
            }

        }.start();

    }

    public void setView() {
        CircularAnim.fullActivity(LoginActivity.this, binding.getRoot())
                .colorOrImageRes(R.color.colorPrimary)
                .go(() -> {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class)
                            .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();
                });


    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void wechat(){
        Platform wechat = new Wechat();
        wechat.SSOSetting(false);  //设置false表示使用SSO授权方式
        wechat.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d(TAG, "onComplete: " + wechat.getDb().getUserGender());
                Log.d(TAG, "onComplete: " + wechat.getDb().getUserIcon());
                Log.d(TAG, "onComplete: " + wechat.getDb().getUserId());
                Log.d(TAG, "onComplete: " + wechat.getDb().getUserName());
                if (wechat.isAuthValid()) {
                    //
                    model.login(wechat.getDb().getUserId(), SignWay.WECHAT.ordinal(),
                            wechat.getDb().getUserName(), null);

                }
// wechat.getDb().getUserName().replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "")
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d(TAG, "onError: ");
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d(TAG, "onCancel: ");
            }
        });
        wechat.authorize();
        //    wechat.showUser(null);
    }

    public void sina(){
        Platform weibo = new SinaWeibo();
        weibo.SSOSetting(false);
        weibo.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d(TAG, "onComplete: " + weibo.getDb().getUserGender());
                Log.d(TAG, "onComplete: " + weibo.getDb().getUserIcon());
                Log.d(TAG, "onComplete: " + weibo.getDb().getUserId());
                Log.d(TAG, "onComplete: " + weibo.getDb().getUserName());
                if (weibo.isAuthValid()) {
                    //zToastUtil.show(LoginActivity.this,"正在登录");
                    model.login(weibo.getDb().getUserId(), SignWay.WEIBO.ordinal(),
                            weibo.getDb().getUserName(), null);
                    //  weibo.getDb().getUserName().replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "")

                }
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d(TAG, "onError: ");
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d(TAG, "onCancel: ");
            }
        });
        weibo.authorize();
    }

    public void taobao(){
        startActivity(new Intent(this, LoginTbActivity.class));

    }

}