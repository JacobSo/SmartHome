package com.lsapp.smarthome.ui;

import android.app.AlertDialog;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.view.View;

import com.lsapp.smarthome.R;
import com.lsapp.smarthome.app.RxBus;
import com.lsapp.smarthome.databinding.ActivityGuideBinding;
import com.lsapp.smarthome.event.FragmentSwitchEvent;
import com.lsapp.smarthome.event.GuideSceneEvent;
import com.lsapp.smarthome.ui.model.GuideModel;
import com.zuni.library.utils.zToastUtil;

import rx.Subscription;

/**
 * Created by Administrator on 2016/9/13.
 */
public class GuideActivity extends BaseActivity implements GuideScanFragment.CallBackValue {
    private Subscription fragmentSubscription;
    private Subscription submitSubscription;
    private ActivityGuideBinding binding;
    private GuideModel model;
    private String wifiPwd;
    private final static String TAG = "GuideActivity";

    @Override
    protected void initModel() {
        model = new GuideModel(this);
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_guide);
        setFragment(GuideNetworkFragment.newInstance(), false);
        initSubscription();
    }

    public void setFragment(Fragment fragment, boolean flag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left,
                        R.anim.slide_out_right)
                .replace(R.id.container, fragment);

        if (flag)
            transaction.addToBackStack("1").commitAllowingStateLoss();
        else
            transaction.disallowAddToBackStack().commitAllowingStateLoss();

    }

    public void initSubscription() {
        fragmentSubscription = RxBus.getDefault().toObserverable(FragmentSwitchEvent.class).subscribe(fragmentSwitchEvent -> {
            if (fragmentSwitchEvent.getFragment() == null) {
              //  model.setDevices(null);
                onBackPressed();
                //getSupportFragmentManager().popBackStack("1", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                setFragment(fragmentSwitchEvent.getFragment(), fragmentSwitchEvent.isBack());
            }
        });
        submitSubscription = RxBus.getDefault().toObserverable(GuideSceneEvent.class).subscribe(guideSceneEvent -> {
            model.submit(guideSceneEvent.getPosition(), guideSceneEvent.isExist());
        });
    }

    public void setDeviceFragment() {
        setFragment(GuideDeviceFragment.newInstance(model.getDevices(), model.getScenes()), true);
    }

    public void setStepTwo() {
        setFragment(GuideScanFragment.newInstance(wifiPwd, 1), true);
    }

    public void onClose(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.quit_quick_set));
        builder.setMessage(getString(R.string.quit_quick_set_content));
        builder.setNegativeButton(getString(R.string.cancel), null);
        builder.setPositiveButton(getString(R.string.confirm), (dialogInterface, i) -> {
            finish();
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!fragmentSubscription.isUnsubscribed()) {
            fragmentSubscription.unsubscribe();
        }
        if (!submitSubscription.isUnsubscribed()) {
            submitSubscription.unsubscribe();
        }
    }


    @Override
    public void sendMessageValue(String strValue, String pwd) {
        if (strValue == null && pwd == null) {//step2
            if (model.getDevices() != null && model.getDevices().size() != 0)
                setDeviceFragment();
            else zToastUtil.show(this, getString(R.string.guide_null_device));
        } else if (strValue == null) {//step 1
            setFragment(GuideScanFragment.newInstance(pwd, 1), true);
        } else {
            wifiPwd = pwd;
            model.getByCode(strValue);
        }
    }
}
