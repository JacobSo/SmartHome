package com.lsapp.smarthome.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.BaseApplication;
import com.lsapp.smarthome.app.Const;
import com.lsapp.smarthome.app.Log;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.databinding.ActivityMainBinding;
import com.lsapp.smarthome.event.DragBackEvent;
import com.lsapp.smarthome.event.NewFeedEvent;
import com.lsapp.smarthome.ui.model.FeedModel;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;
import com.zuni.library.utils.zSharedPreferencesUtil;
import com.zuni.library.utils.zToastUtil;

import java.util.Timer;
import java.util.TimerTask;

import me.majiajie.pagerbottomtabstrip.Controller;
import me.majiajie.pagerbottomtabstrip.TabItemBuilder;
import me.majiajie.pagerbottomtabstrip.TabLayoutMode;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectListener;
import rx.Subscription;

public class MainActivity extends BaseActivity implements OnTabItemSelectListener {
    private Controller controller;
    private static Boolean isExit = false;
    private FeedModel model;
    private ActivityMainBinding binding;
    private MainPagerFragment mainPagerFragment = null;
    private MessageFragment messageFragment = null;
    private ProfileFragment profileFragment = null;
    private Subscription subscription;//feed red point listener

    @Override
    protected void initModel() {
        model = new FeedModel(this);
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initLocation();
        if (!zSharedPreferencesUtil.get(this, Const.GUIDE_SP, Const.GUIDE_INTRO).equals("1")) {
            startActivity(new Intent(this, SplashActivity.class));
        } else {
            if (BaseApplication.get(this).getSessionKey() != null && !BaseApplication.get(this).getSessionKey().equals(""))
                model.feed();
            initSubscription();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        checkUpdateSnack();
        BaseApplication.get(this).initCloudAccount(BaseApplication.get(this).getUserName() + BaseApplication.get(this).getParty());
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().hide();
        }
        TabItemBuilder tabItemBuilder = new TabItemBuilder(this).create()
                .setDefaultIcon(R.drawable.new_home)
                .setText(getString(R.string.tab_home))
                .build();
        controller = binding.tabs.builder()
                .addTabItem(tabItemBuilder)
                .addTabItem(R.drawable.new_message, getString(R.string.tab_message))
                .addTabItem(R.drawable.new_setting, getString(R.string.tab_setting))
                .setMode(TabLayoutMode.HIDE_TEXT)
                .setMessageBackgroundColor(Color.parseColor("#FF5252"))
                .build();
        controller.addTabItemClickListener(this);
        controller.setDisplayOval(1, false);
    }

    private void checkUpdateSnack() {
        PgyUpdateManager.register(MainActivity.this, "lshome",
                new UpdateManagerListener() {

                    @Override
                    public void onUpdateAvailable(final String result) {
                Log.v(getClass().getName(), result + "");
                final AppBean appBean = getAppBeanFromString(result);
                zToastUtil.showSnackWithButton(binding.getRoot(), "发现新版本！", "马上更新", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startDownloadTask(MainActivity.this, appBean.getDownloadURL());
                    }
                });
            }

            @Override
            public void onNoUpdateAvailable() {
                Log.d("PGYER", "onNoUpdateAvailable: update to date");

            }
        });
    }

    public void initSubscription() {
        subscription = RxBus.getDefault().toObserverable(NewFeedEvent.class).subscribe(newFeedEvent -> {
            model.setFeed(newFeedEvent.isNewsFeed());
            controller.setDisplayOval(1, newFeedEvent.isNewsFeed());
        }, Throwable::printStackTrace);

    }

    public void setFragment(Fragment fragment) {
        //  invalidateOptionsMenu();

        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right)
                .add(R.id.container, fragment)
                .commitAllowingStateLoss();
    }

    public void showFragment(Fragment show, Fragment hide1, Fragment hide2) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right)
                .show(show)
                .hide(hide1 == null ? hide2 : hide1)
                .hide(hide2 == null ? hide1 : hide2)
                .commitAllowingStateLoss();
    }

    public void setFeed() {
        controller.setDisplayOval(1, true);
    }

    @Override
    public void onSelected(int index, Object tag) {
        binding.tabs.getChildAt(0).setEnabled(false);
        switch (index) {
            case 0:
                if (mainPagerFragment == null)
                    setFragment(mainPagerFragment = MainPagerFragment.newInstance());
                else {
                    showFragment(mainPagerFragment, messageFragment, profileFragment);
                }
                break;
            case 1:
                if (messageFragment == null)
                    setFragment(messageFragment = MessageFragment.newInstance());
                else {
                    showFragment(messageFragment, mainPagerFragment, profileFragment);
                    if (model.isFeed())
                        messageFragment.onRefresh();
                }
                break;
            case 2:
                if (profileFragment == null)
                    setFragment(profileFragment = ProfileFragment.newInstance());
                else
                    showFragment(profileFragment, messageFragment, mainPagerFragment);
                break;
        }
    }

    @Override
    public void onRepeatClick(int index, Object tag) {

    }

    @Override
    public void onBackPressed() {
        if (controller.getSelected() == 0) {
            if (!isExit) {
                isExit = true;
                Timer tExit = new Timer();
                tExit.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 500);
                RxBus.getDefault().post(new DragBackEvent());
            } else {
                super.onBackPressed();
            }
        } else
            super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();
    }
}
