package com.lsapp.smarthome.ui;

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
import com.lsapp.smarthome.data.base.Scene;
import com.lsapp.smarthome.databinding.ActivityFunctionCustomBinding;
import com.lsapp.smarthome.event.FragmentSwitchEvent;

import rx.Subscription;

/**
 * Created by Administrator on 2016/9/13.
 */
public class CustomizeActivity extends BaseActivity {
    private Subscription fragmentSubscription;
    private ActivityFunctionCustomBinding binding;

    @Override
    protected void initModel() {
        fragmentSubscription = RxBus.getDefault().toObserverable(FragmentSwitchEvent.class).subscribe(fragmentSwitchEvent -> {

            if (fragmentSwitchEvent.getFragment() == null) {
                getSupportFragmentManager().popBackStack("1", FragmentManager.POP_BACK_STACK_INCLUSIVE);
            } else {
                setFragment(fragmentSwitchEvent.getFragment(), fragmentSwitchEvent.isBack());
            }
        });
    }

    @Override
    public View getBindingView() {
        return binding.getRoot();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_function_custom);
        setTitle(binding.toolbar, getString(R.string.customize_function), true);
        CustomSelectFragment indexFragment = CustomSelectFragment.newInstance((Scene) getIntent().getSerializableExtra("scene"));
        setFragment(indexFragment, false);
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

    }


}
