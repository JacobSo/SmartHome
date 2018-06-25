package com.lsapp.smarthome.event;

import android.support.v4.app.Fragment;

/**
 * Created by Administrator on 2016/9/14.
 */
public class FragmentSwitchEvent {
    Fragment fragment;
    boolean isBack;
    public FragmentSwitchEvent(Fragment fragment,boolean isBack) {
        this.isBack = isBack;
        this.fragment = fragment;
    }

    public boolean isBack() {
        return isBack;
    }

    public void setBack(boolean back) {
        isBack = back;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
